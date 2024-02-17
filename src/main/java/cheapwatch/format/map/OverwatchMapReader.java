package cheapwatch.format.map;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;

import cheapwatch.format.OverwatchReader;
import cheapwatch.format.OverwatchVersion;

public class OverwatchMapReader extends OverwatchReader {

	public static final OverwatchVersion VERSION = new OverwatchVersion(2, 1);

	public OverwatchMapReader(ByteBuffer buffer) {
		super(buffer);
	}

	public OverwatchMap get() {
		final var version = readVersion();
		if (!VERSION.equals(version)) {
			throw new IllegalStateException("invalid version: " + version);
		}

		final var name = readString();
		final var objectCount = readAndCastUnsignedInteger();
		final var propCount = readAndCastUnsignedInteger();
		final var lightCount = readAndCastUnsignedInteger();

		final var objects = new ArrayList<OverwatchObject>(objectCount);
		for (var index = 0; index < objectCount; ++index) {
			objects.add(readObject());
		}

		final var props = new ArrayList<OverwatchProp>(propCount);
		for (var index = 0; index < propCount; ++index) {
			props.add(readProp());
		}

		final var lights = new ArrayList<OverwatchLight>(propCount);
		for (var index = 0; index < lightCount; ++index) {
			lights.add(readLight());
		}

		final var soundCount = readAndCastUnsignedInteger();
		final var sounds = new ArrayList<OverwatchSound>(soundCount);
		for (var index = 0; index < soundCount; ++index) {
			sounds.add(readSound());
		}

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
		final var groups = new ArrayList<OverwatchGroup>(groupCount);
		for (var index = 0; index < groupCount; ++index) {
			groups.add(readGroup());
		}

		return new OverwatchObject(
			modelPath,
			Collections.unmodifiableList(groups)
		);
	}

	public OverwatchGroup readGroup() {
		final var materialPath = readString();

		final var instanceCount = readAndCastUnsignedInteger();
		final var instances = new ArrayList<OverwatchInstance>(instanceCount);
		for (var index = 0; index < instanceCount; ++index) {
			instances.add(readInstance());
		}

		return new OverwatchGroup(
			materialPath,
			Collections.unmodifiableList(instances)
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
		final var modelPath = readString();
		final var materialPath = readString();
		final var position = readVector3();
		final var scale = readVector3();
		final var rotation = readQuaternion();

		return new OverwatchProp(
			modelPath,
			materialPath,
			position,
			scale,
			rotation
		);
	}

	public OverwatchLight readLight() {
		final var position = readVector3();
		final var rotation = readQuaternion();
		final var type = OverwatchLight.Type.valueOf(readAndCastUnsignedInteger());
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
		final var paths = new ArrayList<String>(count);
		for (var index = 0; index < count; ++index) {
			paths.add(readString());
		}

		return new OverwatchSound(
			position,
			Collections.unmodifiableList(paths)
		);
	}

}