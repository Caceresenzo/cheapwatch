package blender.shader.code.ast;

public record IndexAccess(
	AstNode base,
	AstNode index
) implements AstNode {}