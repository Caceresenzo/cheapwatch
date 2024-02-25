package cheapwatch.state;

import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.joml.Matrix4f;
import org.joml.Vector3fc;

import cheapwatch.Game;
import cheapwatch.debug.CubeMesh;
import cheapwatch.format.OverwatchLoader;
import cheapwatch.format.OverwatchTextureMapping;
import cheapwatch.format.OverwatchTextureTypes;
import cheapwatch.format.entity.OverwatchEntity;
import cheapwatch.format.map.OverwatchGroup;
import cheapwatch.format.map.OverwatchInstance;
import cheapwatch.format.map.OverwatchObject;
import cheapwatch.format.map.OverwatchProp;
import cheapwatch.render.MovableCamera;
import cheapwatch.render.SimpleShaderProgram;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import opengl.OpenGL;
import opengl.texture.ImageData;
import opengl.texture.Texture;
import opengl.util.Vectors;
import opengl.vertex.BufferUsage;
import opengl.vertex.VertexArray;
import opengl.vertex.VertexBuffer;

public class PlayState implements GameState {

	private ExecutorService executor;
	private BlockingQueue<List<GameObject>> blockingQueue;
	private SimpleShaderProgram scopShaderProgram;
	private CubeMesh cubeMesh;
	private OverwatchLoader loader;
	private MovableCamera camera;
	private List<GameObject> gameObjects;

	@FieldDefaults(level = AccessLevel.PUBLIC)
	static class GameObject {

		Vector3fc position;
		Matrix4f modelMatrix;
		List<Mesh> meshes;
		Texture texture;

	}

	@FieldDefaults(level = AccessLevel.PUBLIC)
	static class Mesh {

		VertexArray vertexArray;

	}

	@Override
	@SneakyThrows
	public void initialize() {
		executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		//		executor = Executors.newSingleThreadExecutor();
		blockingQueue = new ArrayBlockingQueue<>(20);

		camera = new MovableCamera();

		scopShaderProgram = SimpleShaderProgram.create();
		scopShaderProgram.use();
		scopShaderProgram.projection.load(camera.getProjection());

		cubeMesh = new CubeMesh(scopShaderProgram);

		gameObjects = new ArrayList<>();

		//		final var root = Paths.get("Games\\Extracts\\Maps\\Dorado\\2C3\\");
		final var root = Paths.get("Games\\Extracts\\Maps\\Route 66\\5BB");
		//		final var root = Paths.get("Games\\Extracts\\Maps\\Junkertown\\756");
		//		final var root = Paths.get("Games\\Extracts\\Maps\\Château Guillard\\7A4");
		loader = OverwatchLoader.of(root);

		//		final var map = loader.loadMap(Paths.get("Evening - Escort.owmap"));
		final var map = loader.loadMap(Paths.get("Morning - Escort.owmap"));
		//		final var map = loader.loadMap(Paths.get("Morning - Escort.owmap"));
		//		final var map = loader.loadMap(Paths.get("Evening - Deathmatch.owmap"));
		for (final var object : map.objects()) {
			executor.submit(new ObjectLoadTask(object));
		}

		for (final var prop : map.props()) {
			executor.submit(new PropLoadTask(prop));
		}
	}

	@RequiredArgsConstructor
	class PropLoadTask implements Runnable {

		private final OverwatchProp prop;

		public void process(OverwatchEntity entity) throws IOException {
			for (final var subEntity : entity.subEntities()) {
				final var subPath = Paths.get("Entities", subEntity.path(), "%s.owentity".formatted(subEntity.path()));
				final var entity2 = loader.loadEntity(subPath);
				process(entity2);
			}

			final var modelName = entity.modelName();
			if (StringUtils.isBlank(modelName)) {
				return;
			}

			final var modelPath = Paths.get("Models", modelName, "%s.owmdl".formatted(modelName.replace(".00C", "")));

			// TODO better system
			executor.submit(new ObjectLoadTask(
				new OverwatchObject(
					modelPath.toString(),
					List.of(new OverwatchGroup(
						prop.materialPath(),
						List.of(new OverwatchInstance(
							prop.position(),
							prop.scale(),
							prop.rotation()
						))
					))
				)
			));
		}

