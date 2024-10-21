package inkball;

import processing.core.PApplet;
import processing.core.PImage;


public abstract class GameObject {
    protected float x, y;
    protected PImage image;
    protected Board board;


    public GameObject(float x, float y, PImage image, Board board){
        this.x = x;
        this.y = y;
        this.image = image;
        this.board = board;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public PImage getImage(){
        return image;
    }

    public void setImage(PImage image){
        this.image = image;
    }

    public abstract void display(PApplet app);
}
