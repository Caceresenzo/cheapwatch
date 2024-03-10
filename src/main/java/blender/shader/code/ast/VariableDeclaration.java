package blender.shader.code.ast;

public record VariableDeclaration(
	String typeName,
	String name,
	AstNode initialValue
) implements AstStatement {}