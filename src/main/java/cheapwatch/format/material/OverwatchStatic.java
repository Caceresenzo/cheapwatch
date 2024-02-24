package cheapwatch.format.material;

import it.unimi.dsi.fastutil.bytes.ByteList;

public record OverwatchStatic(
	long id,
	ByteList data
) {}