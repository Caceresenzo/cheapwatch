package blender.shader.code.ast;

import java.util.List;

public record Block(
	List<AstStatement> statements
) implements AstStatement {}