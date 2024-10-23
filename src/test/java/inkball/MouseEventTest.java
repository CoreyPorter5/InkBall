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

public class MouseEventTest {

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
}
