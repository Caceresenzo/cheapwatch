package cheapwatch.format.map;

import org.joml.Quaternionfc;
import org.joml.Vector3fc;

public record OverwatchLight(
	Vector3fc position,
	Quaternionfc rotation,
	Type type,
	float fov,
	Vector3fc color,
	float intensity
) {

	public enum Type {

		POINT,
		SPOT;

		public static Type valueOf(int ordinal) {
			return switch (ordinal) {
				case 0 -> OverwatchLight.Type.POINT;
				case 1 -> OverwatchLight.Type.SPOT;
				default -> throw new IllegalArgumentException("Unexpected value: " + ordinal);
			};
		}

	}

}