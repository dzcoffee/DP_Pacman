package game.entities;

import java.awt.*;

public class TeleportZone extends StaticEntity{
    private TeleportZone partner;
    private Color color;

    public TeleportZone(int xPos, int yPos) {
        super(32, xPos, yPos);
    }

    @Override
    public void render(Graphics2D g) {
        if (color != null) {
            g.setColor(this.color);
            g.drawOval(xPos, yPos, 32, 32);
        } else {
            g.setColor(new Color(255, 183, 174));
            g.drawOval(xPos, yPos, 32, 32);   
        }
    }

    public void setPartner(TeleportZone partner) {
        this.partner = partner;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public TeleportZone getPartner() {
        return partner;
    }

}
