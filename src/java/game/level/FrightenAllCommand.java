package game.level;

import game.Game;


public class FrightenAllCommand implements LevelUpCommand {
    private final Game game;
    public FrightenAllCommand(Game game) { this.game = game; }

    @Override
    public void execute() {
        game.eatGhostAll();
    }
}
