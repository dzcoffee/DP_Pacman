package game.keyInputManager;

import game.keyInputManager.command.LevelInputCommand;
import game.utils.KeyHandler;

public class PauseState extends GameState{
    private LevelInputCommand levelInputCommand;

    public PauseState(DummyLevelUIPannel levelUIPannel) {
        levelInputCommand = new LevelInputCommand(levelUIPannel);
    }

    @Override
    public void getInput(KeyHandler k) {
        levelInputCommand.execute(k);
    }
}
