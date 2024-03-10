package blender.shader.library;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.JsonNode;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.node.ShaderNode;
import blender.shader.node.converter.CombineColorShaderNode;
import blender.shader.node.converter.CombineXYZShaderNode;
import blender.shader.node.converter.InvertShaderNode;
import blender.shader.node.converter.MathShaderNode;
import blender.shader.node.converter.MixShaderNode;
import blender.shader.node.converter.SeparateColorShaderNode;
import blender.shader.node.converter.SeparateXYZShaderNode;
import blender.shader.node.converter.VectorMathShaderNode;
import blender.shader.node.converter.VectorRotateShaderNode;
import blender.shader.node.converter.VectorTransformShaderNode;
import blender.shader.node.group.GroupInputShaderNode;
import blender.shader.node.group.GroupOutputShaderNode;
import blender.shader.node.group.GroupShaderNode;
import blender.shader.node.group.ShaderNodeGroup;
import blender.shader.node.input.FresnelShaderNode;
import blender.shader.node.input.GeometryShaderNode;
import blender.shader.node.input.LightPathShaderNode;
import blender.shader.node.input.NormalMapShaderNode;
import blender.shader.node.input.TangentShaderNode;
import blender.shader.node.input.UVMapShaderNode;
import blender.shader.node.input.ValueShaderNode;
import blender.shader.node.input.VertexColorShaderNode;
import blender.shader.node.layout.FrameNode;
import blender.shader.node.layout.RerouteNode;
import blender.shader.node.shader.BsdfDiffuseShaderNode;
import blender.shader.node.shader.BsdfGlossyShaderNode;
import blender.shader.node.shader.BsdfPrincipledShaderNode;
import blender.shader.node.shader.BsdfTransparentShaderNode;
import blender.shader.node.shader.EmissionShaderNode;
import blender.shader.node.shader.ShaderAddShaderNode;
import blender.shader.node.shader.ShaderMixShaderNode;

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
			registerComplexFactory("ShaderNodeValue", (parent, jsonNode) -> {
				final var defaultValue = jsonNode.get("outputs").get(0).get("default_value").floatValue();

				return new ValueShaderNode(defaultValue);
			});
			registerFactory("ShaderNodeVertexColor", VertexColorShaderNode::new);
		}

		if (true) { /* converter */
			registerFactory("ShaderNodeCombineColor", CombineColorShaderNode::new);
			registerFactory("ShaderNodeCombineXYZ", CombineXYZShaderNode::new);
			registerFactory("ShaderNodeInvert", InvertShaderNode::new);

			registerSimpleFactory("ShaderNodeMath", (parent, attributes) -> {
				final var rawOperation = attributes.get("operation").asText();
				final var clamp = attributes.get("use_clamp").asBoolean();

				final var operation = MathShaderNode.Operation.valueOf(rawOperation);

				return new MathShaderNode(operation, clamp);
			});

			registerSimpleFactory("ShaderNodeMix", (parent, attributes) -> {
				final var clampFactor = attributes.get("clamp_factor").asBoolean();
				final var clampResult = attributes.get("clamp_result").asBoolean();
				final var factorMode = MixShaderNode.FactorMode.valueOf(attributes.get("factor_mode").asText());
				final var blendType = MixShaderNode.BlendType.valueOf(attributes.get("blend_type").asText());
				final var dataType = ShaderDataType.valueOf(attributes.get("data_type").asText());

				return new MixShaderNode(clampFactor, clampResult, factorMode, blendType, dataType);
			});
			registerFactory("ShaderNodeSeparateColor", SeparateColorShaderNode::new);
			registerFactory("ShaderNodeSeparateXYZ", SeparateXYZShaderNode::new);

			registerSimpleFactory("ShaderNodeVectorMath", (parent, attributes) -> {
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
			registerSimpleFactory("NodeGroupInput", (parent, attributes) -> parent.getInputNode());
			registerSimpleFactory("NodeGroupOutput", (parent, attributes) -> parent.getOutputNode());
			registerSimpleFactory("ShaderNodeGroup", (parent, attributes) -> {
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
		}

		//        for (final var name : library.names()) {
		//            final var group = library.get(name);
		//
		//            System.out.println(name);
		//            System.out.println(group.getInputs().size());
		//        }

		for (final var shaderJsonNode : libraryRoot) {
			final var name = shaderJsonNode.get("name").asText();
			final var group = library.get(name);

			final var nodesRoot = shaderJsonNode.get("nodes");

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
				nodeJsonNode,
				group
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

	public ShaderNode createNode(JsonNode jsonNode, ShaderNodeGroup parent) {
		final var type = jsonNode.get("type").asText();
		final var factory = factories.get(type);

		if (factory == null) {
			throw new IllegalStateException("unknown blender idname: " + type);
		}

		return factory.apply(parent, jsonNode);
	}

	public ShaderLibraryLoader registerFactory(String type, Supplier<ShaderNode> getter) {
		return registerSimpleFactory(type, (parent, jsonNode) -> getter.get());
	}

	public ShaderLibraryLoader registerSimpleFactory(String type, BiFunction<ShaderNodeGroup, JsonNode, ShaderNode> function) {
		final BiFunction<ShaderNodeGroup, JsonNode, ShaderNode> simplified = (parent, jsonNode) -> {
			final var attributes = jsonNode.get("attributes");

			return function.apply(parent, attributes);
		};

		factories.put(type, simplified);
		return this;
	}

	public ShaderLibraryLoader registerComplexFactory(String type, BiFunction<ShaderNodeGroup, JsonNode, ShaderNode> function) {
		factories.put(type, function);
		return this;
	}

}