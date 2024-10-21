package inkball;

import processing.core.PApplet;
import processing.core.PImage;

public class Hole extends GameObject {

    private int colour;



    public Hole(int x, int y, int colour, Board board) {
        super(x * App.CELLSIZE,y  * App.CELLSIZE + App.TOPBAR, board.getHoleImage(colour), board);
        this.colour = colour;



    }



    public int getColor() {
        return colour;
    }

    public float getCentreX() {
        return x + (float) (App.CELLSIZE * 2) / 2;
    }

    public float getCentreY() {
        return y + (float) (App.CELLSIZE * 2) / 2;
    }




    @Override
    public void display(PApplet app) {

        app.image(getImage(), x, y, App.CELLSIZE * 2, App.CELLSIZE * 2);

    }


}