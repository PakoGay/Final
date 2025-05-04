// src/main/PathFinder.java
package entity;

import main.GameplayScreen;
import java.awt.Point;
import java.util.*;


public class PathFinder {
    private final GameplayScreen gp;
    private final int maxCol, maxRow;

    public PathFinder(GameplayScreen gp) {
        this.gp     = gp;
        this.maxCol = gp.maxWorldCol;
        this.maxRow = gp.maxWorldRow;
    }

    private boolean isWalkable(int col, int row) {
        if (col < 0 || row < 0 || col >= maxCol || row >= maxRow) return false;
        int tileNum = gp.tileM.mapTileNum[col][row];
        return !gp.tileM.tile[tileNum].collision;
    }

    public List<Point> findPath(int startCol, int startRow, int goalCol, int goalRow) {
        boolean[][] visited = new boolean[maxCol][maxRow];
        Node[][]    nodes   = new Node[maxCol][maxRow];
        Queue<Node> queue   = new ArrayDeque<>();

        Node start = new Node(startCol, startRow, null);
        visited[startCol][startRow] = true;
        nodes[startCol][startRow]   = start;
        queue.add(start);

        Node goalNode = null;
        int[] dc = { 0,  0, -1, +1};
        int[] dr = {-1, +1,  0,  0};

        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            if (cur.col == goalCol && cur.row == goalRow) {
                goalNode = cur;
                break;
            }
            for (int i = 0; i < 4; i++) {
                int nc = cur.col + dc[i];
                int nr = cur.row + dr[i];
                if (nc < 0 || nr < 0 || nc >= maxCol || nr >= maxRow) continue;
                if (visited[nc][nr]) continue;
                if (!isWalkable(nc, nr))  continue;
                Node nxt = new Node(nc, nr, cur);
                visited[nc][nr] = true;
                nodes[nc][nr]   = nxt;
                queue.add(nxt);
            }
        }

        if (goalNode == null) return Collections.emptyList();

        List<Point> path = new ArrayList<>();
        for (Node n = goalNode; n != null; n = n.parent) {
            path.add(new Point(n.col, n.row));
        }
        Collections.reverse(path);
        return path;
    }

    private static class Node {
        final int col, row;
        final Node parent;
        Node(int c, int r, Node p) { col = c; row = r; parent = p; }
    }
}
