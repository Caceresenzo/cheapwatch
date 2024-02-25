package cheapwatch.format;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OverwatchTextureMapping {

	public static final Set<String> COLLISION_MATERIALS;

	static {
		final var collisionMaterials = new HashSet<String>();
		collisionMaterials.add("0000000034A0");
		collisionMaterials.add("000000002DD4");
		collisionMaterials.add("000000002D77");
		collisionMaterials.add("0000000034A2");
		collisionMaterials.add("000000002D77");
		collisionMaterials.add("000000000796");
		collisionMaterials.add("000000000005");
		collisionMaterials.add("000000000794");
		collisionMaterials.add("0000000048EF");
		collisionMaterials.add("0000000034A3");
		collisionMaterials.add("000000000797");
		collisionMaterials.add("0000000007A2");
		collisionMaterials.add("0000000007A0");
		collisionMaterials.add("0000000007C0");
		collisionMaterials.add("0000000007A1");

		COLLISION_MATERIALS = Collections.unmodifiableSet(collisionMaterials);
	}

}