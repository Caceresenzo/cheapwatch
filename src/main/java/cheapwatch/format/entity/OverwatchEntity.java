package cheapwatch.format.entity;

import java.util.List;

public record OverwatchEntity(
	String name,
	String modelName,
	String effectName,
	long guid,
	long modelGuid,
	long effectGuid,
	List<OverwatchSubEntity> subEntities,
	String lookName,
	String pathsRelativeTo
) {}