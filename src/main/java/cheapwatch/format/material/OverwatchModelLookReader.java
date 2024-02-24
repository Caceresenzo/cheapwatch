package cheapwatch.format.material;

import java.nio.ByteBuffer;

import cheapwatch.format.OverwatchReader;
import cheapwatch.format.OverwatchVersion;

public class OverwatchModelLookReader extends OverwatchReader<OverwatchModelLook> {

	public static final OverwatchVersion VERSION = new OverwatchVersion(3, 0);

	public OverwatchModelLookReader(ByteBuffer buffer) {
		super(buffer);
	}

	@Override
	public OverwatchModelLook get() {
		final var version = readVersion();
		if (!VERSION.equals(version)) {
			throw new IllegalStateException("invalid version: " + version);
		}

		final var type = OverwatchMaterialType.valueOf(readAndCastUnsignedInteger());
		if (!OverwatchMaterialType.MODEL_LOOK.equals(type)) {
			throw new IllegalStateException("invalid type: " + type);
		}

		final var count = Math.toIntExact(readUnsignedLong());
		final var entries = readArray(count, this::readEntry);

		return new OverwatchModelLook(
			entries
		);
	}

	public OverwatchModelLook.Entry readEntry() {
		final var id = readUnsignedLong();
		final var materialPath = readString();

		return new OverwatchModelLook.Entry(
			id,
			materialPath
		);
	}

}