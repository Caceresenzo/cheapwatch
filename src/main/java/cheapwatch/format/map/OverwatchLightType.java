package cheapwatch.format.map;

public enum OverwatchLightType {

	POINT,
	SPOT;

	public static OverwatchLightType valueOf(int ordinal) {
		return switch (ordinal) {
			case 0 -> POINT;
			case 1 -> SPOT;
			default -> throw new IllegalArgumentException("Unexpected value: " + ordinal);
		};
	}

}