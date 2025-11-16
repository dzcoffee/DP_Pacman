package game;

import java.util.ArrayList;
import java.util.List;

public class ScoreManager implements ScoreSubject {
    private List<ScoreObserver> observerCollection;

    public ScoreManager() {
        observerCollection = new ArrayList<>();
    }

    @Override
    public void registerObserver(ScoreObserver observer) {
        observerCollection.add(observer);
    }

    @Override
    public void removeObserver(ScoreObserver observer) {
        observerCollection.remove(observer);
    }


    @Override
    public void notifyObserverLevelUpEvent() {
        observerCollection.forEach(obs -> obs.updateLevelUpEvent());
    }

    @Override
    public void notifyObserverLevelUpEnd() {
        observerCollection.forEach(obs -> obs.updateLevelUpEnd());
    }
}
