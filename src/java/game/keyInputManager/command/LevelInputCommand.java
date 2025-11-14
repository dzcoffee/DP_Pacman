package game.keyInputManager.command;

import game.keyInputManager.DummyLevelUIPannel;
import game.utils.KeyHandler;

public class LevelInputCommand implements InputCommand {

    private final DummyLevelUIPannel target;

    public LevelInputCommand(DummyLevelUIPannel levelUIPannel) {
        this.target = levelUIPannel;
    }

    @Override
    public void execute(KeyHandler k) {
        target.input(k);
    }
}
