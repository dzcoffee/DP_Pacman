package game.entities;

import java.awt.*;

//Classe abtraite pour décrite une entité
public abstract class Entity {
    protected int size;
    protected int xPos;
    protected int yPos;

    protected boolean destroyed = false;

    public Entity(int size, int xPos, int yPos) {
        this.size = size;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void update() {}

    public void render(Graphics2D g) {}

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void revive() {
        destroyed = false;
    }

    public int getSize() {
        return size;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public abstract Rectangle getHitbox();
}
