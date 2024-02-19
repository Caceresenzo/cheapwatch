package cheapwatch.format.model;

import java.util.List;

import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector3ic;
import org.joml.Vector4fc;

public record OverwatchMesh(
	String name,
	long materialId,
	List<Vector3fc> positions,
	List<Vector3fc> normals,
	List<Vector4fc> tangents,
	List<List<Vector2fc>> uvss,
	List<List<Integer>> boneIndexess,
	List<List<Float>> boneWeights,
	List<List<Vector4fc>> colorss,
	List<Vector3ic> indexes
) {}