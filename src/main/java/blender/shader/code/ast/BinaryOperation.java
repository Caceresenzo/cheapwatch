package blender.shader.code.ast;

public record BinaryOperation(
	String operator,
	AstNode left,
	AstNode right
) implements AstNode {

	public static BinaryOperation chain(AstNode left, String operator1, AstNode middle, String operator2, AstNode right) {
		return new BinaryOperation(
			operator2,
			new BinaryOperation(
				operator1,
				left,
				middle
			),
			right
		);
	}

	public static BinaryOperation chain(
		AstNode node1,
		String operator1,
		AstNode node2,
		String operator2,
		AstNode node3,
		String operator3,
		AstNode node4,
		String operator4,
		AstNode node5,
		String operator5,
		AstNode node6,
		String operator6,
		AstNode node7,
		String operator7,
		AstNode node8
	) {
		return new BinaryOperation(
			operator7,
			new BinaryOperation(
				operator6,
				new BinaryOperation(
					operator5,
					new BinaryOperation(
						operator4,
						new BinaryOperation(
							operator3,
							new BinaryOperation(
								operator2,
								new BinaryOperation(
									operator1,
									node1,
									node2
								),
								node3
							),
							node4
						),
						node5
					),
					node6
				),
				node7
			),
			node8
		);
	}

}