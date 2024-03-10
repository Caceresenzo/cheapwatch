package blender.shader.code;

import blender.shader.code.ast.AstStatement;
import blender.shader.code.ast.AstVisitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShaderCodeWriter {

	private final StringBuilder builder;

	public void append(AstStatement statement) {
		final var visitor = new AstVisitor(builder);

		visitor.visit(statement);
	}

}