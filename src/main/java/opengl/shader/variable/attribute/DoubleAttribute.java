package opengl.shader.variable.attribute;

import static org.lwjgl.opengl.GL41.glVertexAttribLPointer;

import opengl.shader.DataType;
import opengl.shader.ShaderProgram;

public class DoubleAttribute extends Attribute {

	protected DoubleAttribute(ShaderProgram program, String name, int size, DataType dataType) {
		super(program, name, size, dataType);
	}

	protected void doLink(int stride, int offset) {
		glVertexAttribLPointer(location, size, dataType.value(), stride, offset);
	}

	public static DoubleAttribute ofDouble(ShaderProgram shaderProgram, String name, int size) {
		return new DoubleAttribute(shaderProgram, name, size, DataType.DOUBLE);
	}

}