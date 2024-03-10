package blender.shader.code.ast;

public record Paranthesis(
	AstNode expression
) implements AstNode {}