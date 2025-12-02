package game.keyInputManager;

import game.GameplayPanel;
import game.entities.Pacman;
import game.keyInputManager.command.InputCommand;
import game.keyInputManager.command.PacmanInputCommand;
import game.utils.KeyHandler;

public class PlayState extends GameState {

    private InputCommand pacmanInputCommand;

    public PlayState(InputCommand pacmanInputCommand) {
        this.pacmanInputCommand = pacmanInputCommand;
    }

    @Override
    public void getInput(KeyHandler k) {
        pacmanInputCommand.execute(k);
    }
}
