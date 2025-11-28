package game.entities.ghosts;

import game.entities.StaticEntity;
import game.ghostStrategies.PinkyStrategy;

//Classe concrète de Pinky (le fantôme rose)
public class Pinky extends Ghost {
    public Pinky(int xPos, int yPos) {
        super(xPos, yPos, "pinky.png");
        setStrategy(new PinkyStrategy());
    }
    @Override
    public void update() {
        super.update();
        if (state == chaseMode || state == scatterMode) {
            StaticEntity pg = (StaticEntity) collisionDetector.checkCollisionWithDestroyed(this, StaticEntity.class);
            if (pg != null) {
                pg.revive();
            }
        }
    }
}
