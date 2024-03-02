package cheapwatch;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.graph.ShaderCodeGenerator;
import blender.shader.graph.SimpleShaderNodeGraph;
import blender.shader.group.ShaderNodeGroup;
import blender.shader.node.*;
import cheapwatch.state.PlayState;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joml.Vector3f;

import java.util.HashMap;

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
        final var root = (ObjectNode) objectMapper.readTree(Bootstrap.class.getResourceAsStream("/owm_basic.json"));
//        final var root = (ObjectNode) objectMapper.readTree(Bootstrap.class.getResourceAsStream("/owm_blend_1_b.json"));

        final var group = new ShaderNodeGroup();
        group.setName(root.get("name").asText());

        int index = 0;
        for (final var jsonNode : root.get("inputs")) {
            final var blenderSocketIdName = jsonNode.get("bl_socket_idname").asText();
            final var name = jsonNode.get("name").asText();
            final var defaultValue = jsonNode.get("default_value");

            final var type = fromBlenderSocketIdName(blenderSocketIdName);

            group.addInput(new ShaderSocket(
                    name,
                    type,
                    type.parse(defaultValue),
                    index++
            ));
        }

        index = 0;
        for (final var jsonNode : root.get("outputs")) {
            final var blenderSocketIdName = jsonNode.get("bl_socket_idname").asText();
            final var name = jsonNode.get("name").asText();

            final var type = fromBlenderSocketIdName(blenderSocketIdName);

            group.addOutput(new ShaderSocket<>(
                    name,
                    type,
                    null,
                    index++
            ));
        }

        final var shaderNodes = new HashMap<String, ShaderNode>();
        for (final var jsonNode : root.get("nodes")) {
            final var blenderIdName = jsonNode.get("bl_idname");
            final var attributes = jsonNode.get("attributes");

            final var shaderNode = switch (blenderIdName.asText()) {
                case "ShaderNodeCombineXYZ" -> new CombineXYZShaderNode();
                case "ShaderNodeSeparateXYZ" -> new SeparateXYZShaderNode();
                case "ShaderNodeMix" -> new MixShaderNode();
                case "ShaderNodeNormalMap" -> new NormalMapShaderNode();
                case "ShaderNodeBsdfPrincipled" -> new BsdfPrincipledShaderNode();
                case "NodeGroupInput" -> group.getInputNode();
                case "NodeGroupOutput" -> group.getOutputNode();
                case "ShaderNodeValue" -> new ValueShaderNode(0.0f);
                case "ShaderNodeMath" -> {
                    final var rawOperation = attributes.get("operation").asText();
                    final var clamp = attributes.get("use_clamp").asBoolean();

                    final var operation = MathShaderNode.Operation.valueOf(rawOperation);

                    yield new MathShaderNode(operation, clamp);
                }
                case "ShaderNodeVectorMath" -> {
                    final var rawOperation = attributes.get("operation").asText();

                    final var operation = MathVectorShaderNode.Operation.valueOf(rawOperation);

                    yield new MathVectorShaderNode(operation);
                }
                case "ShaderNodeGroup" -> {
                    final var treeName = attributes.get("node_tree").asText();

                    final var subGroup = new ShaderNodeGroup();
                    subGroup.setName(treeName);

                    yield subGroup;
                }
                default -> throw new IllegalStateException("unknown blender idname: " + blenderIdName);
            };

            final var name = jsonNode.get("name").asText();
            System.out.println(name);
            shaderNode.setName(name);

            if (!(shaderNode instanceof ShaderNodeGroup)) {
                final var inputs = jsonNode.get("inputs");
                for (final var input : inputs) {
                    final var inputName = input.get("name").asText();
                    final var inputIndex = input.get("index").asInt();
                    final var defaultValue = input.get("default_value");

                    final var socket = shaderNode.getInput(inputIndex, inputName).orElseThrow();

                    System.out.println(input);
//                    System.out.println(shaderNode.getInputs());
                    System.out.println(defaultValue);
                    System.out.println(defaultValue.getClass());
                    shaderNode.addInputOverrides(socket, defaultValue);
                }
            }

            shaderNodes.put(name, shaderNode);
        }

        for (final var jsonNode : root.get("nodes")) {
            final var name = jsonNode.get("name").asText();
            final var shaderNode = shaderNodes.get(name);

            final var links = jsonNode.get("links");
            for (final var link : links) {
                System.out.println(link);
                final var fromIndex = link.get("fs").asInt();
                final var toName = link.get("tn").asText();
                final var toIndex = link.get("ts").asInt();

                final var toNode = shaderNodes.get(toName);
                shaderNode.addLink(fromIndex, toNode, toIndex);
            }
        }

        final var codeGenerator = new ShaderCodeGenerator(group);
        System.out.println(codeGenerator.generate());
    }

    public static void xmain(String[] args) {
		Game.run(new PlayState());
    }

}