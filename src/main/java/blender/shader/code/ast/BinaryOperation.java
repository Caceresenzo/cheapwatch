package blender.shader.code.ast;

public record BinaryOperation(
	AstNode left,
	String operator,
	AstNode right
) implements AstNode {

	public BinaryOperation(
		AstNode node1,
		String operator1,
		AstNode node2,
		String operator2,
		AstNode node3
	) {
		this(
			new BinaryOperation(
				node1,
				operator1,
				node2
			),
			operator2,
			node3
		);
	}

	public BinaryOperation(
		AstNode node1,
		String operator1,
		AstNode node2,
		String operator2,
		AstNode node3,
		String operator3,
		AstNode node4
	) {
		this(
			new BinaryOperation(
				node1,
				operator1,
				node2,
				operator2,
				node3
			),
			operator3,
			node4
		);
	}

	public BinaryOperation(
		AstNode node1,
		String operator1,
		AstNode node2,
		String operator2,
		AstNode node3,
		String operator3,
		AstNode node4,
		String operator4,
		AstNode node5
	) {
		this(
			new BinaryOperation(
				node1,
				operator1,
				node2,
				operator2,
				node3,
				operator3,
				node4
			),
			operator4,
			node5
		);
	}

	public BinaryOperation(
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
		AstNode node6
	) {
		this(
			new BinaryOperation(
				node1,
				operator1,
				node2,
				operator2,
				node3,
				operator3,
				node4,
				operator4,
				node5
			),
			operator5,
			node6
		);
	}

	public BinaryOperation(
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
		AstNode node7
	) {
		this(
			new BinaryOperation(
				node1,
				operator1,
				node2,
				operator2,
				node3,
				operator3,
				node4,
				operator4,
				node5,
				operator5,
				node6
			),
			operator6,
			node7
		);
	}

	public BinaryOperation(
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
		this(
			new BinaryOperation(
				node1,
				operator1,
				node2,
				operator2,
				node3,
				operator3,
				node4,
				operator4,
				node5,
				operator5,
				node6,
				operator6,
				node7
			),
			operator7,
			node8
		);
	}

}