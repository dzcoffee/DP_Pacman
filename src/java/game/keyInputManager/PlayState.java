package game.keyInputManager;

import game.keyInputManager.command.InputCommand;
import game.utils.KeyHandler;

public class PlayState extends GameState {

    public PlayState(InputCommand pacmanInputCommand) {
        inputCommand = pacmanInputCommand;
    }

    @Override
    public void getInput(KeyHandler k) {
        inputCommand.execute(k);
    }
}
