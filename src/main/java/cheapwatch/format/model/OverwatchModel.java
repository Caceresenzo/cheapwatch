package cheapwatch.format.model;

import java.util.List;

public record OverwatchModel(
	String lookFileName,
	String name,
	long guid,
	List<OverwatchBone> bones,
	List<OverwatchMesh> meshes,
	List<OverwatchHardpoint> hardpoints
) {}