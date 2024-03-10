package blender.shader.code.ast;

public sealed interface AstNode extends AstElement permits BinaryOperation, FunctionCall, Identifier, IndexAccess, Litteral, MemberAccess, Paranthesis, Ternary {}