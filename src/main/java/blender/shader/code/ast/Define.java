package blender.shader.code.ast;

public record Define(
	String key,
	String value
) implements AstStatement {}