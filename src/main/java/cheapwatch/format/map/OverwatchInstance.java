package cheapwatch.format.map;

import org.joml.Quaternionfc;
import org.joml.Vector3fc;

public record OverwatchInstance(
	Vector3fc position,
	Vector3fc scale,
	Quaternionfc rotation
) {}