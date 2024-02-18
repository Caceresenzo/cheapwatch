package cheapwatch.format;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import cheapwatch.util.LEB128;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OverwatchReader {

	private final ByteBuffer buffer;

	public OverwatchVersion readVersion() {
		return new OverwatchVersion(
			readUnsignedShort(),
			readUnsignedShort()
		);
	}

	public int readUnsignedShort() {
		return Short.toUnsignedInt(buffer.getShort());
	}

	public long readUnsignedLong() {
		return buffer.getLong();
	}

	public int readUnsignedByte() {
		return Byte.toUnsignedInt(buffer.get());
	}

	public long readUnsignedInteger() {
		return Integer.toUnsignedLong(buffer.getInt());
	}

	public String readString() {
		final var length = Math.toIntExact(LEB128.read(buffer));
		final var data = new byte[length];
		buffer.get(data);

		return new String(data, StandardCharsets.US_ASCII);
	}

	public Vector2fc readVector2() {
		return new Vector2f(
			buffer.getFloat(),
			buffer.getFloat()
		);
	}

	public Vector3fc readVector3() {
		return new Vector3f(
			buffer.getFloat(),
			buffer.getFloat(),
			buffer.getFloat()
		);
	}

	public Vector4fc readVector4() {
		return new Vector4f(
			buffer.getFloat(),
			buffer.getFloat(),
			buffer.getFloat(),
			buffer.getFloat()
		);
	}

	public Quaternionfc readQuaternion() {
		return new Quaternionf(
			buffer.getFloat(),
			buffer.getFloat(),
			buffer.getFloat(),
			buffer.getFloat()
		);
	}

	public int readAndCastUnsignedInteger() {
		final var value = Integer.toUnsignedLong(buffer.getInt());

		return Math.toIntExact(value);
	}

	public float readFloat() {
		return buffer.getFloat();
	}

	public float readLong() {
		return buffer.getLong();
	}

	public <T> List<T> readArray(int length, Supplier<T> readFunction) {
		final var elements = new ArrayList<T>(length);

		for (var index = 0; index < length; ++index) {
			elements.add(readFunction.get());
		}

		return Collections.unmodifiableList(elements);
	}

	public <T> List<List<T>> readArray2(int length1, int length2, Supplier<T> readFunction) {
		final var lists = new ArrayList<List<T>>(length1);

		for (var index = 0; index < length1; ++index) {
			lists.add(readArray(length2, readFunction));
		}

		return Collections.unmodifiableList(lists);
	}

}