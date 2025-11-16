package game.keyInputManager.command;

import game.GameplayPanel;
import game.entities.Pacman;
import game.utils.KeyHandler;

public class PacmanInputCommand implements InputCommand {

    private final GameplayPanel target;

    public PacmanInputCommand(GameplayPanel gameplayPanel) {
        this.target = gameplayPanel;
    }

    @Override
    public void execute(KeyHandler k) {
        target.input(k);
    }
}
