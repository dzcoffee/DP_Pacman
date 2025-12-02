package game.keyInputManager;

import game.keyInputManager.command.InputCommand;
import game.keyInputManager.command.NullInputCommand;
import game.utils.KeyHandler;

public abstract class GameState {
    protected InputCommand inputCommand = new NullInputCommand();
    public void getInput(KeyHandler k) {};
}
