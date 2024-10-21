package inkball;

import java.util.ArrayList;
import java.util.List;

import static processing.core.PApplet.dist;
import processing.core.PVector;

public class PlayerLine {

    public final int HumanError = 6;
    private List<PVector> points;

    public PlayerLine() {
        points = new ArrayList<>();
    }

    public void addPoint(int x, int y) {
        points.add(new PVector(x, y));
    }

    public List<PVector> getPoints() {
        return points;
    }

    public boolean mouseCollideWithLine(int x, int y, int x1, int y1, int x2, int y2) {
        float d = dist(x, y, x1, y1) + dist(x, y, x2, y2);
        float l = dist(x1, y1, x2, y2);
        return d <= l + HumanError;
    }
}
