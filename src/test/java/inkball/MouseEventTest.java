package inkball;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import processing.core.PApplet;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class MouseEventTest {




    @Test
    public void testMouseReleasedRight(){
        //Tests that the player line is removed when the right mouse button is released
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
    public void testMouseReleasedLeft(){
        //Tests that the current line is set to null when the left mouse button is released
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);

        app.currentLine = new PlayerLine();
        app.currentLine.addPoint(100, 100);

        MouseEvent mouseEvent = new MouseEvent(app, 0, MouseEvent.RELEASE, 0, 150, 150, PConstants.LEFT, 1);
        app.mouseReleased(mouseEvent);

        assertNull(app.currentLine, "currentLine should be null after left mouse button release");


    }



    @Test
    public void testMouseReleasedLeftwithCTRL(){
        //Tests that the player line is removed when the left mouse button is released with the CTRL key pressed
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
    public void testSpaceKeyTogglesPause() {
        //Tests that the space key toggles the game between paused and unpaused
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);

        KeyEvent spaceEvent = new KeyEvent(app, 0, 0, 0, ' ', ' ');
        app.keyPressed(spaceEvent);

        assertTrue(app.paused, "Game should be paused");


        app.keyPressed(spaceEvent);

        assertFalse(app.paused, "Game should be unpaused");

    }

    @Test
    public void testRKeyRestartsLevel() {
        //Tests that the R key restarts the current level
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        app.currentLevel = 0;
        app.levels.append(new JSONObject());
        app.levels.append(new JSONObject());

        KeyEvent rEvent = new KeyEvent(app, 0, 0, 0, 'r', 'r');
        app.keyPressed(rEvent);


    }

    @Test
    public void mousePressedLeftTest(){
        //Tests that the current line is not null after the left mouse button is pressed
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);

        MouseEvent mouseEvent = new MouseEvent(app, 0, MouseEvent.PRESS, 0, 150, 150, PConstants.LEFT, 1);
        app.mousePressed(mouseEvent);

        assertNotNull(app.currentLine, "currentLine should not be null after left mouse button press");
    }
}
