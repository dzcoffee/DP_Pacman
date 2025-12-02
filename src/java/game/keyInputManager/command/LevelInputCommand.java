package game.keyInputManager.command;

import game.LevelUIPanel;
import game.utils.KeyHandler;

public class LevelInputCommand implements InputCommand {

    private final LevelUIPanel target;

    public LevelInputCommand(LevelUIPanel levelUIPanel) {
        this.target = levelUIPanel;
    }

    @Override
    public void execute(KeyHandler k) {
        target.input(k);
    }
}
