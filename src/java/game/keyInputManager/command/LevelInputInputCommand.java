package game.keyInputManager.command;

import game.keyInputManager.DummyLevelUIPannel;
import game.utils.KeyHandler;

public class LevelInputInputCommand implements InputCommand {

    private final DummyLevelUIPannel target;

    public LevelInputInputCommand(DummyLevelUIPannel levelUIPannel) {
        this.target = levelUIPannel;
    }

    @Override
    public void execute(KeyHandler k) {
        target.input(k);
    }
}
