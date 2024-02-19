package cheapwatch;

import org.joml.Vector2i;
import org.joml.Vector3f;

public class Settings {

	public static final Vector2i WINDOW_RESOLUTION = new Vector2i(1200, 720);

	public static final float ASPECT_RATIO = (float) WINDOW_RESOLUTION.x / WINDOW_RESOLUTION.y;
	public static final int FOV_DEGREE = 50;
	public static final float VERTICAL_FOV = (float) Math.toRadians(FOV_DEGREE);
	public static final float HORIZONTAL_FOV = (float) (2 * Math.atan(Math.tan(VERTICAL_FOV * 0.5) * ASPECT_RATIO));
	public static final float NEAR = 0.1f;
	public static final float FAR = 200000.0f;
	public static final float PITCH_MAX = (float) Math.toRadians(89);

	public static final float PLAYER_SPEED = 0.1f;
	public static final float PLAYER_ROTATION_SPEED = 0.003f;
	public static final Vector3f PLAYER_POSITION = new Vector3f(4.608f, 21f, -85.578f);
	public static final float PLAYER_PITCH = (float) Math.toDegrees(-0.4220027);
	public static final float PLAYER_YAW = (float) Math.toDegrees(-17.468798);
	public static final float MOUSE_SENSITIVITY = 0.002f;

	public static final Vector3f WORLD_UP = new Vector3f(0, 1, 0);

}