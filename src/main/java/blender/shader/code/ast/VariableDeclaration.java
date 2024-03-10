package blender.shader.code.ast;

public record VariableDeclaration(
	String typeName,
	Identifier identifier,
	AstNode initialValue
) implements AstStatement {}