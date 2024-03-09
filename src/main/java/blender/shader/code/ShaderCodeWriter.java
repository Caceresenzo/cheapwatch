package blender.shader.code;

import blender.shader.ShaderDataType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShaderCodeWriter {

	private final StringBuilder builder;

	public ShaderCodeWriter comment() {
		builder
			.append("// ");

		return this;
	}

	public ShaderCodeWriter comment(String text) {
		builder
			.append("// ")
			.append(text);

		return this;
	}

	public ShaderCodeWriter text(String text) {
		builder
			.append(text);

		return this;
	}

	public ShaderCodeWriter declareAndAssign(ShaderVariable variable) {
		builder
			.append(variable.type().getCodeType())
			.append(" ")
			.append(variable.name())
			.append(" = ");

		return this;
	}

	public <T> ShaderCodeWriter value(ShaderVariable variable) {
		builder
			.append(variable.name());

		return this;
	}

	public <T> ShaderCodeWriter value(ShaderDataType<T> type, T value) {
		builder
			.append(type.render(value));

		return this;
	}

	public ShaderCodeWriter value(String type, String... arguments) {
		builder
			.append(type)
			.append("(")
			.append(String.join(", ", arguments))
			.append(")");

		return this;
	}

	public ShaderCodeWriter endLine() {
		builder
			.append(";\n");

		return this;
	}

	public ShaderCodeWriter prepareCall(String name) {
		builder
			.append(name)
			.append("(");

		return this;
	}

	public ShaderCodeWriter endCall(String... values) {
		if (values.length != 0) {
			builder
				.append(", ")
				.append(String.join(", ", values));
		}

		builder
			.append(")");

		return this;
	}

	public ShaderCodeWriter useBinaryOperator(String operator, ShaderVariable<?> left, ShaderVariable<?> right) {
		builder
			.append(left.name())
			.append(" ")
			.append(operator)
			.append(" ")
			.append(right.name());

		return this;
	}

	public ShaderCodeWriter useFunctionCall(String name, ShaderVariable<?> x) {
		builder
			.append(name)
			.append("(")
			.append(x.name())
			.append(")");

		return this;
	}

	public ShaderCodeWriter useBiFunctionCall(String name, ShaderVariable<?> x, ShaderVariable<?> y) {
		builder
			.append(name)
			.append("(")
			.append(x.name())
			.append(", ")
			.append(y.name())
			.append(")");

		return this;
	}

	public ShaderCodeWriter useComparison(String operator, ShaderVariable<?> left, ShaderVariable<?> right) {
		builder
			.append("((")
			.append(left.name())
			.append(") ")
			.append(operator)
			.append(" (")
			.append(right.name())
			.append(") ? 1.0f : 0.0f)");

		return this;
	}

	public ShaderCodeWriter useTrinaryOperator(String operator1, String operator2, ShaderVariable<?> left, ShaderVariable<?> middle, ShaderVariable<?> right) {
		builder
			.append(left.name())
			.append(" ")
			.append(operator1)
			.append(" ")
			.append(middle.name())
			.append(" ")
			.append(operator2)
			.append(" ")
			.append(right.name());

		return this;
	}

	public ShaderCodeWriter field(String name) {
		builder
			.append(".")
			.append(name);

		return this;
	}

}