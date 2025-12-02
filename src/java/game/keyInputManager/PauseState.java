package game.keyInputManager;

import game.keyInputManager.command.InputCommand;
import game.utils.KeyHandler;

public class PauseState extends GameState{

    public PauseState(InputCommand levelInputCommand) {
        inputCommand = levelInputCommand;
    }

    @Override
    public void getInput(KeyHandler k) {
        inputCommand.execute(k);
    }
}
