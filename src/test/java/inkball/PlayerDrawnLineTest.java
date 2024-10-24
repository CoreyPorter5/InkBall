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
        App app = new App();
        PApplet.runSketch(new String[]{"App"}, app);
        app.playerLines = new ArrayList<>();

        app.playerLines.add(new PlayerLine());
        app.playerLines.get(0).addPoint(100, 100);
        app.playerLines.get(0).addPoint(200, 200);
        //app.draw();
    }

    @Test
    public void testCollisionHandlerBallPlayerLine() {
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
        // Initialize the App instance
        App app = new App();
        app.playerLines = new ArrayList<>();
        App.random = new java.util.Random(); // Ensure random is initialized

        // Initialize sprites
        HashMap<String, PImage> sprites = new HashMap<>();
        sprites.put("ball0", new PImage(24, 24)); // Initialize with dimensions

        // Initialize Board with an empty layout for simplicity
        char[][] testLayout = new char[18][18];
        Board board = new Board("level1.txt", sprites, app);

        // Initialize the ball near the player line
        Ball ball = new Ball(48, 50, 0, board, app);
        ball.setX_velocity(2); // Ball moves right towards increasing x
        ball.setY_velocity(0);

        double initialXVelocity = ball.getX_velocity();
        double initialYVelocity = ball.getY_velocity();

        // Create and add a PlayerLine that the ball will collide with
        PlayerLine playerLine = new PlayerLine();
        playerLine.addPoint(55, 45);
        playerLine.addPoint(55, 55);
        app.playerLines.add(playerLine);

        // Create a CollisionHandler
        CollisionHandler collision = new CollisionHandler(app, board);

        // Run a loop to update the state of the ball without drawing
        boolean collisionOccurred = false;
        for (int i = 0; i < 10; i++) {
            // Update the ball's position
            ball.update();

            // Test for collisions
            collision.testForCollisions(ball);

            // Check if collision occurred by comparing velocities
            if (ball.getX_velocity() != initialXVelocity || ball.getY_velocity() != initialYVelocity) {
                collisionOccurred = true;
                break;
            }
        }

        // Assert that a collision occurred
        assertTrue(collisionOccurred, "Collision did not occur as expected");

        PVector normal = new PVector(-1, 0);
        PVector velocity = new PVector((float) initialXVelocity, (float) initialYVelocity);
        float dotProduct = PVector.dot(velocity, normal);
        PVector expectedVelocity = PVector.sub(velocity, PVector.mult(normal, 2 * dotProduct));

        assertEquals(expectedVelocity.x, ball.getX_velocity(), 0.001, "Ball X velocity did not update correctly");
        assertEquals(expectedVelocity.y, ball.getY_velocity(), 0.001, "Ball Y velocity did not update correctly");

    }


}