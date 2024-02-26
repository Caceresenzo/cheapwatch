package cheapwatch;

import blender.shader.ShaderDataType;
import blender.shader.ShaderPort;
import blender.shader.graph.ShaderCodeGenerator;
import blender.shader.graph.ShaderNodeGraph;
import blender.shader.graph.SimpleShaderNodeGraph;
import blender.shader.group.ShaderNodeGroup;
import blender.shader.node.*;
import cheapwatch.state.PlayState;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Iterator;

public class Bootstrap {

    public static void main(String[] args) throws Exception {
        final var objectMapper = new ObjectMapper();
        final var root = (ObjectNode) objectMapper.readTree(Bootstrap.class.getResourceAsStream("/owm_unpack_blue_channel.json"));
//        final var root = (ObjectNode) objectMapper.readTree(Bootstrap.class.getResourceAsStream("/owm_unpack_pbr_v2.json"));
//        final var root = (ObjectNode) objectMapper.readTree(Bootstrap.class.getResourceAsStream("/owm_basic.json"));
//        final var root = (ObjectNode) objectMapper.readTree(Bootstrap.class.getResourceAsStream("/owm_blend_1_b.json"));

        final var group = new ShaderNodeGroup();
        group.setName(root.get("name").asText());

        int index = 0;
        for (final var jsonNode : root.get("inputs")) {
            final var name = jsonNode.get("name").asText();
            final var rawType = jsonNode.get("type").asText();
            final var defaultValue = jsonNode.get("default_value");

            final var type = ShaderDataType.valueOf(rawType);

            group.addInput(new ShaderPort(
                    name,
                    type,
                    type.parseDefaultValue(defaultValue),
                    index++
            ));
        }

        index = 0;
        for (final var jsonNode : root.get("outputs")) {
            final var name = jsonNode.get("name").asText();
            final var rawType = jsonNode.get("type");

            // TODO use bl_socket_idname to resolve type
            final var type = ShaderDataType.VALUE;

            group.addOutput(new ShaderPort(
                    name,
                    type,
                    null,
                    index++
            ));
        }

        System.out.println(group.getOutputs());

        final var shaderNodes = new HashMap<String, ShaderNode>();
        for (final var jsonNode : root.get("nodes")) {
            final var blenderIdName = jsonNode.get("bl_idname");
            final var attributes = jsonNode.get("attributes");

            final var shaderNode = switch (blenderIdName.asText()) {
                case "ShaderNodeCombineXYZ" -> new CombineXYZShaderNode();
                case "ShaderNodeSeparateXYZ" -> new SeparateXYZShaderNode();
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
            shaderNode.setName(name);

            if (!(shaderNode instanceof ShaderNodeGroup)) {
                final var inputs = jsonNode.get("inputs");
                for (final var input : inputs) {
                    final var portIndex = input.get("index").asInt();
                    final var defaultValue = input.get("default_value");

                    shaderNode.addInputOverrides(portIndex, defaultValue);
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

        final var first = new VectorShaderNode(new Vector3f(15, 2, 2001));
        first.setName("first");

        final var separate = new SeparateXYZShaderNode();
        separate.setName("separate");

        final var two = new ValueShaderNode(2.0f);
        two.setName("two");

        final var multiply = new MathShaderNode(MathShaderNode.Operation.MULTIPLY, false);
        multiply.setName("multiply");

        final var add = new MathShaderNode(MathShaderNode.Operation.ADD, false);
        add.setName("add");

        final var combine = new CombineXYZShaderNode();
        combine.setName("combine");

        first.addLink(
                "Vector",
                separate,
                "Vector"
        );

        separate.addLink(
                "X",
                multiply,
                "A"
        );

        separate.addLink(
                "Y",
                add,
                "A"
        );

        two.addLink(
                "Value",
                multiply,
                "B"
        );

        two.addLink(
                "Value",
                add,
                "B"
        );

        multiply.addLink(
                "Value",
                combine,
                "X"
        );

        add.addLink(
                "Value",
                combine,
                "Y"
        );

        separate.addLink(
                "Z",
                combine,
                "Z"
        );

        final var nodeGraph = new SimpleShaderNodeGraph()
                .addNode(first)
                .addNode(separate)
                .addNode(two)
                .addNode(multiply)
                .addNode(combine);

        final var codeGenerator = new ShaderCodeGenerator(nodeGraph);
        System.out.println(codeGenerator.generate());
    }

}