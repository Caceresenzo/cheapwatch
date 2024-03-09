package cheapwatch.format.model;

import java.util.List;

public record OverwatchMesh(
	String name,
	long materialId,
	int vertexCount,
	float[] positions,
	//	List<Vector3fc> normals,
	//	List<Vector4fc> tangents,
	List<float[]> uvss,
	//	List<List<Integer>> boneIndexess,
	//	List<List<Float>> boneWeights,
	//	List<List<Vector4fc>> colorss,
	int[] indexes
) {}