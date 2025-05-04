package entity;

public interface CharacterState {
    void enter(Entity e);
    void update(Entity e);
    void exit(Entity e);
}
