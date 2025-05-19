package entity;

import entity.Player;

public class MoveUpCommand implements Command {
    private final Player player;
    public MoveUpCommand(Player player) { this.player = player; }
    @Override public void execute() { player.moveUp(); }
}