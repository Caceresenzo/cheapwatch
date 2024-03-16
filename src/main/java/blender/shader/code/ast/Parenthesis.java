package blender.shader.code.ast;

public record Parenthesis(
	AstNode expression
) implements AstNode {}