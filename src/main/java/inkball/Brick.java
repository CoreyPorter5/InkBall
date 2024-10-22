package inkball;

import processing.core.PApplet;
import processing.core.PImage;


public class Brick extends GameObject {
    private int hitCount;
    private int maxHits;
    private int colour;
    private PImage normalBrick;
    private PImage crackedBrick;

    public Brick(int x, int y, PImage normalBrick, PImage crackedBrick, Board board, int colour, int maxHits) {
        super(x * App.CELLSIZE, y * App.CELLSIZE + App.TOPBAR, normalBrick, board);
        this.hitCount = 0;
        this.maxHits = maxHits;
        this.colour = colour;
        this.normalBrick = normalBrick;
        this.crackedBrick = crackedBrick;
    }

    public int getColour() {
        return colour;
    }

    public void hit() {
        hitCount++;
        if (hitCount == 1) {
            setImage(crackedBrick);
        } else if (hitCount >= maxHits) {
            board.removeBrick(this);
        }
    }

    public int getHitCount() {
        return hitCount;
    }

    public void display(PApplet app) {
        app.image(getImage(), (float)x, (float)y, App.CELLSIZE, App.CELLSIZE);
    }
}
