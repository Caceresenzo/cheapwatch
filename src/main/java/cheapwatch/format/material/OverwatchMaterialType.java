package cheapwatch.format.material;

public enum OverwatchMaterialType {

	MATERIAL,
	MODEL_LOOK;

	public static OverwatchMaterialType valueOf(int ordinal) {
		return switch (ordinal) {
			case 0 -> MATERIAL;
			case 1 -> MODEL_LOOK;
			default -> throw new IllegalArgumentException("Unexpected value: " + ordinal);
		};
	}

}