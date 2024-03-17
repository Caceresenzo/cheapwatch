package cheapwatch;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeGenerator;
import blender.shader.code.ShaderVariableAllocator;
import blender.shader.code.ast.AstVisitor;
import blender.shader.graph.SimpleShaderNodeGraph;
import blender.shader.library.ShaderLibrary;
import blender.shader.library.ShaderLibraryLoader;
import blender.shader.node.converter.MathShaderNode;
import blender.shader.node.group.GroupShaderNode;
import blender.shader.node.group.ShaderNodeGroup;
import blender.shader.node.input.ValueShaderNode;
import cheapwatch.state.PlayState;

public class Bootstrap {

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
	public static class TypeInfoMixIn {}

	public static void xxmain(String[] args) throws Exception {
		final var library = new ShaderLibrary();

		{
			final var twoNode = new ValueShaderNode(2f)
				.setName("two");

			final var multiply = new MathShaderNode(MathShaderNode.Operation.MULTIPLY, false)
				.setName("multiply");

			final var group = new ShaderNodeGroup()
				.setName("double")
				.addInput(new ShaderSocket("Input", ShaderDataType.VALUE, 0))
				.addOutput(new ShaderSocket("Output", ShaderDataType.VALUE, 0))
				.addNode(twoNode)
				.addNode(multiply);

			twoNode.addLink(twoNode.getOutput(0), multiply, multiply.getInput(0));
			group.getInputNode().addLink(group.getInputs().get(0), multiply, multiply.getInput(1));
			multiply.addLink(multiply.getOutput(0), group.getOutputNode(), group.getOutputs().get(0));

			library.add(group);
		}

		final var graph = new SimpleShaderNodeGraph();
		{
			final var five = new ValueShaderNode(5f)
				.setName("five");

			final var callDouble = new GroupShaderNode("double")
				.link(library)
				.setName("call_double");

			final var sin = new MathShaderNode(MathShaderNode.Operation.SINE, false)
				.setName("sin");

			graph
				.addNode(five)
				.addNode(callDouble)
				.addNode(sin);

			five.addLink(five.getOutput(0), callDouble, callDouble.getInput(0));
			callDouble.addLink(callDouble.getOutput(0), sin, sin.getInput(0));
		}

		final var variableAllocator = new ShaderVariableAllocator();
		final var codeGenerator = new ShaderCodeGenerator(graph, variableAllocator, new ArrayList<>(), null);
		final var statements = codeGenerator.generate();

		final var builder = new StringBuilder();
		final var visitor = new AstVisitor(builder);
		statements.forEach(visitor::visit);
		System.out.println(builder.toString());
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
		//		final var group = library.get("OWM: Unpack Blue Channel");
		final var group = library.get("OWM: Basic");

		final var variableAllocator = new ShaderVariableAllocator();
		final var codeGenerator = new ShaderCodeGenerator(group, variableAllocator, new ArrayList<>(), null);
		final var statements = codeGenerator.generate();

		//		objectMapper.addMixInAnnotations(AstElement.class, TypeInfoMixIn.class);
		//
		//		System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(statements));

		final var builder = new StringBuilder();
		final var visitor = new AstVisitor(builder);
		statements.forEach(visitor::visit);
		System.out.println(builder.toString());

		//            break;
		//        }
	}

	public static void xmain(String[] args) {
		Game.run(new PlayState());
	}

}