package cheapwatch.format.model;

import org.joml.Vector3fc;

public record OverwatchBone(
	String name,
	int parentIndex,
	Vector3fc position,
	Vector3fc scale,
	Vector3fc rotationEuler
) {}