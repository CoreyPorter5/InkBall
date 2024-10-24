package inkball;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    private App app;

    @BeforeEach
    public void setUp() {
        app = new App();
        PApplet.runSketch(new String[]{"App"}, app);
        app.noLoop();
        app.delay(1000);
    }
    @Test
    public void testDrawWhenCountdownZeroSpawnsBallAndResetsCountdown() {
        app.paused = false;
        app.timer.setTime(10); // time > 0
        app.countdown = 0; // countdown <= 0

        int initialBallCount = app.board.getBalls().size();

        app.draw();

        // Check that countdown has been reset
        assertEquals(app.spawnInterval * App.FPS, app.countdown);

        // Check that a new ball has been spawned
        int newBallCount = app.board.getBalls().size();
        assertEquals(initialBallCount + 1, newBallCount);
    }

    @Test
    public void testDrawWhenCountdownGreaterThanZeroCountdownDecrements() {
        app.paused = false;
        app.timer.setTime(10); // time > 0
        app.countdown = 5; // countdown > 0

        float initialCountdown = app.countdown;

        app.draw();

        // Check that countdown has decreased by 1
        assertEquals(initialCountdown - 1, app.countdown);
    }

    @Test
    public void testDrawWhenPaused() {
        app.paused = true;
        app.timer.setTime(10); // time > 0

        float initialCountdown = app.countdown;

        app.draw();

        // Ensure countdown has not changed
        assertEquals(initialCountdown, app.countdown);
    }

    @Test
    public void testDrawWhenTimerZero() {
        app.paused = false;
        app.timer.setTime(0); // time <= 0

        app.draw();

        // Verify that levelWin is still false
        assertFalse(app.levelWin);
    }

    @Test
    public void testDrawWhenLevelWin() {
        app.paused = false;
        app.timer.setTime(10); // time > 0
        app.levelWin = false;

        app.board.getBalls().clear();
        app.ballQueue.clear();

        app.draw();

        // Check that levelWin has been set to true
        assertTrue(app.levelWin);

        // Check that yellowTiles have been initialized
        assertFalse(app.yellowTiles.isEmpty());
    }

    @Test
    public void testDrawWhenLevelWinIsTrue() {
        app.paused = false;
        app.timer.setTime(10); // time > 0
        app.levelWin = true;

        int initialRemainingTime = app.scoreBoard.remainingTime;

        app.draw();

        // Assuming scoreBoard.updateScoreWithTime() decreases remainingTime
        int newRemainingTime = app.scoreBoard.remainingTime;
        assertTrue(newRemainingTime <= initialRemainingTime);
    }

    @Test
    public void testDrawWhenGameEnded() {
        app.paused = false;
        app.levelWin = true;
        app.timer.setTime(0); // time <= 0
        app.currentLevel = app.levels.size() - 1; // last level

        app.yellowTiles.add(new YellowTile(0, App.TOPBAR, null, app)); // Ensure yellowTiles is not empty

        app.draw();

        // Check that yellowTiles have been cleared
        assertTrue(app.yellowTiles.isEmpty());
    }

    @Test
    public void testDrawWhenBallShouldBeRemoved() {
        app.paused = false;
        app.timer.setTime(10); // time > 0
        app.countdown = 5; // countdown > 0

        // Create a ball that should be removed
        Ball ball = new Ball(100, 100, 0, app.board, app) {
            @Override
            public boolean shouldBeRemoved() {
                return true;
            }
        };

        app.board.getBalls().clear();
        app.board.getBalls().add(ball);

        app.draw();

        // Ensure the ball has been removed
        assertFalse(app.board.getBalls().contains(ball));
    }

    @Test
    public void testDrawWhenBallShouldNotBeRemoved() {
        app.paused = false;
        app.timer.setTime(10); // time > 0
        app.countdown = 5; // countdown > 0

        // Create a ball that should not be removed
        Ball ball = new Ball(100, 100, 0, app.board, app) {
            @Override
            public boolean shouldBeRemoved() {
                return false;
            }
        };

        app.board.getBalls().clear();
        app.board.getBalls().add(ball);

        app.draw();

        // Ensure the ball is still present
        assertTrue(app.board.getBalls().contains(ball));
    }

    @Test
    public void testDrawPlayerLinesAreDrawn() {
        app.paused = false;
        app.timer.setTime(10); // time > 0
        app.levelWin = false;

        // Add a player line
        PlayerLine line = new PlayerLine();
        line.addPoint(100, 100);
        line.addPoint(150, 150);
        app.playerLines.add(line);

        app.draw();

        // Since we can't check graphical output, we ensure no exceptions occur
        assertTrue(true);
    }












    // Test cases go here
}
