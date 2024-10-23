package inkball;

import processing.core.PApplet;
import processing.core.PImage;

public class Wall extends GameObject {

    private int colour;


    public Wall(int x, int y, PImage image, Board board, int colour) {
        super(x * App.CELLSIZE, y * App.CELLSIZE + App.TOPBAR, image, board);
        this.colour = colour;


    }

    public int getColour() {
        return colour;
    }





    @Override
    public void display(PApplet app) {
        app.image(getImage(), x , y, App.CELLSIZE, App.CELLSIZE);
    }

}