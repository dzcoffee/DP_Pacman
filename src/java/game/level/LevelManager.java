package game.level;


import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private final GameLevelData levelData;

    public LevelManager() {
        this.levelData = new GameLevelData();
    }

    public void increasePacmanLife(){
        levelData.increasePacmanLife();
    }

    public void decreasePacmanLife(){
        levelData.decreasePacmanLife();
    }

    public void increasePacmanSpeed(){
        levelData.increasePacmanSpeed();
    }
    public void decreasePacmanSpeed(){
        levelData.decreasePacmanSpeed();
    }
    public void increaseGhostSpeed(){
        levelData.increaseGhostSpeed();
    }
    public void decreaseGhostSpeed(){
        levelData.decreaseGhostSpeed();
    }

}
