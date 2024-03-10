package blender.shader.code.ast;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BinaryOperationTest {

	@Test
	void chain3() {
		final var builder = new StringBuilder();
		final var visitor = new AstVisitor(builder);

		final var chain = BinaryOperation.chain(
			new Identifier("a"),
			"*",
			new Identifier("b"),
			"+",
			new Identifier("c")
		);
		
		visitor.visit(chain);

		assertEquals("a * b + c", builder.toString());
	}
	
	@Test
	void chain8() {
		final var builder = new StringBuilder();
		final var visitor = new AstVisitor(builder);
		
		final var chain = BinaryOperation.chain(
			new Identifier("mxp_tangent"),
			"*",
			new MemberAccess(
				new Identifier("v"),
				"x"
				),
			"*",
			new Identifier("mxp_scale"),
			"+",
			new Identifier("B"),
			"*",
			new MemberAccess(
				new Identifier("v"),
				"y"
				),
			"*",
			new Identifier("mxp_scale"),
			"+",
			new Identifier("mxp_normal"),
			"*",
			new MemberAccess(
				new Identifier("v"),
				"z"
				)
			);
		
		visitor.visit(chain);
		
		assertEquals("mxp_tangent * v.x * mxp_scale + B * v.y * mxp_scale + mxp_normal * v.z", builder.toString());
	}

}