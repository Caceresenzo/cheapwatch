package cheapwatch.format;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

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
			Short.toUnsignedInt(buffer.getShort()),
			Short.toUnsignedInt(buffer.getShort())
		);
	}

	public long readUnsignedInteger() {
		return LEB128.read(buffer);
	}

	public String readString() {
		final var length = Math.toIntExact(readUnsignedInteger());
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

}