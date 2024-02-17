package cheapwatch.util;

import java.io.DataInput;
import java.io.IOException;
import java.nio.ByteBuffer;

import lombok.experimental.UtilityClass;

/**
 * LEB128 reader
 *
 * @author Matthew Khouzam
 * @author https://git.eclipse.org/r/plugins/gitiles/tracecompass.incubator/org.eclipse.tracecompass.incubator/+/refs/changes/80/200680/4/tracetypes/org.eclipse.tracecompass.incubator.golang.core/src/org/eclipse/tracecompass/incubator/internal/golang/core/trace/LEB128Util.java
 */
@UtilityClass
public class LEB128Util {

	/**
	 * Read leb128... we're really doing 64 here
	 *
	 * @param in
	 *            leb128
	 * @return decoded long
	 */
	public static long read(ByteBuffer in) {
		long result = 0;
		long shift = 0;
		byte current = 0;
		do {
			current = in.get();
			result |= ((long) (current & 0x7f)) << shift;
			shift += 7;
		} while ((current & 0x80) != 0);
		return result;
	}

	/**
	 * Read leb128... we're really doing 64 here
	 *
	 * @param in
	 *            leb128
	 * @return decoded long
	 * @throws IOException
	 *             if the file ends
	 */
	public static long read(DataInput in) throws IOException {
		long result = 0;
		long shift = 0;
		byte current = 0;
		do {
			current = in.readByte();
			result |= ((long) (current & 0x7f)) << shift;
			shift += 7;
		} while ((current & 0x80) != 0);
		return result;
	}

}