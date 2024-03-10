package blender.shader.code.ast;

import java.util.List;

public record CommentBlock(
	List<AstStatement> statements,
	String prependText,
	String appendText
) implements AstStatement {
	
	public CommentBlock(List<AstStatement> statements) {
		this(statements, null, null);
	}
	
}