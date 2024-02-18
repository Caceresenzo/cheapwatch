package cheapwatch.format.model;

import org.joml.Quaternionfc;
import org.joml.Vector3fc;

public record OverwatchHardpoint(
	String name,
	String boneName,
	Vector3fc position,
	Quaternionfc rotation
) {}