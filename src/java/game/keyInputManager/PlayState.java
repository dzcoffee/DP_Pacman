package game.keyInputManager;

import game.GameplayPanel;
import game.entities.Pacman;
import game.keyInputManager.command.PacmanInputCommand;
import game.utils.KeyHandler;

public class PlayState extends GameState {

    private PacmanInputCommand pacmanInputCommand;

    public PlayState(GameplayPanel gameplayPanel) {
        pacmanInputCommand = new PacmanInputCommand(gameplayPanel);
    }

    @Override
    public void getInput(KeyHandler k) {
        pacmanInputCommand.execute(k);
    }
}
