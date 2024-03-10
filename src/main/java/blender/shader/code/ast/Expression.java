package blender.shader.code.ast;

public record Expression(
	AstNode expression
) implements AstStatement {}