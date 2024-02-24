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
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import cheapwatch.util.LEB128;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class OverwatchReader<T> {

	private final ByteBuffer buffer;

	public OverwatchVersion readVersion() {
		return new OverwatchVersion(
			readUnsignedShort(),
			readUnsignedShort()
		);
	}

	public abstract T get();

	public int readUnsignedByte() {
		return Byte.toUnsignedInt(buffer.get());
	}

	public int readUnsignedShort() {
		return Short.toUnsignedInt(buffer.getShort());
	}

	public long readUnsignedInteger() {
		return Integer.toUnsignedLong(buffer.getInt());
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

	public long readUnsignedLong() {
		return buffer.getLong();
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

	public Vector3ic readVector3i() {
		return new Vector3i(
			buffer.getInt(),
			buffer.getInt(),
			buffer.getInt()
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

	public <E> List<E> readArray(int length, Supplier<E> readFunction) {
		final var elements = new ArrayList<E>(length);

		for (var index = 0; index < length; ++index) {
			elements.add(readFunction.get());
		}

		return Collections.unmodifiableList(elements);
	}

	public <E> List<List<E>> readArray2(int length1, int length2, Supplier<E> readFunction) {
		final var lists = new ArrayList<List<E>>(length1);

		for (var index = 0; index < length1; ++index) {
			lists.add(readArray(length2, readFunction));
		}

		return Collections.unmodifiableList(lists);
	}

	public ByteList readBytes(int length) {
		final var array = new byte[length];
		buffer.get(array);
		
		return ByteArrayList.wrap(array);
	}

}