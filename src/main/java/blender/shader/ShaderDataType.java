package blender.shader;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

@RequiredArgsConstructor
@Getter
public enum ShaderDataType {

	VALUE("float") {

		@Override
		public String renderDefaultValue(Object defaultValue) {
			return "%sf".formatted((float) defaultValue);
		}

		@Override
		public Object parseDefaultValue(JsonNode valueNode) {
			return valueNode.floatValue();
		}

	},
	VECTOR("vec3") {

		@Override
		public String renderDefaultValue(Object defaultValue) {
			final var vector = (Vector3fc) defaultValue;

			return "vec3(%sf, %sf, %sf)".formatted(vector.x(), vector.y(), vector.z());
		}

		@Override
		public Object parseDefaultValue(JsonNode valueNode) {
			final var x = valueNode.get(0).floatValue();
			final var y = valueNode.get(1).floatValue();
			final var z = valueNode.get(2).floatValue();

			return new Vector3f(x, y, z);
		}

	},
	RGBA("vec4") {

		@Override
		public String renderDefaultValue(Object defaultValue) {
			final var vector = (Vector4fc) defaultValue;

			return "vec4(%sf, %sf, %sf, %sf)".formatted(vector.x(), vector.y(), vector.z(), vector.w());
		}

		@Override
		public Object parseDefaultValue(JsonNode valueNode) {
			final var x = valueNode.get(0).floatValue();
			final var y = valueNode.get(1).floatValue();
			final var z = valueNode.get(2).floatValue();
			final var w = valueNode.get(3).floatValue();

			return new Vector4f(x, y, z, w);
		}

	};

	private final String codeType;

	public abstract String renderDefaultValue(Object defaultValue);

	public abstract Object parseDefaultValue(JsonNode valueNode);

}