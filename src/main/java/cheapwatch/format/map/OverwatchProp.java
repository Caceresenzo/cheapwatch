package cheapwatch.format.map;

import org.joml.Quaternionfc;
import org.joml.Vector3fc;

public record OverwatchProp(
	String entityPath,
	String materialPath,
	Vector3fc position,
	Vector3fc scale,
	Quaternionfc rotation
) {}