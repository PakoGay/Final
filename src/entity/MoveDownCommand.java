package entity;

import entity.Player;

public class MoveDownCommand implements Command {
    private final Player player;
    public MoveDownCommand(Player player) { this.player = player; }
    @Override public void execute() { player.moveDown(); }
}