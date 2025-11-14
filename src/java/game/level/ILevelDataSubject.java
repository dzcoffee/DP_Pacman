package game.level;

public interface ILevelDataSubject {
    void registerObserver(ILevelDataObserver o);
    void removeObserver(ILevelDataObserver o);
    void notify(GameLevelData data);
}
