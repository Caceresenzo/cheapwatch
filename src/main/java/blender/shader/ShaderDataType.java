package blender.shader;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
@Getter
public abstract class ShaderDataType {

	public static final ShaderDataType VALUE = new ShaderDataType("VALUE", Float.class, "float", 0.0f) {

		@Override
		public String render(Object value) {
			return "%s".formatted((float) value);
		}

		@Override
		public Float parse(JsonNode valueNode) {
			return (Float) valueNode.floatValue();
		}

	};

	public static final ShaderDataType VECTOR = new ShaderDataType("VECTOR", Vector3fc.class, "vec3", new Vector3f()) {

		@Override
		public String render(Object value) {
			final var vector = (Vector3fc) value;

			return "vec3(%s, %s, %s)".formatted(vector.x(), vector.y(), vector.z());
		}

		@Override
		public Vector3fc parse(JsonNode valueNode) {
			final var x = valueNode.get("x").floatValue();
			final var y = valueNode.get("y").floatValue();
			final var z = valueNode.get("z").floatValue();

			return new Vector3f(x, y, z);
		}

	};

	public static final ShaderDataType ROTATION = new ShaderDataType("ROTATION", Vector3fc.class, VECTOR.codeType, VECTOR.defaultValue) {

		@Override
		public String render(Object value) {
			return VECTOR.render(value);
		}

		@Override
		public Vector3fc parse(JsonNode valueNode) {
			return (Vector3fc) VECTOR.parse(valueNode);
		}

	};

	public static final ShaderDataType RGBA = new ShaderDataType("RGBA", Vector4fc.class, "vec4", new Vector4f()) {

		@Override
		public String render(Object value) {
			final var vector = (Vector4fc) value;

			return "vec4(%s, %s, %s, %s)".formatted(vector.x(), vector.y(), vector.z(), vector.w());
		}

		@Override
		public Vector4fc parse(JsonNode valueNode) {
			final var x = valueNode.get("x").floatValue();
			final var y = valueNode.get("y").floatValue();
			final var z = valueNode.get("z").floatValue();
			final var a = valueNode.get("a").floatValue();

			return new Vector4f(x, y, z, a);
		}

	};

	public static final ShaderDataType SHADER = new ShaderDataType("SHADER", Vector4fc.class, RGBA.codeType, RGBA.defaultValue) {

		@Override
		public String render(Object value) {
			return RGBA.render(value);
		}

		@Override
		public Vector4fc parse(JsonNode valueNode) {
			if (valueNode == null) {
				return null;
			}

			return (Vector4fc) RGBA.parse(valueNode);
		}

	};

	@EqualsAndHashCode.Include
	private final String name;
	private final Class<?> typeClass;
	private final String codeType;
	private final Object defaultValue;

	public abstract String render(Object value);

	public abstract Object parse(JsonNode valueNode);

	@Override
	public String toString() {
		return name;
	}

	public static ShaderDataType valueOf(String name) {
		return switch (name) {
			case "VALUE", "FLOAT" -> VALUE;
			case "VECTOR" -> VECTOR;
			case "RGBA", "COLOR" -> RGBA;
			case "ROTATION" -> ROTATION;
			case "SHADER" -> SHADER;
			default -> throw new IllegalArgumentException("unknown type: %s".formatted(name));
		};
	}

}