package cheapwatch.debug;

import java.io.IOException;

import opengl.shader.Shader;
import opengl.shader.ShaderProgram;
import opengl.shader.variable.attribute.FloatAttribute;
import opengl.shader.variable.uniform.Matrix4fUniform;

public class ScopShaderProgram extends ShaderProgram {

	public final Matrix4fUniform projection;
	public final Matrix4fUniform model;
	public final Matrix4fUniform view;
	public final FloatAttribute position;

	public ScopShaderProgram(Shader vertexShader, Shader fragmentShader) {
		super(vertexShader, fragmentShader);

		this.projection = createMatrix4fUniform("projection");
		this.model = createMatrix4fUniform("model");
		this.view = createMatrix4fUniform("view");
		this.position = createFloatAttribute("in_Positions", 3);
	}

	public static ScopShaderProgram create() throws IOException {
		try (
			final var vertexInputStream = Shader.class.getResourceAsStream("/shaders/scop.vert");
			final var fragmentInputStream = Shader.class.getResourceAsStream("/shaders/scop.frag");
		) {
			return new ScopShaderProgram(
				Shader.load(Shader.Type.VERTEX, vertexInputStream),
				Shader.load(Shader.Type.FRAGMENT, fragmentInputStream)
			);
		}
	}

}