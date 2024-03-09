package cheapwatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import blender.shader.ShaderDataType;
import blender.shader.code.ShaderCodeGenerator;
import blender.shader.code.ShaderVariableAllocator;
import blender.shader.library.ShaderLibraryLoader;
import cheapwatch.state.PlayState;

public class Bootstrap {

	public static ShaderDataType<?> fromBlenderSocketIdName(String idName) {
		return switch (idName) {
			case "NodeSocketColor" -> ShaderDataType.VECTOR;
			case "NodeSocketFloat" -> ShaderDataType.VALUE;
			case "NodeSocketFloatFactor" -> ShaderDataType.VALUE;
			case "NodeSocketShader" -> ShaderDataType.VALUE; // TODO ??
			default -> throw new IllegalStateException("unknown socket idname: " + idName);
		};
	}

	public static void main(String[] args) throws Exception {
		final var objectMapper = new ObjectMapper();
		//        final var root = (ObjectNode) objectMapper.readTree(Bootstrap.class.getResourceAsStream("/owm_unpack_blue_channel.json"));
		//        final var root = (ObjectNode) objectMapper.readTree(Bootstrap.class.getResourceAsStream("/owm_unpack_pbr_v2.json"));
		//        final var root = (ObjectNode) objectMapper.readTree(Bootstrap.class.getResourceAsStream("/owm_basic.json"));
		//        final var root = (ObjectNode) objectMapper.readTree(Bootstrap.class.getResourceAsStream("/owm_blend_1_b.json"));
		final var root = (ObjectNode) objectMapper.readTree(Bootstrap.class.getResourceAsStream("/library-v2.json"));

		final var loader = new ShaderLibraryLoader(root);
		final var library = loader.load();

		//        for (final var name : library.names()) {
		//            System.out.println(name);
		final var group = library.get("OWM: Unpack Blue Channel");

		final var variableAllocator = new ShaderVariableAllocator();
		final var codeGenerator = new ShaderCodeGenerator(group, variableAllocator);
		System.out.println(codeGenerator.generate());

		//            break;
		//        }
	}

	public static void xmain(String[] args) {
		Game.run(new PlayState());
	}

}