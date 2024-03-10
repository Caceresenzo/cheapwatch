package blender.shader.code.ast;

public sealed interface AstStatement extends AstElement permits Block, CommentBlock, VariableDeclaration {

}