package game.level;

public class GameLevelData {

    private static final float[] speedData = {0.5f,1,2};

    private int pacmanSpeedIndex;
    private int ghostSpeedIndex;
    private int pacmanLife;

    public GameLevelData() {
        init();
    }

    public void init(){
        //초기 수치
        pacmanSpeedIndex = 1;
        ghostSpeedIndex = 1;
        pacmanLife = 1;
    }

    public int getPacmanLife() {
        return pacmanLife;
    }

    public void increasePacmanLife() {
        this.pacmanLife++;
    }

    public void decreasePacmanLife() {
        this.pacmanLife--;
    }

    public float getGhostSpeed() {
        return speedData[ghostSpeedIndex];
    }

    public void increaseGhostSpeed() {
        this.ghostSpeedIndex++;
    }

    public void decreaseGhostSpeed() {
        this.ghostSpeedIndex--;
    }

    public float getPacmanSpeed() {
        return speedData[pacmanSpeedIndex];
    }

    public void increasePacmanSpeed() {
        this.pacmanSpeedIndex++;
    }

    public void decreasePacmanSpeed() {
        this.pacmanSpeedIndex--;
    }

}
