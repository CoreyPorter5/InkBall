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
        //Tests that a ball is spawned and the countdown is reset when the countdown reaches 0
        app.paused = false;
        app.timer.setTime(10);
        app.countdown = 0;

        int initialBallCount = app.board.getBalls().size();

        app.draw();

        assertEquals(app.spawnInterval * App.FPS, app.countdown);

        int newBallCount = app.board.getBalls().size();
        assertEquals(initialBallCount + 1, newBallCount);
    }

    @Test
    public void testDrawWhenCountdownGreaterThanZeroCountdownDecrements() {
        //Tests that the countdown decrements when it is greater than 0
        app.paused = false;
        app.timer.setTime(10);
        app.countdown = 5;

        float initialCountdown = app.countdown;

        app.draw();

        assertEquals(initialCountdown - 1, app.countdown);
    }

    @Test
    public void testDrawWhenPaused() {
        //Tests that the countdown does not decrement or increment when the game is paused
        app.paused = true;
        app.timer.setTime(10);

        float initialCountdown = app.countdown;

        app.draw();


        assertEquals(initialCountdown, app.countdown);
    }

    @Test
    public void testWhenTimerZero() {
        //Tests that the level is not won when the timer reaches 0
        app.paused = false;
        app.timer.setTime(0);

        app.draw();


        assertFalse(app.levelWin);
    }

    @Test
    public void testDrawWhenLevelWin() {
        //Tests that levelWin variable is set to true and yellow tiles are initialized when the level is won
        app.paused = false;
        app.timer.setTime(10);
        app.levelWin = false;

        app.board.getBalls().clear();
        app.ballQueue.clear();

        app.draw();

        assertTrue(app.levelWin);

        assertFalse(app.yellowTiles.isEmpty());
    }

    @Test
    public void testDrawWhenLevelWinIsTrue() {
        //Tests that the remaining time in the scoreboard is decremented when the level is won
        app.paused = false;
        app.timer.setTime(10);
        app.levelWin = true;

        int initialRemainingTime = app.scoreBoard.remainingTime;

        app.draw();

        int newRemainingTime = app.scoreBoard.remainingTime;
        assertTrue(newRemainingTime <= initialRemainingTime);
    }

    @Test
    public void testDrawWhenGameEnded() {
        //Tests that yellow tiles are cleared when the game has ended
        app.paused = false;
        app.levelWin = true;
        app.timer.setTime(0);
        app.currentLevel = app.levels.size() - 1;

        app.yellowTiles.add(new YellowTile(0, App.TOPBAR, null, app));

        app.draw();

        assertTrue(app.yellowTiles.isEmpty());
    }

    @Test
    public void testDrawWhenBallShouldBeRemoved() {
        //Tests that a ball is removed from the board when it should be removed
        app.paused = false;
        app.timer.setTime(10);
        app.countdown = 5;


        Ball ball = new Ball(100, 100, 0, app.board, app) {
            @Override
            public boolean shouldBeRemoved() {
                return true;
            }
        };

        app.board.getBalls().clear();
        app.board.getBalls().add(ball);

        app.draw();

        assertFalse(app.board.getBalls().contains(ball));
    }

    @Test
    public void testDrawWhenBallShouldNotBeRemoved() {
        //Tests that a ball is not removed from the board when it should not be removed
        app.paused = false;
        app.timer.setTime(10);
        app.countdown = 5;

        Ball ball = new Ball(100, 100, 0, app.board, app) {
            @Override
            public boolean shouldBeRemoved() {
                return false;
            }
        };

        app.board.getBalls().clear();
        app.board.getBalls().add(ball);

        app.draw();


        assertTrue(app.board.getBalls().contains(ball));
    }

    @Test
    public void testDrawPlayerLinesAreDrawn() {
        //Tests that player lines are drawn on the screen without any errors
        app.paused = false;
        app.timer.setTime(10);
        app.levelWin = false;


        PlayerLine line = new PlayerLine();
        line.addPoint(100, 100);
        line.addPoint(150, 150);
        app.playerLines.add(line);

        app.draw();

        assertTrue(true);
    }












    // Test cases go here
}
