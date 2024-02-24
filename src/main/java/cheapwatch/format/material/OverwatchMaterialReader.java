package cheapwatch.format.material;

import java.nio.ByteBuffer;

import cheapwatch.format.OverwatchReader;
import cheapwatch.format.OverwatchVersion;

public class OverwatchMaterialReader extends OverwatchReader<OverwatchMaterial> {

	public static final OverwatchVersion VERSION = new OverwatchVersion(3, 0);

	public OverwatchMaterialReader(ByteBuffer buffer) {
		super(buffer);
	}

	@Override
	public OverwatchMaterial get() {
		final var version = readVersion();
		if (!VERSION.equals(version)) {
			throw new IllegalStateException("invalid version: " + version);
		}

		final var type = OverwatchMaterialType.valueOf(readAndCastUnsignedInteger());
		if (!OverwatchMaterialType.MATERIAL.equals(type)) {
			throw new IllegalStateException("invalid type: " + type);
		}

		final var textureCount = Math.toIntExact(readUnsignedLong());
		final var staticCount = Math.toIntExact(readUnsignedLong());
		final var shaderId = readUnsignedInteger();

		final var textures = readArray(textureCount, this::readTexture);
		final var statics = readArray(staticCount, this::readStatic);

		return new OverwatchMaterial(
			shaderId,
			textures,
			statics
		);

	}

	public OverwatchTexture readTexture() {
		final var path = readString();
		final var id = readUnsignedInteger();

		return new OverwatchTexture(
			id,
			path
		);
	}

	public OverwatchStatic readStatic() {
		final var id = readUnsignedInteger();
		final var dataSize = readAndCastUnsignedInteger();
		final var data = readBytes(dataSize);

		return new OverwatchStatic(
			id,
			data
		);
	}

}