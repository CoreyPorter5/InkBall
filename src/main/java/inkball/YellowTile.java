package inkball;

import processing.core.PApplet;
import processing.core.PImage;

public class YellowTile implements Updatable {
    private int x, y;
    private PImage image;
    private App app;
    private int lastUpdateTime;

    public YellowTile(int x, int y, PImage image, App app) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.app = app;
        this.lastUpdateTime = app.millis();
    }

    @Override
    public void update() {
        int currentTime = app.millis();
        if (currentTime - lastUpdateTime >= 67) {
            moveClockwise();
            lastUpdateTime = currentTime;
        }
    }

    protected int getX(){
        return this.x;
    }

    protected int getY(){
        return this.y;
    }

    protected void moveClockwise() {
        if (y == App.TOPBAR && x < app.width - App.CELLSIZE) {
            x += App.CELLSIZE;
        } else if (x == app.width - App.CELLSIZE && y < app.height - App.CELLSIZE) {
            y += App.CELLSIZE;
        } else if (y == app.height - App.CELLSIZE && x > 0) {
            x -= App.CELLSIZE;
        } else if (x == 0 && y > App.TOPBAR) {
            y -= App.CELLSIZE;
        }
    }

    public void display() {
        app.image(image, x, y, App.CELLSIZE, App.CELLSIZE);
    }
}