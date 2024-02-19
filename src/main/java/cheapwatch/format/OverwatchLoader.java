package cheapwatch.format;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import cheapwatch.format.map.OverwatchMap;
import cheapwatch.format.map.OverwatchMapReader;
import cheapwatch.format.model.OverwatchModel;
import cheapwatch.format.model.OverwatchModelReader;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OverwatchLoader {

	private final Path root;

	public OverwatchMap loadMap(Path relative) throws IOException {
		return load(relative, OverwatchMapReader::new);
	}

	public OverwatchModel loadModel(Path relative) throws IOException {
		return load(relative, OverwatchModelReader::new);
	}

	public <T> T load(Path relative, Function<ByteBuffer, OverwatchReader<T>> readerFactory) throws IOException {
		final var path = root.resolve(relative);
		final var size = Files.size(path);

		try (final var fileChannel = FileChannel.open(path)) {
			final var buffer = fileChannel.map(MapMode.READ_ONLY, 0, size);
			buffer.order(ByteOrder.LITTLE_ENDIAN);

			return readerFactory.apply(buffer).get();
		}
	}

	public static OverwatchLoader of(Path root) {
		return new OverwatchLoader(root);
	}

}