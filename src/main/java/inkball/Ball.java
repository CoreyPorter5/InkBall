package inkball;

import processing.core.PApplet;
import processing.core.PVector;


public class Ball extends GameObject implements Updatable {

    private float x_velocity, y_velocity;
    private App app;
    private CollisionHandler collision;
    private float scaleFactor = 1.0f;
    protected float radius = Board.BALLRADIUS;
    private final float originalRadius = Board.BALLRADIUS;
    private float originalXVelocity, originalYVelocity;
    private int colour;


    public Ball(int x, int y, int colour, Board board, App app) {
        super(x * App.CELLSIZE + Board.BALLRADIUS, y * App.CELLSIZE + App.TOPBAR + Board.BALLRADIUS, board.getBallImage(colour), board);
        this.x_velocity = (App.random.nextBoolean() ? 2 : -2);
        this.y_velocity = (App.random.nextBoolean() ? 2 : -2);
        this.app = app;
        this.originalXVelocity = x_velocity;
        this.originalYVelocity = y_velocity;
        this.colour = colour;
        this.collision = new CollisionHandler(app, board);




    }






    public double getX_velocity(){
        return this.x_velocity;
    }

    public double getY_velocity(){
        return this.y_velocity;
    }

    public boolean shouldBeRemoved() {
        for (Hole hole : board.getHoles()) {
            PVector holeCentre = new PVector(hole.getCentreX(), hole.getCentreY());
            PVector ballPos = new PVector(x, y);
            float distance = PVector.dist(ballPos, holeCentre);
            if (distance < 5.5) {
                if(hole.getColor() == this.colour || this.colour == 0 || hole.getColor() == 0){
                    app.increaseScore(hole.getColor());
                }else{
                    app.decreaseScore(hole.getColor());
                    app.requeueBall(this);
                }
                return true;


            }
        }
        return false;
    }



    public void setX_velocity(float x_velocity){
        this.x_velocity = x_velocity;
    }

    public void setY_velocity(float y_velocity){
        this.y_velocity = y_velocity;
    }

    public void updateRadius(float factor){
        this.radius *= factor;
    }

    public void resetRadius(){
        this.radius = originalRadius;
    }

    public void applyAttractionForce(PVector force){
        this.x_velocity += force.x;
        this.y_velocity += force.y;
    }


    public void changeColour(int newColour){
        if(newColour == 0){
            return;
        }
        this.colour = newColour;
        this.setImage(board.getBallImage(newColour));


    }









    @Override
    public void update(){
        board.testForCollisions(this);







        x +=  x_velocity;
        y +=  y_velocity;


        //Handles collisions with boarder

        if (x - radius < 0 || x + radius > App.WIDTH) {
            x_velocity *= -1;
        }
        if (y - radius < App.TOPBAR || y + radius > App.HEIGHT) {
            y_velocity *= -1;
        }








    }

    public int getColour(){
        return colour;
    }




    @Override
    public void display(PApplet app) {
        app.image(getImage(), (x - radius), (y - radius), radius * 2, radius * 2);


    }





}