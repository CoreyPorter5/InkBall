package inkball;

import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerDrawnLineTest {

    @Test
    public void testLineDrawing() {
        //Tests that a player line is added correctly and all the points are added correctly
        App app = new App();
        PApplet.runSketch(new String[]{"App"}, app);
        app.playerLines = new ArrayList<>();

        app.playerLines.add(new PlayerLine());
        app.playerLines.get(0).addPoint(100, 100);
        app.playerLines.get(0).addPoint(200, 200);

    }

    @Test
    public void testCollisionHandlerBallPlayerLine() {
        //Tests that the balls velocity is updated correctly when it collides with a player line
        HashMap<String, PImage> sprites = new HashMap<>();
        sprites.put("ball0", new PImage(24, 24));

        App app = new App();
        PApplet.runSketch(new String[]{"App"}, app);
        app.playerLines = new ArrayList<>();

        Board board = new Board("level2.txt", sprites, app);

        Ball ball = new Ball(50, 50, 0, board, app);
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

        PVector velocity = new PVector((float) initialXVelocity, (float) initialYVelocity);
        float dotProduct = PVector.dot(velocity, normal);
        PVector expectedVelocity = PVector.sub(velocity, PVector.mult(normal, 2 * dotProduct));

        assertEquals(expectedVelocity.x, ball.getX_velocity(), 0.001, "Ball X velocity did not update correctly");
        assertEquals(expectedVelocity.y, ball.getY_velocity(), 0.001, "Ball Y velocity did not update correctly");

        assertTrue(app.playerLines.isEmpty(), "Player line was not removed");
    }

    @Test
    public void testBallAndPlayerLineCollision() {
        //Tests that the balls velocity is updated correctly when it collides with a player line

        App app = new App();
        app.playerLines = new ArrayList<>();
        App.random = new java.util.Random();


        HashMap<String, PImage> sprites = new HashMap<>();
        sprites.put("ball0", new PImage(24, 24));


        Board board = new Board("level1.txt", sprites, app);

        Ball ball = new Ball(48, 50, 0, board, app);
        ball.setX_velocity(2);
        ball.setY_velocity(0);

        double initialXVelocity = ball.getX_velocity();
        double initialYVelocity = ball.getY_velocity();

        PlayerLine playerLine = new PlayerLine();
        playerLine.addPoint(55, 45);
        playerLine.addPoint(55, 55);
        app.playerLines.add(playerLine);

        CollisionHandler collision = new CollisionHandler(app, board);

        boolean collisionOccurred = false;
        for (int i = 0; i < 10; i++) {

            ball.update();

            collision.testForCollisions(ball);

            if (ball.getX_velocity() != initialXVelocity || ball.getY_velocity() != initialYVelocity) {
                collisionOccurred = true;
                break;
            }
        }

        assertTrue(collisionOccurred, "Collision did not occur as expected");

        PVector normal = new PVector(-1, 0);
        PVector velocity = new PVector((float) initialXVelocity, (float) initialYVelocity);
        float dotProduct = PVector.dot(velocity, normal);
        PVector expectedVelocity = PVector.sub(velocity, PVector.mult(normal, 2 * dotProduct));

        assertEquals(expectedVelocity.x, ball.getX_velocity(), 0.001, "Ball X velocity did not update correctly");
        assertEquals(expectedVelocity.y, ball.getY_velocity(), 0.001, "Ball Y velocity did not update correctly");

    }


}