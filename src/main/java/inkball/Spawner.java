package inkball;

import processing.core.PApplet;
import processing.core.PImage;

public class Spawner extends GameObject {

    public Spawner(int x, int y, PImage image, Board board) {
        super(x * App.CELLSIZE, y * App.CELLSIZE + App.TOPBAR, image, board);

    }


    @Override
    public void display(PApplet app) {
        app.image(getImage(), x, y, App.CELLSIZE, App.CELLSIZE);
    }
}