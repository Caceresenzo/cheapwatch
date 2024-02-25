package cheapwatch.format.map;

import java.nio.ByteBuffer;
import java.util.Collections;

import cheapwatch.format.OverwatchReader;
import cheapwatch.format.OverwatchVersion;

public class OverwatchMapReader extends OverwatchReader<OverwatchMap> {

	public static final OverwatchVersion VERSION = new OverwatchVersion(2, 1);

	public OverwatchMapReader(ByteBuffer buffer) {
		super(buffer);
	}

	@Override
	public OverwatchMap get() {
		final var version = readVersion();
		if (!VERSION.equals(version)) {
			throw new IllegalStateException("invalid version: " + version);
		}

		final var name = readString();
		final var objectCount = readAndCastUnsignedInteger();
		final var propCount = readAndCastUnsignedInteger();
		final var lightCount = readAndCastUnsignedInteger();

		final var objects = readArray(objectCount, this::readObject);
		final var props = readArray(propCount, this::readProp);
		final var lights = readArray(lightCount, this::readLight);

		final var soundCount = readAndCastUnsignedInteger();
		final var sounds = readArray(soundCount, this::readSound);

		return new OverwatchMap(
			name,
			objects,
			props,
			lights,
			sounds
		);
	}

	public OverwatchObject readObject() {
		final var modelPath = readString();

		final var groupCount = readAndCastUnsignedInteger();
		final var groups = readArray(groupCount, this::readGroup);

		return new OverwatchObject(
			modelPath,
			groups
		);
	}

	public OverwatchGroup readGroup() {
		final var materialPath = readString();

		final var instanceCount = readAndCastUnsignedInteger();
		final var instances = readArray(instanceCount, this::readInstance);

		return new OverwatchGroup(
			materialPath,
			instances
		);
	}

	public OverwatchInstance readInstance() {
		final var position = readVector3();
		final var scale = readVector3();
		final var rotation = readQuaternion();

		return new OverwatchInstance(
			position,
			scale,
			rotation
		);
	}

	public OverwatchProp readProp() {
		final var entityPath = readString();
		final var materialPath = readString();
		final var position = readVector3();
		final var scale = readVector3();
		final var rotation = readQuaternion();

		return new OverwatchProp(
			entityPath,
			materialPath,
			position,
			scale,
			rotation
		);
	}

	public OverwatchLight readLight() {
		final var position = readVector3();
		final var rotation = readQuaternion();
		final var type = OverwatchLightType.valueOf(readAndCastUnsignedInteger());
		final var fov = readFloat();
		final var color = readVector3();
		final var intensity = readFloat();

		readLong();
		readLong();

		return new OverwatchLight(
			position,
			rotation,
			type,
			fov,
			color,
			intensity
		);
	}

	public OverwatchSound readSound() {
		final var position = readVector3();

		final var count = readAndCastUnsignedInteger();
		final var paths = readArray(count, this::readString);

		return new OverwatchSound(
			position,
			Collections.unmodifiableList(paths)
		);
	}

}