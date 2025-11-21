package game.level;

import java.util.ArrayList;
import java.util.List;




public class LevelManager implements ILevelDataSubject, ILevelUpEventSubject {
    private final GameLevelData levelData;
    private final List<ILevelDataObserver> observerCollection;
    private final List<ILevelUpEventObserver> levelUpEventObserverCollection;


    private final FrightenAllCommand frightenAllCommand;

    public LevelManager(FrightenAllCommand frightenAllCommand) {
        this.frightenAllCommand = frightenAllCommand;
        this.levelData = new GameLevelData();
        this.observerCollection = new ArrayList<>();
        this.levelUpEventObserverCollection = new ArrayList<>();
    }

    public int getPacmanLife(){
        return levelData.getPacmanLife();
    }

    public void increasePacmanLife(){
        levelData.increasePacmanLife();
        notify(levelData);
    }

    public void decreasePacmanLife(){
        levelData.decreasePacmanLife();
        notify(levelData);


    }

    public void increasePacmanSpeed(){
        levelData.increasePacmanSpeed();
        notify(levelData);
    }

    public void decreasePacmanSpeed(){
        levelData.decreasePacmanSpeed();
        notify(levelData);
    }

    public void increaseGhostSpeed(){
        levelData.increaseGhostSpeed();
        notify(levelData);
    }

    public void decreaseGhostSpeed(){
        levelData.decreaseGhostSpeed();
        notify(levelData);
    }

    public void frightenAll(){
        frightenAllCommand.execute();
        notifyLevelUpEnd();

    }

    @Override
    public void registerObserver(ILevelDataObserver o) {
        observerCollection.add(o);
    }

    @Override
    public void removeObserver(ILevelDataObserver o) {
        observerCollection.remove(o);
    }

    @Override
    public void notify(GameLevelData data) {
        observerCollection.forEach(o -> o.updateLevelData(data));
        notifyLevelUpEnd();
    }

    @Override
    public void registerObserver(ILevelUpEventObserver observer) {
        levelUpEventObserverCollection.add(observer);
    }

    @Override
    public void removeObserver(ILevelUpEventObserver observer) {
        levelUpEventObserverCollection.remove(observer);
    }

    @Override
    public void notifyLevelUpEvent() {

    }

    @Override
    public void notifyLevelUpEnd() {
        levelUpEventObserverCollection.forEach(o -> o.updateLevelUpEnd());
    }
}
