package cheapwatch;

import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3fc;

import cheapwatch.debug.CubeMesh;
import cheapwatch.debug.ScopShaderProgram;
import cheapwatch.format.OverwatchLoader;
import cheapwatch.render.MovableCamera;
import cheapwatch.state.GameState;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import opengl.OpenGL;
import opengl.util.Vectors;
import opengl.vertex.BufferUsage;
import opengl.vertex.VertexArray;
import opengl.vertex.VertexBuffer;

public class Bootstrap implements GameState {

	private ScopShaderProgram scopShaderProgram;
	private CubeMesh cubeMesh;
	private OverwatchLoader loader;
	private MovableCamera camera;
	private List<GameObject> gameObjects;

	@FieldDefaults(level = AccessLevel.PUBLIC)
	static class GameObject {

		Vector3fc position;
		Matrix4f modelMatrix;
		List<Mesh> meshes;

	}

	@FieldDefaults(level = AccessLevel.PUBLIC)
	static class Mesh {

		VertexArray vertexArray;

	}

	@Override
	@SneakyThrows
	public void initialize() {
		camera = new MovableCamera();

		scopShaderProgram = ScopShaderProgram.create();
		scopShaderProgram.use();
		scopShaderProgram.projection.load(camera.getProjection());

		cubeMesh = new CubeMesh(scopShaderProgram);

		gameObjects = new ArrayList<>();

//		final var root = Paths.get("Games\\Extracts\\Maps\\Dorado\\2C3\\");
//		final var root = Paths.get("Games\\Extracts\\Maps\\Route 66\\5BB");
		final var root = Paths.get("Games\\Extracts\\Maps\\Junkertown\\756");
		//		final var root = Paths.get("Games\\Extracts\\Maps\\Château Guillard\\7A4");
		loader = OverwatchLoader.of(root);

//		final var map = loader.loadMap(Paths.get("Evening - Escort.owmap"));
//		final var map = loader.loadMap(Paths.get("Morning - Escort.owmap"));
		final var map = loader.loadMap(Paths.get("Morning - Escort.owmap"));
		//		final var map = loader.loadMap(Paths.get("Evening - Deathmatch.owmap"));
		for (final var object : map.objects()) {
			final var path = Paths.get(object.modelPath());
			final var model = loader.loadModel(path);

			final var meshes = new ArrayList<Mesh>(model.meshes().size());
			for (final var overwatchMesh : model.meshes()) {
				final var indices = VertexBuffer.ofIndice(BufferUsage.STATIC_DRAW);
				indices.store(Vectors.flatten3i(overwatchMesh.indexes()));

				final var positions = VertexBuffer.ofArray(BufferUsage.STATIC_DRAW);
				positions.store(flatten3fSwapYZ(overwatchMesh.positions()));

				final var array = new VertexArray(scopShaderProgram);
				array.add(indices);
				array.add(positions);

				final var mesh = new Mesh();
				mesh.vertexArray = array;

				meshes.add(mesh);
			}

			for (final var group : object.groups()) {
				for (final var instance : group.instances()) {
					final var gameObject = new GameObject();
					gameObject.position = instance.position();
					gameObject.meshes = meshes;
					gameObject.modelMatrix = new Matrix4f()
						.identity()
						.translate(instance.position().x(), instance.position().y(), instance.position().z())
						.scale(instance.scale())
						.rotate(instance.rotation());

					gameObjects.add(gameObject);
				}
			}
		}
	}

	@Override
	public void update() {
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
			for (final var mesh : gameObject.meshes) {
				mesh.vertexArray.render();
			}
		}

		OpenGL.checkErrors();
	}

	@Override
	public void cleanup() {
		glfwSetMouseButtonCallback(Game.window, null);
	}

	public static void main(String[] args) {
		Game.run(new Bootstrap());
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