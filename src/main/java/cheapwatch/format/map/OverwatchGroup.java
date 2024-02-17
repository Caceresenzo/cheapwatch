package cheapwatch.format.map;

import java.util.List;

public record OverwatchGroup(
	String materialPath,
	List<OverwatchInstance> instances
) {}