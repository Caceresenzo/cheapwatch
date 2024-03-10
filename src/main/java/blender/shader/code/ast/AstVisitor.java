package blender.shader.code.ast;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AstVisitor {

	private final StringBuilder builder;
	private int depth;
	private int comment;

	public AstVisitor() {
		this(new StringBuilder());
	}

	public void visit(AstElement element) {
		switch (element) {
			case BinaryOperation binaryOperation -> {
				visit(binaryOperation.left());

				append(" ");
				append(binaryOperation.operator());
				append(" ");

				visit(binaryOperation.right());
			}

			case Block block -> {
				append("{");

				++depth;
				for (final var statement : block.statements()) {
					appendLine();
					visit(statement);
				}
				--depth;

				append("}");
				appendEndLine();
			}

			case CommentBlock block -> {
				++comment;

				final var prependText = block.prependText();
				if (prependText != null) {
					appendLine().append(prependText).append("\n");
				}

				for (final var statement : block.statements()) {
					visit(statement);
				}

				final var appendText = block.appendText();
				if (appendText != null) {
					appendLine().append(appendText).append("\n");
				}

				--comment;
			}

			case FunctionCall functionCall -> {
				append(functionCall.name());
				append("(");

				final var iterator = functionCall.arguments().iterator();
				while (iterator.hasNext()) {
					final var argument = iterator.next();
					visit(argument);

					if (iterator.hasNext()) {
						append(", ");
					}
				}

				append(")");
			}

			case Identifier identifier -> {
				append(identifier.name());
			}

			case Litteral litteral -> {
				append(litteral.value());
			}

			case MemberAccess memberAccess -> {
				visit(memberAccess.expression());
				append(".");
				append(memberAccess.componentName());
			}

			case Ternary ternary -> {
				append("(");
				visit(ternary.condition());
				append(") ? (");
				visit(ternary.trueExpression());
				append(") : (");
				visit(ternary.falseExpression());
				append(")");
			}

			case VariableDeclaration variableDeclaration -> {
				appendLine();
				append(variableDeclaration.typeName());
				append(" ");
				append(variableDeclaration.name());

				final var initialValue = variableDeclaration.initialValue();
				if (initialValue != null) {
					append(" = ");
					visit(initialValue);
				}

				appendEndLine();
			}
		}
	}

	private AstVisitor appendLine() {
		if (isInComment()) {
			builder.append("// ");
		}

		builder
			.append("\t".repeat(depth));

		return this;
	}

	private AstVisitor appendEndLine() {
		builder.append(";\n");

		return this;
	}

	private AstVisitor append(String code) {
		builder
			.append(code);

		return this;
	}

	public boolean isInComment() {
		return comment != 0;
	}

}