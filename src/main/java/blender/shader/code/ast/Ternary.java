package blender.shader.code.ast;

public record Ternary(
	AstNode condition,
	AstNode trueExpression,
	AstNode falseExpression
) implements AstNode {}