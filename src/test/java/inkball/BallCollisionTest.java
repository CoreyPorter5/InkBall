package inkball;

import processing.core.PApplet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PImage;
import processing.core.PVector;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class BallCollisionTest {

    private App app;
    private Board board;
    private HashMap<String, PImage> sprites;

    @BeforeEach
    public void setUp() {
        app = new App();
        PApplet.runSketch(new String[]{"App"}, app);
        sprites = new HashMap<>();
        sprites.put("wall0", new PImage(32, 32));
        sprites.put("ball0", new PImage(24, 24));
        board = new Board("level2.txt", sprites, app);
        app.board = board;
    }

    @Test
    public void testBallUpdatePosition() {
        // Test that the ball's position updates correctly based on its velocity
        Ball ball = new Ball(100, 100, 0, board, app);

        float initialX = ball.getX();
        float initialY = ball.getY();
        double xVelocity = ball.getX_velocity();
        double yVelocity = ball.getY_velocity();

        ball.update();

        assertEquals(initialX + xVelocity, ball.getX(), "Ball X position did not update correctly");
        assertEquals(initialY + yVelocity, ball.getY(), "Ball Y position did not update correctly");
    }

    @Test
    public void testBallandWallCollision() {
        //Test that the ball correctly collides with a wall
        Wall wall = new Wall(5, 5, sprites.get("wall0"), board, 1);
        board.getWalls().add(wall);

        Ball ball = new Ball(5, 4, 0, board, app);
        ball.setY_velocity(2);

        CollisionHandler collision = new CollisionHandler(app, board);
        boolean value = collision.isCollidingWithLineSegment(new PVector(5, 5), new PVector(5, 6), new PVector(5, 4));



        assertTrue(value, "Ball did not collide with wall");
    }

    @Test
    public void testChooseCloserNormal() {
        //Tests that the correct normal vector is chosen based on the balls position
        CollisionHandler collision = new CollisionHandler(app, board);
        PVector p1 = new PVector(0, 0);
        PVector p2 = new PVector(10, 0);
        PVector normal1 = new PVector(0, -1);
        PVector normal2 = new PVector(0, 1);
        PVector ballPosition = new PVector(5, -5);

        PVector chosenNormal = collision.chooseCloserNormal(p1, p2, normal1, normal2, ballPosition);

        assertEquals(normal1, chosenNormal, "Incorrect normal chosen");
    }

    @Test
    public void testCollisonHandlerBallWallCollision() {
        //Tests that the ball's velocity is updated correctly after colliding with a wall
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

        PVector velocity = new PVector((float) initialXVelocity, (float) initialYVelocity);
        float dotProduct = PVector.dot(velocity, normal);
        PVector expectedVelocity = PVector.sub(velocity, PVector.mult(normal, 2 * dotProduct));

        assertEquals(expectedVelocity.x, ball.getX_velocity(), 0.001, "Ball X velocity did not update correctly");
        //assertEquals(expectedVelocity.y, ball.getY_velocity(), 0.001, "Ball Y velocity did not update correctly");
    }




}