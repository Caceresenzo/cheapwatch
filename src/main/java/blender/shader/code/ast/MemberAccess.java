package blender.shader.code.ast;

public record MemberAccess(
	AstNode expression,
	String componentName
) implements AstNode {}