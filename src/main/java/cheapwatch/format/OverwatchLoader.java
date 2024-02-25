package cheapwatch.format;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import javax.imageio.ImageIO;

import cheapwatch.format.entity.OverwatchEntity;
import cheapwatch.format.entity.OverwatchEntityReader;
import cheapwatch.format.map.OverwatchMap;
import cheapwatch.format.map.OverwatchMapReader;
import cheapwatch.format.material.OverwatchMaterial;
import cheapwatch.format.material.OverwatchMaterialReader;
import cheapwatch.format.material.OverwatchModelLook;
import cheapwatch.format.material.OverwatchModelLookReader;
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

	public OverwatchModelLook loadModelLook(Path relative) throws IOException {
		return load(relative, OverwatchModelLookReader::new);
	}

	public OverwatchMaterial loadMaterial(Path relative) throws IOException {
		return load(relative, OverwatchMaterialReader::new);
	}

	public OverwatchEntity loadEntity(Path relative) throws IOException {
		return load(relative, OverwatchEntityReader::new);
	}

	public BufferedImage loadTexture(Path relative) throws IOException {
		final var path = resolve(relative);

		return ImageIO.read(path.toFile());
	}

	public List<String> listEntities() throws IOException {
		try (final var stream = Files.list(root.resolve("Entities"))) {
			return stream
				.filter(Files::isDirectory)
				.map(Path::getFileName)
				.map(Path::toString)
				.toList();
		}
	}

	public Path resolve(Path relative) {
		return root.resolve(relative);
	}

	public <T> T load(Path relative, Function<ByteBuffer, OverwatchReader<T>> readerFactory) throws IOException {
		final var path = resolve(relative);
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