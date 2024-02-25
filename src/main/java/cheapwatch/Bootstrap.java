package cheapwatch;

import cheapwatch.state.PlayState;

public class Bootstrap {

	public static void main(String[] args) {
		Game.run(new PlayState());
	}

}