package game;

public interface ScoreSubject {
    void registerObserver(ScoreObserver observer);
    void removeObserver(ScoreObserver observer);
    void notifyObserverLevelUpEvent();
    void notifyObserverLevelUpEnd();
}
