package blender.shader.code;

import java.util.List;

import blender.shader.code.ast.AstStatement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShaderCodeWriter {

	private final @Getter List<AstStatement> statements;
	private final @Getter ShaderCodeGenerator codeGenerator;

	public void append(AstStatement statement) {
		statements.add(statement);
	}
	
	public void append(List<AstStatement> statements) {
		this.statements.addAll(statements);
	}

}