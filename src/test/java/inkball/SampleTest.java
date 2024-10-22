package inkball;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class SampleTest{



    @Test
    public void simpleTest() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000); // delay is to give time to initialise stuff before drawing begins
    }



    //This tests the ball update method to ensure that the ball position is updated correctly
    @Test
    public void testBallUpdatePosition() {

        HashMap<String, PImage> sprites = new HashMap<>();
        sprites.put("ball0", new PImage());


        App app = new App();
        app.playerLines = new ArrayList<>();

        Board board = new Board("level1.txt", sprites, app);
        Ball ball = new Ball(100, 100, 0, board, app);

        float initialX = ball.getX();
        float initialY = ball.getY();
        double xVelocity = ball.getX_velocity();
        double yVelocity = ball.getY_velocity();

        ball.update();

        assertEquals(initialX + xVelocity, ball.getX() , "Ball X position did not update correctly");
        assertEquals(initialY + yVelocity, ball.getY(), "Ball Y position did not update correctly");
    }

    @Test
    public void testBallandWallCollision(){
        HashMap<String, PImage> sprites = new HashMap<>();
        sprites.put("wall0", new PImage());
        sprites.put("ball0", new PImage());

        App app = new App();
        app.playerLines = new ArrayList<>();

        Board board = new Board("level2.txt", sprites, app);

        Wall wall = new Wall(5, 5, null, board, 1);
        board.getWalls().add(wall);

        Ball ball = new Ball(5, 4, 0, board, app);
        ball.setY_velocity(2);

        CollisionHandler collision = new CollisionHandler(app, board);
        boolean value = collision.isCollidingWithLineSegment(new PVector(5, 5), new PVector(5, 6), new PVector(5, 4));

        ball.update();
        board.testForCollisions(ball);

        assertTrue(value, "Ball did not collide with wall");
    }

    @Test
    public void testChooseCloserNormal(){
        CollisionHandler collision = new CollisionHandler(new App(), new Board("level2.txt", new HashMap<>(), new App()));
        PVector p1 = new PVector(0, 0);
        PVector p2 = new PVector(10, 0);
        PVector normal1 = new PVector(0, -1);
        PVector normal2 = new PVector(0, 1);
        PVector ballPosition = new PVector(5, -5);

        PVector chosenNormal = collision.chooseCloserNormal(p1, p2, normal1, normal2, ballPosition);

        assertEquals(normal1, chosenNormal, "Incorrect normal chosen");
    }

    @Test
    public void testCollisonHandlerBallWallCollision(){

        HashMap<String, PImage> sprites = new HashMap<>();
        sprites.put("wall0", new PImage());
        sprites.put("ball0", new PImage());

        App app = new App();
        app.playerLines = new ArrayList<>();

        Board board = new Board("level2.txt", sprites, app);

        Wall wall = new Wall(5, 5, sprites.get("wall0"), board, 1);
        board.getWalls().add(wall);

        Ball ball = new Ball(5, 4, 0, board, app);

        ball.x = 5 * App.CELLSIZE + Board.BALLRADIUS;
        ball.y = 4 * App.CELLSIZE + App.TOPBAR + Board.BALLRADIUS;

        ball.setX_velocity(0);
        ball.setY_velocity(2);

        double initialXVelocity = ball.getX_velocity();
        double initialYVelocity = ball.getY_velocity();

        CollisionHandler collision = new CollisionHandler(app, board);
        collision.testForCollisions(ball);

        PVector normal = new PVector(0, -1);

        PVector velocity = new PVector((float)initialXVelocity, (float)initialYVelocity);
        float dotProduct = PVector.dot(velocity, normal);
        PVector expectedVelocity = PVector.sub(velocity, PVector.mult(normal, 2 * dotProduct));

        assertEquals(expectedVelocity.x, ball.getX_velocity(), 0.001, "Ball X velocity did not update correctly");
        assertEquals(expectedVelocity.y, ball.getY_velocity(), 0.001, "Ball Y velocity did not update correctly");
    }

    @Test
    public void testCollisionHandlerBallPlayerLine(){
        HashMap<String, PImage> sprites = new HashMap<>();
        sprites.put("ball0", new PImage());

        App app = new App();
        app.playerLines = new ArrayList<>();

        Board board = new Board("level2.txt", sprites, app);

        Ball ball = new Ball(50, 50, 0, board, app);
        ball.x = 50;
        ball.y = 50;
        ball.setX_velocity(2);
        ball.setY_velocity(0);

        double initialXVelocity = ball.getX_velocity();
        double initialYVelocity = ball.getY_velocity();

        PlayerLine playerLine = new PlayerLine();
        playerLine.addPoint(55, 45);
        playerLine.addPoint(55, 55);
        app.playerLines.add(playerLine);

        CollisionHandler collision = new CollisionHandler(app, board);
        collision.testForCollisions(ball);

        PVector normal = new PVector(-1, 0);

        PVector velocity = new PVector((float)initialXVelocity, (float)initialYVelocity);
        float dotProduct = PVector.dot(velocity, normal);
        PVector expectedVelocity = PVector.sub(velocity, PVector.mult(normal, 2 * dotProduct));

        assertEquals(expectedVelocity.x, ball.getX_velocity(), 0.001, "Ball X velocity did not update correctly");
        assertEquals(expectedVelocity.y, ball.getY_velocity(), 0.001, "Ball Y velocity did not update correctly");

        assertTrue(app.playerLines.isEmpty(), "Player line was not removed");

    }

    @Test
    public void testMouseReleasedRight(){
        App app = new App();

        if (app.playerLines == null) {
            app.playerLines = new ArrayList<>();
        }


        PlayerLine playerLine = new PlayerLine();
        playerLine.addPoint(100, 100);
        playerLine.addPoint(200, 200);
        app.playerLines.add(playerLine);

        MouseEvent mouseEvent = new MouseEvent(app, 0, MouseEvent.RELEASE, 0, 150, 150, PConstants.RIGHT, 1);
        app.mouseReleased(mouseEvent);

        assertEquals(0, app.playerLines.size(), "Player line was not removed");
    }

    @Test
    public void testMouseReleasedLeftwithCTRL(){
        App app = new App();

        if (app.playerLines == null) {
            app.playerLines = new ArrayList<>();
        }


        PlayerLine playerLine = new PlayerLine();
        playerLine.addPoint(100, 100);
        playerLine.addPoint(200, 200);
        app.playerLines.add(playerLine);

        MouseEvent mouseEvent = new MouseEvent(app, 0, MouseEvent.RELEASE, MouseEvent.CTRL, 150, 150, PConstants.LEFT, 1);
        app.mouseReleased(mouseEvent);

        assertEquals(0, app.playerLines.size(), "Player line was not removed");
    }

    @Test
    public void testLineDrawing(){
        App app = new App();
        app.playerLines = new ArrayList<>();

        app.playerLines.add(new PlayerLine());
        app.playerLines.get(0).addPoint(100, 100);
        app.playerLines.get(0).addPoint(200, 200);
        app.draw();
    }





}

// gradle run						Run the program
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
