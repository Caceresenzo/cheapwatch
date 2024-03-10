package blender.shader.code.ast;

import java.util.List;

public record FunctionCall(
	String name,
	List<AstNode> arguments
) implements AstNode {}