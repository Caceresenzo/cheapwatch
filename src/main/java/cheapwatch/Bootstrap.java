package cheapwatch;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

import cheapwatch.format.map.OverwatchMapReader;

public class Bootstrap {

	public static void main(String[] args) throws IOException {
		final var path = Paths.get("Games/Extracts/Maps/Dorado/2C3/Evening - Escort.owmap");
		final var size = Files.size(path);
		try (final var fileChannel = FileChannel.open(path)) {
			final var buffer = fileChannel.map(MapMode.READ_ONLY, 0, size);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			
			final var objectMapper = new ObjectMapper().writerWithDefaultPrettyPrinter();
			final var map = new OverwatchMapReader(buffer).get();
			System.out.println(objectMapper.writeValueAsString(map));
			System.out.println(objectMapper.writeValueAsString(map).contains("owentity"));
		}

		System.out.println("Hello World");
	}

}