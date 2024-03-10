package blender.shader.node.input;

import java.util.Collections;
import java.util.List;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariable;
import blender.shader.code.ShaderVariables;
import blender.shader.code.ast.AstStatement;
import blender.shader.code.ast.CommentBlock;
import blender.shader.code.ast.Litteral;
import blender.shader.code.ast.VariableDeclaration;
import blender.shader.node.ShaderNode;
import lombok.ToString;

@ToString(callSuper = true)
public class LightPathShaderNode extends ShaderNode {

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Is Camera Ray", ShaderDataType.VALUE, 0),
		new ShaderSocket("Is Shadow Ray", ShaderDataType.VALUE, 1),
		new ShaderSocket("Is Diffuse Ray", ShaderDataType.VALUE, 2),
		new ShaderSocket("Is Glossy Ray", ShaderDataType.VALUE, 3),
		new ShaderSocket("Is Singular Ray", ShaderDataType.VALUE, 4),
		new ShaderSocket("Is Reflection Ray", ShaderDataType.VALUE, 5),
		new ShaderSocket("Is Transmission Ray", ShaderDataType.VALUE, 6),
		new ShaderSocket("Ray Length", ShaderDataType.VALUE, 7),
		new ShaderSocket("Ray Depth", ShaderDataType.VALUE, 8),
		new ShaderSocket("Diffuse Depth", ShaderDataType.VALUE, 9),
		new ShaderSocket("Glossy Depth", ShaderDataType.VALUE, 10),
		new ShaderSocket("Transparent Depth", ShaderDataType.VALUE, 11),
		new ShaderSocket("Transmission Depth", ShaderDataType.VALUE, 12)
	);

	@Override
	public List<ShaderSocket> getInputs() {
		return Collections.emptyList();
	}

	@Override
	public List<ShaderSocket> getOutputs() {
		return OUTPUTS;
	}

	@Override
	public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
		for (final var output : variables.getOutputs()) {
			writer.append(toAstNode(output));
		}
	}

	public AstStatement toAstNode(ShaderVariable output) {
		AstStatement block = new VariableDeclaration(
			output.type().getCodeType(),
			output.name(),
			new Litteral("0.0")
		);

		if (!output.linked()) {
			block = new CommentBlock(
				List.of(block)
			);
		}

		return block;
	}

}