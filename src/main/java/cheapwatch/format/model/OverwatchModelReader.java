package cheapwatch.format.model;

import java.nio.ByteBuffer;

import cheapwatch.format.OverwatchReader;
import cheapwatch.format.OverwatchVersion;

public class OverwatchModelReader extends OverwatchReader {

	public static final OverwatchVersion VERSION = new OverwatchVersion(2, 0);

	public OverwatchModelReader(ByteBuffer buffer) {
		super(buffer);
	}

	public OverwatchModel get() {
		final var version = readVersion();
		if (!VERSION.equals(version)) {
			throw new IllegalStateException("invalid version: " + version);
		}

		final var lookFileName = readString();
		final var name = readString();
		final var guid = readUnsignedInteger();

		final var boneCount = readUnsignedShort();
		final var meshCount = readAndCastUnsignedInteger();
		final var hardpointCount = readAndCastUnsignedInteger();

		final var bones = readArray(boneCount, this::readBone);
		final var meshes = readArray(meshCount, this::readMesh);
		final var hardpoints = readArray(hardpointCount, this::readHardpoint);

		return new OverwatchModel(
			lookFileName,
			name,
			guid,
			bones,
			meshes,
			hardpoints
		);
	}

	public OverwatchBone readBone() {
		final var name = readString();
		final var parentIndex = readUnsignedShort();
		final var position = readVector3();
		final var scale = readVector3();
		final var rotationEuler = readVector3();

		return new OverwatchBone(
			name,
			parentIndex,
			position,
			scale,
			rotationEuler
		);
	}

	public OverwatchMesh readMesh() {
		final var name = readString();
		final var materialId = readUnsignedLong();

		final var uvCount = readUnsignedByte();
		final var vertexCount = readAndCastUnsignedInteger();
		final var indexCount = readAndCastUnsignedInteger();
		final var boneCount = readUnsignedByte();

		final var positions = readArray(vertexCount, this::readVector3);
		final var normals = readArray(vertexCount, this::readVector3);
		final var tangents = readArray(vertexCount, this::readVector4);
		final var uvss = readArray2(uvCount, vertexCount, this::readVector2);
		final var boneIndexess = readArray2(boneCount, vertexCount, this::readUnsignedShort);
		final var boneWeightss = readArray2(boneCount, vertexCount, this::readFloat);
		final var colorss = readArray2(2, vertexCount, this::readVector4);
		final var indexess = readArray2(indexCount, 3, this::readAndCastUnsignedInteger);

		return new OverwatchMesh(
			name,
			materialId,
			positions,
			normals,
			tangents,
			uvss,
			boneIndexess,
			boneWeightss,
			colorss,
			indexess
		);
	}

	public OverwatchHardpoint readHardpoint() {
		final var name = readString();
		final var boneName = readString();
		final var position = readVector3();
		final var rotation = readQuaternion();

		return new OverwatchHardpoint(
			name,
			boneName,
			position,
			rotation
		);
	}

}