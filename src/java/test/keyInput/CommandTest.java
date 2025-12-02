package keyInput;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import game.LevelUIPanel;
import game.entities.Pacman;
import game.keyInputManager.command.LevelInputCommand;
import game.keyInputManager.command.PacmanInputCommand;
import game.utils.KeyHandler;
import org.junit.Test;
import org.mockito.Mockito;

public class CommandTest {

    @Test
    public void testCommand_levelInputCommandCall() {

        LevelUIPanel mockPanel = Mockito.mock(LevelUIPanel.class);
        KeyHandler mockKeyHandler = Mockito.mock(KeyHandler.class);

        LevelInputCommand command = new LevelInputCommand(mockPanel);
        command.execute(mockKeyHandler);

        verify(mockPanel, times(1)).input(mockKeyHandler);
    }

    @Test
    public void testCommand_PacmanInputCommandCall() {
        // given
        Pacman mockPacman = Mockito.mock(Pacman.class);
        KeyHandler mockKeyHandler = Mockito.mock(KeyHandler.class);

        PacmanInputCommand command = new PacmanInputCommand(mockPacman);
        command.execute(mockKeyHandler);

        verify(mockPacman, times(1)).input(mockKeyHandler);
    }
}