		@Override
		public void run() {
			try {
				final var path = Paths.get(prop.entityPath());
				final var entity = loader.loadEntity(path);

				process(entity);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

	}

	@RequiredArgsConstructor
	class ObjectLoadTask implements Runnable {

		private final OverwatchObject object;

		@SneakyThrows
		@Override
		public void run() {
			try {
				final var path = Paths.get(object.modelPath());
				//				final var modelId = path.getParent().getFileName().toString();
				final var model = loader.loadModel(path);

				//			System.out.printf("-- object %s %n", path);

				final var meshes = new ArrayList<Mesh>(model.meshes().size());
				for (final var overwatchMesh : model.meshes()) {
					//				System.out.printf("  load mesh %n");

					final var indicesData = Vectors.flatten3i(overwatchMesh.indexes());
					final var positionsData = flatten3fSwapYZ(overwatchMesh.positions());

					final var uvsData = overwatchMesh.uvss().isEmpty()
						? null
						: Vectors.flatten2f(overwatchMesh.uvss().getFirst());

					final VertexArray array;
					try (final var context = Game.acquireContext()) {
						array = new VertexArray(scopShaderProgram);

						final var indices = VertexBuffer.ofIndice(BufferUsage.STATIC_DRAW);
						indices.store(indicesData);
						array.add(indices);

						final var positions = VertexBuffer.ofArray(BufferUsage.STATIC_DRAW);
						positions.store(positionsData);
						array.add(positions, List.of(scopShaderProgram.position));

						if (uvsData != null) {
							final var uvs = VertexBuffer.ofArray(BufferUsage.STATIC_DRAW);
							uvs.store(uvsData);
							array.add(uvs, List.of(scopShaderProgram.uv));
						}
					}

					final var mesh = new Mesh();
					mesh.vertexArray = array;

					meshes.add(mesh);
				}

				for (final var group : object.groups()) {
					//				System.out.printf("  group %n");

					Texture texture = null;
					final var modelLookPath = Paths.get(group.materialPath());
					final var modelLookId = modelLookPath.getFileName().toString().replace(".owmat", "");
					if (OverwatchTextureMapping.COLLISION_MATERIALS.contains(modelLookId)) {
						continue;
					}

					final var modelLook = loader.loadModelLook(modelLookPath);

					for (final var entry : modelLook.entries()) {
						final var materialPath = modelLookPath.resolve(entry.materialPath()).normalize();
						final var materialId = materialPath.getFileName().toString().replace(".owmat", "");
						if (OverwatchTextureMapping.COLLISION_MATERIALS.contains(materialId)) {
							continue;
						}

						final var material = loader.loadMaterial(materialPath);

						final var albedo = material.getTexture(OverwatchTextureTypes.ALBEDO);
						if (albedo != null) {
							//						System.out.printf("    entry %s %n", entry.id());
							final var texturePath = materialPath.getParent().resolve(albedo.path()).normalize();
							//						System.out.printf("      load texture %s %n", texturePath);

							final ImageData imageData;
							try (final var inputStream = Files.newInputStream(loader.resolve(texturePath))) {
								imageData = ImageData.load(inputStream, false, true);
							}

							try (final var context = Game.acquireContext()) {
								texture = Texture.create(imageData);
							}

							break;
						}
					}

					final var list = new ArrayList<GameObject>();
					for (final var instance : group.instances()) {
						final var gameObject = new GameObject();
						gameObject.position = instance.position();
						gameObject.meshes = meshes;
						gameObject.texture = texture;
						gameObject.modelMatrix = new Matrix4f()
							.identity()
							.translate(instance.position())
							.rotate(instance.rotation())
							.scale(instance.scale());

						list.add(gameObject);
					}

					blockingQueue.put(list);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

	}

	@Override
	public void update() {
		List<GameObject> list;
		while ((list = blockingQueue.poll()) != null) {
			gameObjects.addAll(list);
		}

		camera.update();
	}

	@Override
	public void render() {
		//		for (final var gameObject : gameObjects) {
		//			cubeMesh.move(gameObject.position);
		//			cubeMesh.render(camera);
		//		}

		scopShaderProgram.use();
		scopShaderProgram.view.load(camera.getView());

		for (final var gameObject : gameObjects) {
			scopShaderProgram.model.load(gameObject.modelMatrix);

			if (gameObject.texture == null) {
				continue;
			}

			gameObject.texture.activate(0);
			scopShaderProgram.texture.load(0);

			for (final var mesh : gameObject.meshes) {
				mesh.vertexArray.render();
			}

			gameObject.texture.unbind();
		}

		OpenGL.checkErrors();
	}

	@Override
	public void cleanup() {
		glfwSetMouseButtonCallback(Game.window, null);

		executor.shutdownNow();
	}

	public static float[] flatten3fSwapYZ(List<? extends Vector3fc> vectors) {
		final var size = vectors.size();
		final var array = new float[size * 3];

		for (var index = 0; index < size; ++index) {
			final var vector = vectors.get(index);
			array[index * 3 + 0] = vector.x();
			array[index * 3 + 1] = vector.z();
			array[index * 3 + 2] = -vector.y();
		}

		return array;
	}

}