package cheapwatch.format.entity;

public record OverwatchSubEntity(
	String path,
	long id,
	long idGuid,
	long hardpoint,
	long hardpointGuid,
	String hardpointName
) {}