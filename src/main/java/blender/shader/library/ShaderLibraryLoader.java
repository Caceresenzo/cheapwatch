package blender.shader.library;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.node.ShaderNode;
import blender.shader.node.converter.*;
import blender.shader.node.input.*;
import blender.shader.node.shader.*;
import blender.shader.node.group.GroupInputShaderNode;
import blender.shader.node.group.GroupOutputShaderNode;
import blender.shader.node.group.GroupShaderNode;
import blender.shader.node.group.ShaderNodeGroup;
import blender.shader.node.layout.FrameNode;
import blender.shader.node.layout.RerouteNode;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ShaderLibraryLoader {

    private final JsonNode libraryRoot;

    private final Map<String, BiFunction<ShaderNodeGroup, JsonNode, ShaderNode>> factories = new HashMap<>();

    public ShaderLibraryLoader(JsonNode libraryRoot) {
        this.libraryRoot = libraryRoot;

        if (true) { /* input */
            registerFactory("ShaderNodeFresnel", FresnelShaderNode::new);
            registerFactory("ShaderNodeNewGeometry", GeometryShaderNode::new);
            registerFactory("ShaderNodeLightPath", LightPathShaderNode::new);
            registerFactory("ShaderNodeNormalMap", NormalMapShaderNode::new);
            registerFactory("ShaderNodeTangent", TangentShaderNode::new);
            registerFactory("ShaderNodeUVMap", UVMapShaderNode::new);
            registerFactory("ShaderNodeValue", ValueShaderNode::new);
            registerFactory("ShaderNodeVertexColor", VertexColorShaderNode::new);
        }

        if (true) { /* converter */
            registerFactory("ShaderNodeCombineColor", CombineColorShaderNode::new);
            registerFactory("ShaderNodeCombineXYZ", CombineXYZShaderNode::new);
            registerFactory("ShaderNodeInvert", InvertShaderNode::new);

            registerFactory("ShaderNodeMath", (parent, attributes) -> {
                final var rawOperation = attributes.get("operation").asText();
                final var clamp = attributes.get("use_clamp").asBoolean();

                final var operation = MathShaderNode.Operation.valueOf(rawOperation);

                return new MathShaderNode(operation, clamp);
            });

            registerFactory("ShaderNodeMix", MixShaderNode::new);
            registerFactory("ShaderNodeSeparateColor", SeparateColorShaderNode::new);
            registerFactory("ShaderNodeSeparateXYZ", SeparateXYZShaderNode::new);

            registerFactory("ShaderNodeVectorMath", (parent, attributes) -> {
                final var rawOperation = attributes.get("operation").asText();

                final var operation = VectorMathShaderNode.Operation.valueOf(rawOperation);

                return new VectorMathShaderNode(operation);
            });

            registerFactory("ShaderNodeVectorRotate", VectorRotateShaderNode::new);
            registerFactory("ShaderNodeVectorTransform", VectorTransformShaderNode::new);
        }

        if (true) { /* shader */
            registerFactory("ShaderNodeBsdfDiffuse", BsdfDiffuseShaderNode::new);
            registerFactory("ShaderNodeBsdfAnisotropic", BsdfGlossyShaderNode::new);
            registerFactory("ShaderNodeBsdfPrincipled", BsdfPrincipledShaderNode::new);
            registerFactory("ShaderNodeBsdfTransparent", BsdfTransparentShaderNode::new);
            registerFactory("ShaderNodeEmission", EmissionShaderNode::new);
            registerFactory("ShaderNodeAddShader", ShaderAddShaderNode::new);
            registerFactory("ShaderNodeMixShader", ShaderMixShaderNode::new);
        }

        if (true) { /* group */
            registerFactory("NodeGroupInput", (parent, attributes) -> parent.getInputNode());
            registerFactory("NodeGroupOutput", (parent, attributes) -> parent.getOutputNode());
            registerFactory("ShaderNodeGroup", (parent, attributes) -> {
                final var treeName = attributes.get("node_tree").asText();

                return new GroupShaderNode(treeName);
            });
        }

        if (true) { /* layout */
            registerFactory("NodeFrame", FrameNode::new);
            registerFactory("NodeReroute", RerouteNode::new);
        }
    }

    public ShaderLibrary load() {
        final var library = new ShaderLibrary();

        for (final var shaderJsonNode : libraryRoot) {
            final var group = new ShaderNodeGroup();
            group.setName(shaderJsonNode.get("name").asText());

            addNodes(group, shaderJsonNode.get("nodes"));

            library.add(group);
        }

        library.doLinkage();

        for (final var shaderJsonNode : libraryRoot) {
            final var name = shaderJsonNode.get("name").asText();
            final var group = library.get(name);

            final var nodesRoot = shaderJsonNode.get("nodes");

            addInputsAndOutputs(group, nodesRoot, true);
            addInputsAndOutputs(group, nodesRoot, false);
        }

        for (final var shaderJsonNode : libraryRoot) {
            final var name = shaderJsonNode.get("name").asText();
            final var group = library.get(name);

            addLinks(group, shaderJsonNode.get("links"));
        }

        return library;
    }

    public void addNodes(ShaderNodeGroup group, JsonNode nodesRoot) {
        for (final var nodeJsonNode : nodesRoot) {
            final var node = createNode(
                    nodeJsonNode.get("type").asText(),
                    group,
                    nodeJsonNode.get("attributes")
            );

            node.setName(nodeJsonNode.get("name").asText());

            group.addNode(node);
        }
    }

    public void addInputsAndOutputs(ShaderNodeGroup group, JsonNode nodesRoot, boolean firstPass) {
        for (final var nodeJsonNode : nodesRoot) {
            final var node = group.getNode(nodeJsonNode.get("name").asText());

            for (final var inputJsonNode : nodeJsonNode.get("inputs")) {
                final var index = inputJsonNode.get("index").asInt();
                final var linked = inputJsonNode.get("is_linked").asBoolean();
                final var name = inputJsonNode.get("identifier").asText();
                final var identifier = inputJsonNode.get("identifier").asText();
                final var type = ShaderDataType.valueOf(inputJsonNode.get("type").asText());
                final var defaultValue = type.parse(inputJsonNode.get("default_value"));

                final Supplier<ShaderSocket> builder = () -> new ShaderSocket(
                        name,
                        identifier,
                        type,
                        defaultValue,
                        index
                );

                if (firstPass) {
                    if (node instanceof GroupOutputShaderNode) {
                        group.addOutput(builder.get());
                    } else if (node instanceof RerouteNode reroute) {
                        reroute.addInput(builder.get());
                    }
                } else {
                    if (!linked) {
                        final var socket = node.getInputs().get(index);
                        node.addInputOverrides(socket, defaultValue);
                    }
                }
            }

            if (firstPass) {
                for (final var outputJsonNode : nodeJsonNode.get("outputs")) {
                    final var index = outputJsonNode.get("index").asInt();
                    final var name = outputJsonNode.get("identifier").asText();
                    final var identifier = outputJsonNode.get("identifier").asText();
                    final var type = ShaderDataType.valueOf(outputJsonNode.get("type").asText());

                    final Supplier<ShaderSocket> builder = () -> new ShaderSocket(
                            name,
                            identifier,
                            type,
                            index
                    );

                    if (node instanceof GroupInputShaderNode) {
                        group.addInput(builder.get());
                    } else if (node instanceof RerouteNode reroute) {
                        reroute.addOutput(builder.get());
                    }
                }
            }
        }
    }

    public void addLinks(ShaderNodeGroup group, JsonNode linksRoot) {
        for (final var linkJsonNode : linksRoot) {
            final var fromNodeName = linkJsonNode.get("from_node").asText();
            final var fromSocketIndex = linkJsonNode.get("from_socket").get("index").asInt();
            final var toNodeName = linkJsonNode.get("to_node").asText();
            final var toSocketIndex = linkJsonNode.get("to_socket").get("index").asInt();

            final var fromNode = group.getNode(fromNodeName);
            final var toNode = group.getNode(toNodeName);

            final var fromSocket = fromNode.getOutput(fromSocketIndex);
            final var toSocket = toNode.getInput(toSocketIndex);

            fromNode.addLink(fromSocket, toNode, toSocket);
        }
    }

    public ShaderNode createNode(String type, ShaderNodeGroup parent, JsonNode attributes) {
        final var factory = factories.get(type);

        if (factory == null) {
            throw new IllegalStateException("unknown blender idname: " + type);
        }

        return factory.apply(parent, attributes);
    }

    public ShaderLibraryLoader registerFactory(String type, Supplier<ShaderNode> getter) {
        return registerFactory(type, (parent, attributes) -> getter.get());
    }

    public ShaderLibraryLoader registerFactory(String type, BiFunction<ShaderNodeGroup, JsonNode, ShaderNode> function) {
        factories.put(type, function);
        return this;
    }

}