package cheapwatch.format.material;

import java.util.List;

public record OverwatchModelLook(
	List<Entry> entries
) {

	public static record Entry(
		long id,
		String materialPath
	) {}

}