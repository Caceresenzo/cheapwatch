package cheapwatch.format.map;

import java.util.List;

public record OverwatchMap(
	String name,
	List<OverwatchObject> objects,
	List<OverwatchProp> props,
	List<OverwatchLight> lights,
	List<OverwatchSound> sounds
) {}