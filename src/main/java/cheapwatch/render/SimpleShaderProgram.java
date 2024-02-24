package cheapwatch.render;

import java.io.IOException;

import opengl.shader.Shader;
import opengl.shader.ShaderProgram;
import opengl.shader.variable.attribute.FloatAttribute;
import opengl.shader.variable.uniform.Matrix4fUniform;
import opengl.shader.variable.uniform.SamplerUniform;

public class SimpleShaderProgram extends ShaderProgram {

	public final Matrix4fUniform projection;
	public final Matrix4fUniform model;
	public final Matrix4fUniform view;
	public final SamplerUniform texture;
	public final FloatAttribute position;
	public final FloatAttribute uv;

	public SimpleShaderProgram(Shader vertexShader, Shader fragmentShader) {
		super(vertexShader, fragmentShader);

		this.projection = createMatrix4fUniform("projection");
		this.model = createMatrix4fUniform("model");
		this.view = createMatrix4fUniform("view");
		this.texture = createSamplerUniform("textureSampler");
		this.position = createFloatAttribute("in_Positions", 3);
		this.uv = createFloatAttribute("in_UV", 2);
	}

	public static SimpleShaderProgram create() throws IOException {
		try (
			final var vertexInputStream = Shader.class.getResourceAsStream("/shaders/simple.vert");
			final var fragmentInputStream = Shader.class.getResourceAsStream("/shaders/simple.frag");
		) {
			return new SimpleShaderProgram(
				Shader.load(Shader.Type.VERTEX, vertexInputStream),
				Shader.load(Shader.Type.FRAGMENT, fragmentInputStream)
			);
		}
	}

}