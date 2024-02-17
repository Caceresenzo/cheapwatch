package cheapwatch.format.map;

import java.util.List;

import org.joml.Vector3fc;

public record OverwatchSound(
	Vector3fc position,
	List<String> paths
) {}