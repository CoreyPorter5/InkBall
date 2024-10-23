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

public class PlayerDrawnLineTest {

    @Test
    public void testLineDrawing(){
        App app = new App();
        app.playerLines = new ArrayList<>();

        app.playerLines.add(new PlayerLine());
        app.playerLines.get(0).addPoint(100, 100);
        app.playerLines.get(0).addPoint(200, 200);
        app.draw();
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
}
