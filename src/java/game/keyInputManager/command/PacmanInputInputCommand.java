package game.keyInputManager.command;

import game.entities.Pacman;
import game.utils.KeyHandler;

public class PacmanInputInputCommand implements InputCommand {

    private final Pacman target;

    public PacmanInputInputCommand(Pacman pacman) {
        this.target = pacman;
    }

    @Override
    public void execute(KeyHandler k) {
        target.input(k);
    }
}
