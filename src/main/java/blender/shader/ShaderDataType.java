package blender.shader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

@RequiredArgsConstructor
@Getter
public abstract class ShaderDataType<T> {

	public static final ShaderDataType<Float> VALUE = new ShaderDataType<>("VALUE", "float", 0.0f) {

		@Override
		public String render(Object value) {
			return "%sf".formatted((float) value);
		}

		@Override
		public Float parse(JsonNode valueNode) {
			return valueNode.floatValue();
		}

	};

	public static final ShaderDataType<Vector3fc> VECTOR = new ShaderDataType<>("VECTOR", "vec3", new Vector3f()) {

		@Override
		public String render(Object value) {
			final var vector = (Vector3fc) value;

			return "vec3(%sf, %sf, %sf)".formatted(vector.x(), vector.y(), vector.z());
		}

		@Override
		public Vector3fc parse(JsonNode valueNode) {
			final var x = valueNode.get("x").floatValue();
			final var y = valueNode.get("y").floatValue();
			final var z = valueNode.get("z").floatValue();

			return new Vector3f(x, y, z);
		}

	};

	public static final ShaderDataType<Vector3fc> ROTATION = new ShaderDataType<>("ROTATION", VECTOR.codeType, VECTOR.defaultValue) {

		@Override
		public String render(Object value) {
			return VECTOR.render(value);
		}

		@Override
		public Vector3fc parse(JsonNode valueNode) {
			return VECTOR.parse(valueNode);
		}

	};

	public static final ShaderDataType<Vector4fc> RGBA = new ShaderDataType<>("RGBA", "vec4", new Vector4f()) {

		@Override
		public String render(Object value) {
			final var vector = (Vector4fc) value;

			return "vec4(%sf, %sf, %sf, %sf)".formatted(vector.x(), vector.y(), vector.z(), vector.w());
		}

		@Override
		public Vector4f parse(JsonNode valueNode) {
			final var x = valueNode.get("x").floatValue();
			final var y = valueNode.get("y").floatValue();
			final var z = valueNode.get("z").floatValue();
			final var a = valueNode.get("a").floatValue();

			return new Vector4f(x, y, z, a);
		}

	};

	public static final ShaderDataType<Vector4fc> SHADER = new ShaderDataType<>("SHADER", RGBA.codeType, RGBA.defaultValue) {

		@Override
		public String render(Object value) {
			return RGBA.render(value);
		}

		@Override
		public Vector4fc parse(JsonNode valueNode) {
			if (valueNode == null) {
				return null;
			}

			return RGBA.parse(valueNode);
		}

	};

	private final String name;
	private final String codeType;
	private final T defaultValue;

    public abstract String render(Object value);

	public abstract T parse(JsonNode valueNode);

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public static ShaderDataType<?> valueOf(String name) {
		return switch (name) {
			case "VALUE" -> VALUE;
			case "VECTOR" -> VECTOR;
			case "RGBA" -> RGBA;
			case "ROTATION" -> ROTATION;
			case "SHADER" -> SHADER;
			default -> throw new IllegalArgumentException("unknown type: %s".formatted(name));
		};
	}

}