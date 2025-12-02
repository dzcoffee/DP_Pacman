package game.score;

import game.Observer;
import game.UIPanel;
import game.entities.PacGum;
import game.entities.SuperPacGum;
import game.entities.ghosts.Ghost;
import game.ghostStates.FrightenedMode;
import game.level.ILevelUpEventObserver;
import game.level.ILevelUpEventSubject;

import java.util.ArrayList;
import java.util.List;

public class ScoreManager implements Observer, ILevelUpEventSubject {
    private static final int LEVELUP_SCORE = 1000;

    private List<ILevelUpEventObserver> observerCollection;
    private int score = 0;
    private int currentScore = 0;

    private UIPanel uiPanel;

    public ScoreManager() {
        observerCollection = new ArrayList<>();
    }

    public int getScore() {
        return this.score;
    }


    public void setUIPanel(UIPanel uiPanel) {
        this.uiPanel = uiPanel;
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
        if (gh.getState() instanceof FrightenedMode) {
            updateScore(500);
        }
    }

    private void updateScore(int score) {
        this.score += score;
        this.currentScore += score;
        if (uiPanel != null) {

            uiPanel.updateScore(this.score);
        }
        if (currentScore >= LEVELUP_SCORE) {
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
        observerCollection.forEach(o -> o.updateLevelUpEvent());
    }

    @Override
    public void notifyLevelUpEnd() {

    }
}
