package cheapwatch.format.map;

import org.joml.Quaternionfc;
import org.joml.Vector3fc;

public record OverwatchLight(
	Vector3fc position,
	Quaternionfc rotation,
	OverwatchLightType type,
	float fov,
	Vector3fc color,
	float intensity
) {}