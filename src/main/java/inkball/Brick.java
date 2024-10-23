package inkball;

import processing.core.PApplet;
import processing.core.PImage;


public class Brick extends GameObject {
    private int maxHits;
    private int currentHits;
    private PImage normalBrick;
    private PImage crackedBrick;
    private Board board;
    private int colour;

    public Brick(int x, int y, PImage normalBrick, PImage crackedBrick, Board board, int colour, int maxHits) {
        super(x * App.CELLSIZE, y * App.CELLSIZE + App.TOPBAR, normalBrick, board);
        this.normalBrick = normalBrick;
        this.crackedBrick = crackedBrick;
        this.board = board;
        this.maxHits = maxHits;
        this.currentHits = 0;
        this.colour = colour;
    }



    public void hit() {
        currentHits++;
        if (currentHits >= maxHits) {
            board.removeBrick(this);
        } else {
            setImage(crackedBrick);
        }
    }



    public boolean canBeDamagedBy(int ballColour){
        return ballColour == 0 || this.colour == ballColour || this.colour == 0;
    }

    public void display(PApplet app) {
        app.image(getImage(), (float)x, (float)y, App.CELLSIZE, App.CELLSIZE);
    }
}
