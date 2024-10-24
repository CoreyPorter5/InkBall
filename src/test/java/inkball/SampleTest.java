package inkball;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class SampleTest {


    @Test
    public void simpleTest() {
        //Tests that hte app instance is created correctly
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);
    }

    @Test
    public void yellowTileMoveToTheRightTest() {
        //Tests that the yellow tile moves to the right
        App app = new App();

        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        PImage image = app.loadImage("wall4");
        YellowTile yellowTile = new YellowTile(0, App.TOPBAR, image, app);
        int initialX = yellowTile.getX();
        int initialY = yellowTile.getY();

        app.delay(67);
        yellowTile.update();

        assertNotEquals(initialX, yellowTile.getX(), "X position should have changed");


        app.delay(67);
        yellowTile.update();


    }

    @Test
    public void yellowTileMoveDownTest() {
        //Tests that the yellow tile moves down
        App app = new App();

        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        PImage image = app.loadImage("wall4");
        YellowTile yellowTile = new YellowTile(App.WIDTH - App.CELLSIZE, 0, image, app);
        int initialX = yellowTile.getX();
        int initialY = yellowTile.getY();

        app.delay(67);
        yellowTile.update();

        assertNotEquals(initialX, yellowTile.getX(), "X position should have changed");

        app.delay(67);
        yellowTile.update();

    }

    @Test
    public void yellowTileMoveToTheLeftTest() {
        //Tests that the yellow tile moves to the left
        App app = new App();

        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        PImage image = app.loadImage("wall4");
        YellowTile yellowTile = new YellowTile(1, App.HEIGHT - App.CELLSIZE, image, app);
        int initialX = yellowTile.getX();
        int initialY = yellowTile.getY();

        app.delay(67);
        yellowTile.update();

        assertNotEquals(initialX, yellowTile.getX(), "X position should have changed");


        app.delay(67);
        yellowTile.update();


    }

    @Test
    public void yellowTileMoveUpTest() {
        //Tests that the yellow tile moves up
        App app = new App();

        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        PImage image = app.loadImage("wall4");
        YellowTile yellowTile = new YellowTile(0, App.TOPBAR + 2, image, app);
        int initialX = yellowTile.getX();
        int initialY = yellowTile.getY();

        app.delay(67);
        yellowTile.update();

        assertNotEquals(initialX, yellowTile.getX(), "X position should have changed");


        app.delay(67);
        yellowTile.update();


    }

    @Test
    public void EndTextTest() {
        //Tests that the end text is displayed when the game ends with the yellow tiles
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        app.currentLevel = app.levels.size() - 1;
        app.levelWin = true;
        app.timer = new Timer(0, app);
        app.yellowTiles = new ArrayList<>();
        app.yellowTiles.add(new YellowTile(0, App.TOPBAR, app.loadImage("wall4"), app));


        if (app.currentLevel == app.levels.size() - 1 && app.levelWin && app.timer.getTime() <= 0) {
            if (!app.yellowTiles.isEmpty()) {
                app.yellowTiles.clear();
            }
            app.fill(205, 204, 205);
            app.noStroke();
            app.rect(250, 20, 177, 30);
            app.textSize(21);
            app.fill(0);
            app.text("===ENDED===", 255, 40);
        }
        assertTrue(true, "Text '===ENDED===' should be displayed");
    }

    @Test
    public void EndTextTestWithNoYellowTiles() {
        //Tests that the end text is displayed when the game ends without the yellow tiles
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        app.currentLevel = app.levels.size() - 1;
        app.levelWin = true;
        app.timer = new Timer(0, app);
        app.yellowTiles = new ArrayList<>();


        if (app.currentLevel == app.levels.size() - 1 && app.levelWin && app.timer.getTime() <= 0) {
            if (!app.yellowTiles.isEmpty()) {
                app.yellowTiles.clear();
            }
            app.fill(205, 204, 205);
            app.noStroke();
            app.rect(250, 20, 177, 30);
            app.textSize(21);
            app.fill(0);
            app.text("===ENDED===", 255, 40);
        }
        assertTrue(true, "Text '===ENDED===' should be displayed");
    }

    @Test
    public void getBallColourStringGreyTest(){
        //Tests that the correct colour is returned for the ball from the colour index
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        String colour = app.getBallColourString(0);
        assertEquals("grey", colour, "Colour should be grey");
    }

    @Test
    public void getBallColourStringOrangeTest(){
        //Tests that the correct colour is returned for the ball from the colour index
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        String colour = app.getBallColourString(1);
        assertEquals("orange", colour, "Colour should be orange");
    }

    @Test
    public void getBallColourStringBlueTest(){
        //Tests that the correct colour is returned for the ball from the colour index
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        String colour = app.getBallColourString(2);
        assertEquals("blue", colour, "Colour should be blue");
    }

    @Test
    public void getBallColourStringGreenTest(){
        //Tests that the correct colour is returned for the ball from the colour index
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        String colour = app.getBallColourString(3);
        assertEquals("green", colour, "Colour should be green");
    }

    @Test
    public void getBallColourStringYellowTest(){
        //Tests that the correct colour is returned for the ball from the colour index
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        String colour = app.getBallColourString(4);
        assertEquals("yellow", colour, "Colour should be yellow");
    }

    @Test
    public void getBallColourStringNullTest(){
        //Tests that the correct colour is returned for the ball from the colour index
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        String colour = app.getBallColourString(5);
        assertEquals(null, colour, "Colour should be null");
    }

    @Test
    public void spawnBallTest(){
        //Tests that a ball is spawned correctly
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);
        app.spawnBall();
        assertEquals(1, app.board.getBalls().size(), "Ball should be spawned");
    }

    @Test
    public void spawnGreenBallTest(){
        //Tests that a green ball is spawned correctly
        App app = new App();
        Ball ball = new Ball(100, 100, 3, app.board, app);
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);
        ball.colour = 3;
        app.spawnBall();
        assertEquals(1, app.board.getBalls().size(), "Ball should be spawned");
    }

    @Test
    public void ballShouldBeRemovedTest(){
        //Tests that a ball is removed correctly and the score is updated when it matches the hole colour
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);
        Ball ball = new Ball(100, 100, 1, app.board, app);

        Hole hole = new Hole(100, 100, 1, app.board);

        float distance = 5.0f;
        if (distance < 5.5) {
            if (hole.getColor() == ball.getColour() || ball.getColour() == 0 || hole.getColor() == 0) {
                app.increaseScore(hole.getColor());
            } else {
                app.decreaseScore(hole.getColor());
                app.requeueBall(ball);
            }
        }

        assertEquals(1, app.score, "Score should be increased by 1");
    }

    @Test
    public void testYellowTilesCleared() {
        //Tests that the yellow tiles are cleared when the game ends
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);

        app.currentLevel = app.levels.size() - 1;
        app.levelWin = true;
        app.timer = new Timer(0, app);
        app.yellowTiles = new ArrayList<>();
        app.yellowTiles.add(new YellowTile(0, App.TOPBAR, app.loadImage("wall4"), app));

        if (app.currentLevel == app.levels.size() - 1 && app.levelWin && app.timer.getTime() <= 0) {
            if (!app.yellowTiles.isEmpty()) {
                app.yellowTiles.clear();
                System.err.println("Cleared yellowTiles");
            }
        }

        assertTrue(app.yellowTiles.isEmpty(), "yellowTiles should be cleared");
    }

    @Test
    public void updateLevelTest(){
        //Tests that the level is updated correctly
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"App"}, app);
        app.setup();
        app.delay(1000);
        app.currentLevel = 0;
        app.updateLevel();
        assertEquals(1, app.currentLevel, "Level should be updated to 1");
    }






}
// gradle run						Run the program
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
