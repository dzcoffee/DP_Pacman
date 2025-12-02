package game.keyInputManager;

import game.LevelUIPanel;
import game.keyInputManager.command.InputCommand;
import game.keyInputManager.command.LevelInputCommand;
import game.utils.KeyHandler;

public class PauseState extends GameState{
    private InputCommand levelInputCommand;

    public PauseState(InputCommand levelInputCommand) {
        this.levelInputCommand = levelInputCommand;
    }

    @Override
    public void getInput(KeyHandler k) {
        levelInputCommand.execute(k);
    }
}
