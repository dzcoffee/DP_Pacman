package game.score;

import game.Observer;
import game.entities.PacGum;
import game.entities.SuperPacGum;
import game.entities.ghosts.Ghost;

import java.util.ArrayList;
import java.util.List;

public class ScoreManager implements Observer, ILevelUpEventSubject {
    private static final int LEVELUP_SCORE = 1000;

    private List<ILevelUpEventObserver> observerCollection;
    private int score = 0;
    private int currentScore = 0;

    public ScoreManager() {
        observerCollection = new ArrayList<>();
    }


    @Override
    public void updatePacGumEaten(PacGum pg) {
        updateScore(10);

    }

    @Override
    public void updateSuperPacGumEaten(SuperPacGum spg) {
        updateScore(100);

    }

    @Override
    public void updateGhostCollision(Ghost gh) {
        updateScore(500);

    }

    private void updateScore(int score) {
        this.score += score;
        this.currentScore += score;
        if(currentScore >= LEVELUP_SCORE) {
            currentScore -= LEVELUP_SCORE;
            notifyLevelUpEvent();
        }
    }

    @Override
    public void registerObserver(ILevelUpEventObserver observer) {
        observerCollection.add(observer);
    }

    @Override
    public void removeObserver(ILevelUpEventObserver observer) {
        observerCollection.remove(observer);
    }

    @Override
    public void notifyLevelUpEvent() {
        observerCollection.forEach(o->o.updateLevelUpEvent());
    }
}
