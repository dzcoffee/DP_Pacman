package game.keyInputManager.command;

import game.entities.Pacman;
import game.utils.KeyHandler;

public class PacmanInputCommand implements Command {

    private final Pacman target;

    public PacmanInputCommand(Pacman pacman) {
        this.target = pacman;
    }

    @Override
    public void execute(KeyHandler k) {
        target.input(k);
    }
}
