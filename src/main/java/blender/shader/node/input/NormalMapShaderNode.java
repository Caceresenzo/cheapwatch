package blender.shader.node.input;

import java.util.List;

import org.joml.Vector4f;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.code.ast.BinaryOperation;
import blender.shader.code.ast.FunctionCall;
import blender.shader.code.ast.Identifier;
import blender.shader.code.ast.Litteral;
import blender.shader.code.ast.MemberAccess;
import blender.shader.code.ast.VariableDeclaration;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class NormalMapShaderNode extends ShaderNode {

	public static final List<ShaderSocket> INPUTS = List.of(
		new ShaderSocket("Strength", ShaderDataType.VALUE, 1.0f, 0),
		new ShaderSocket("Color", ShaderDataType.RGBA, new Vector4f(0.5f, 0.5f, 1.0f, 1.0f), 1)
	);

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Normal", ShaderDataType.SHADER, 0)
	);

	private final Space space;

	@Override
	public List<ShaderSocket> getInputs() {
		return INPUTS;
	}

	@Override
	public List<ShaderSocket> getOutputs() {
		return OUTPUTS;
	}

	@Override
	public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
		final var strength = variables.getInput(0);
		final var color = variables.getInput(1);

		final var result = variables.getOutput(0);

		if (Space.TANGENT.equals(space)) {
			final var v = variables.getTemporary("v", ShaderDataType.VECTOR);
			final var b = variables.getTemporary("B", ShaderDataType.VECTOR);

			writer.append(new VariableDeclaration(
				v.type().getCodeType(),
				v.name(),
				BinaryOperation.chain(
					new Identifier(color.name()),
					"*",
					new Litteral("2.0"),
					"-",
					new Litteral("1.0")
				)
			));

			writer.append(new VariableDeclaration(
				b.type().getCodeType(),
				b.name(),
				new FunctionCall(
					"normalize",
					new FunctionCall(
						"cross",
						new Identifier("mxp_normal"),
						new Identifier("mxp_tangent")
					)
				)
			));

			writer.append(new VariableDeclaration(
				result.type().getCodeType(),
				result.name(),
				new FunctionCall(
					"normalize",
					BinaryOperation.chain(
						new Identifier("mxp_tangent"),
						"*",
						new MemberAccess(
							new Identifier(v.name()),
							"x"
						),
						"*",
						new Identifier(strength.name()),
						"+",
						new Identifier(b.name()),
						"*",
						new MemberAccess(
							new Identifier(v.name()),
							"y"
						),
						"*",
						new Identifier(strength.name()),
						"+",
						new Identifier("mxp_normal"),
						"*",
						new MemberAccess(
							new Identifier(v.name()),
							"z"
						)
					)
				)
			));
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public enum Space {

		TANGENT,
		OBJECT,
		WORLD;

	}

}