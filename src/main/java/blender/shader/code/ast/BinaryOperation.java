package blender.shader.code.ast;

public record BinaryOperation(
	String operator,
	AstNode left,
	AstNode right
) implements AstNode {}