package cheapwatch.format.entity;

import java.nio.ByteBuffer;

import cheapwatch.format.OverwatchReader;
import cheapwatch.format.OverwatchVersion;

public class OverwatchEntityReader extends OverwatchReader<OverwatchEntity> {

	public static final String IDENTIFIER = "owentity";
	public static final OverwatchVersion VERSION = new OverwatchVersion(2, 1);

	public OverwatchEntityReader(ByteBuffer buffer) {
		super(buffer);
	}

	@Override
	public OverwatchEntity get() {
		final var identifier = readString();
		if (!IDENTIFIER.equals(identifier)) {
			throw new IllegalStateException("invalid identifier: " + identifier);
		}

		final var version = readVersion();
		if (!VERSION.equals(version)) {
			throw new IllegalStateException("invalid version: " + version);
		}

		final var name = readString();
		final var modelName = readString();
		final var effectName = readString();
		final var guid = readUnsignedInteger();
		final var modelGuid = readUnsignedInteger();
		final var effectGuid = readUnsignedInteger();

		final var subEntityCount = readAndCastUnsignedInteger();
		final var subEntities = readArray(subEntityCount, this::readSubEntity);

		final var lookName = readString();
		final var pathsRelativeTo = readString();

		return new OverwatchEntity(
			name,
			modelName,
			effectName,
			guid,
			modelGuid,
			effectGuid,
			subEntities,
			lookName,
			pathsRelativeTo
		);
	}

	public OverwatchSubEntity readSubEntity() {
		final var path = readString();
		final var hardpointGuid = readUnsignedLong();
		final var idGuid = readUnsignedLong();
		final var hardpoint = readUnsignedInteger();
		final var id = readUnsignedInteger();
		final var hardpointName = readString();

		return new OverwatchSubEntity(
			path,
			id,
			idGuid,
			hardpoint,
			hardpointGuid,
			hardpointName
		);
	}

}