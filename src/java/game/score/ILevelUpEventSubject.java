package game.score;

public interface ILevelUpEventSubject {
    void registerObserver(ILevelUpEventObserver observer);
    void removeObserver(ILevelUpEventObserver observer);
    void notifyLevelUpEvent();
}
