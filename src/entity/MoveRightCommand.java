package entity;

import entity.Player;

public class MoveRightCommand implements Command {
    private final Player player;
    public MoveRightCommand(Player player) { this.player = player; }
    @Override public void execute() { player.moveRight(); }
}
