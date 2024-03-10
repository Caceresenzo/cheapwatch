package opengl.util;

import java.util.List;

import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public class Vectors {

	public static final List<String> XYZ = List.of("x", "y", "z");

	private static final Vector3ic ZERO_3I = new Vector3i(0);

	public static Vector3ic zero3i() {
		return ZERO_3I;
	}

	public static float[] flatten3f(List<? extends Vector3fc> vectors) {
		final var size = vectors.size();
		final var array = new float[size * 3];

		for (var index = 0; index < size; ++index) {
			final var vector = vectors.get(index);
			array[index * 3 + 0] = vector.x();
			array[index * 3 + 1] = vector.y();
			array[index * 3 + 2] = vector.z();
		}

		return array;
	}

	public static float[] flatten2f(List<? extends Vector2fc> vectors) {
		final var size = vectors.size();
		final var array = new float[size * 2];

		for (var index = 0; index < size; ++index) {
			final var vector = vectors.get(index);
			array[index * 2 + 0] = vector.x();
			array[index * 2 + 1] = vector.y();
		}

		return array;
	}

	public static int[] flatten3i(List<? extends Vector3ic> vectors) {
		final var size = vectors.size();
		final var array = new int[size * 3];

		for (var index = 0; index < size; ++index) {
			final var vector = vectors.get(index);
			array[index * 3 + 0] = vector.x();
			array[index * 3 + 1] = vector.y();
			array[index * 3 + 2] = vector.z();
		}

		return array;
	}

}