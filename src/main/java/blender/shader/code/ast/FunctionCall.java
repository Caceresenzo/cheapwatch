package blender.shader.code.ast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record FunctionCall(
	String name,
	List<AstNode> arguments
) implements AstNode {

	public FunctionCall(String name) {
		this(name, Collections.emptyList());
	}

	public FunctionCall(String name, AstNode argument) {
		this(name, Collections.singletonList(argument));
	}

	public FunctionCall(String name, AstNode... arguments) {
		this(name, Arrays.asList(arguments));
	}

}