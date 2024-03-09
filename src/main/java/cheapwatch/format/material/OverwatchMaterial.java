package cheapwatch.format.material;

import java.util.List;

public record OverwatchMaterial(
	long shaderId,
	List<OverwatchTexture> textures,
	List<OverwatchStatic> statics
) {

	public OverwatchTexture getTexture(long id) {
		for (final var texture : textures) {
			if (texture.id() == id) {
				return texture;
			}
		}

		return null;
	}

}