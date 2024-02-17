package cheapwatch.format.map;

import java.util.List;

public record OverwatchObject(
	String modelPath,
	List<OverwatchGroup> groups
) {}