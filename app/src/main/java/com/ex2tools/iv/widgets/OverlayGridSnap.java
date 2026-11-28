package com.ex2tools.iv.widgets;

import android.graphics.Rect;

import java.util.List;

final class OverlayGridSnap {
    private OverlayGridSnap() {}

    static Rect find(Rect drop, List<Rect> occupied, int screenWidth, int screenHeight, int step) {
        int width = drop.width();
        int height = drop.height();
        int maxX = Math.max(0, screenWidth - width);
        int maxY = Math.max(0, screenHeight - height);
        int x = coordinate(drop.left, maxX, step);
        int y = coordinate(drop.top, maxY, step);
        Rect preferred = new Rect(x, y, x + width, y + height);
        if (isFree(preferred, occupied)) return preferred;

        Rect best = preferred;
        Rect candidate = new Rect();
        long bestDistance = Long.MAX_VALUE;
        for (int row = 0; row <= maxY; row += step) {
            long dy = (long) row - drop.top;
            for (int column = 0; column <= maxX; column += step) {
                long dx = (long) column - drop.left;
                long distance = dx * dx + dy * dy;
                if (distance >= bestDistance) continue;
                candidate.set(column, row, column + width, row + height);
                if (isFree(candidate, occupied)) {
                    bestDistance = distance;
                    best = new Rect(candidate);
                }
            }
        }
        return best;
    }

    private static int coordinate(int requested, int limit, int step) {
        int nearest = Math.round(Math.max(0, requested) / (float) step);
        return Math.min(nearest, limit / step) * step;
    }

    private static boolean isFree(Rect candidate, List<Rect> occupied) {
        for (Rect bounds : occupied) {
            if (Rect.intersects(candidate, bounds)) return false;
        }
        return true;
    }
}
