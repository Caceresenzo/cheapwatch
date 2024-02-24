package cheapwatch.debug;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3fc;

import cheapwatch.render.Camera;
import cheapwatch.render.SimpleShaderProgram;
import opengl.vertex.BufferUsage;
import opengl.vertex.VertexArray;
import opengl.vertex.VertexBuffer;

public class CubeMesh {

	private final SimpleShaderProgram shaderProgram;
	private VertexArray vertexArray;
	private Matrix4f modelMatrix;

	public CubeMesh(SimpleShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
		this.modelMatrix = new Matrix4f();

		createVertexArray();
	}

	public void move(Vector3fc position) {
		modelMatrix.identity().translate(position.x(), position.y(), position.z());
	}

	private void createVertexArray() {
		final var indices = VertexBuffer.ofIndice(BufferUsage.STATIC_DRAW);
		indices.store(new int[] {
			0, 2, 3,
			0, 1, 2,
			1, 7, 2,
			1, 6, 7,
			6, 5, 4,
			4, 7, 6,
			3, 4, 5,
			3, 5, 0,
			3, 7, 4,
			3, 2, 7,
			0, 6, 1,
			0, 5, 6
		});

		final var positions = VertexBuffer.ofArray(BufferUsage.STATIC_DRAW);
		positions.store(new float[] {
			0, 0, 1,
			1, 0, 1,
			1, 1, 1,
			0, 1, 1,
			0, 1, 0,
			0, 0, 0,
			1, 0, 0,
			1, 1, 0
		});

		final var array = new VertexArray(shaderProgram);
		array.add(indices);
		array.add(positions, List.of(shaderProgram.position));

		if (vertexArray != null) {
			vertexArray.delete(true);
		}

		this.vertexArray = array;
	}

	public void render(Camera camera) {
		shaderProgram.use();
		shaderProgram.view.load(camera.getView());
		shaderProgram.model.load(modelMatrix);
		vertexArray.render();
	}

}