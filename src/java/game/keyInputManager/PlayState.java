package game.keyInputManager;

import game.entities.Pacman;
import game.keyInputManager.command.PacmanInputCommand;
import game.utils.KeyHandler;

public class PlayState extends GameState {

    private PacmanInputCommand pacmanInputCommand;

    public PlayState(Pacman pacman) {
        pacmanInputCommand = new PacmanInputCommand(pacman);
    }

    @Override
    public void getInput(KeyHandler k) {
        pacmanInputCommand.execute(k);
    }
}
