package inkball;

import processing.core.PApplet;
import processing.core.PImage;

public class Wall extends GameObject {


    public Wall(int x, int y, PImage image, Board board) {
        super(x * App.CELLSIZE, y * App.CELLSIZE + App.TOPBAR, image, board);


    }

    @Override
    public void display(PApplet app) {
        app.image(getImage(), (float)x , (float)y, App.CELLSIZE, App.CELLSIZE);
    }

}