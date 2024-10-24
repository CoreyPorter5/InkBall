package inkball;

import processing.core.PGraphics;
import processing.core.PImage;
import java.util.ArrayList;

public class BallQueue {

    private ArrayList<String> queue;
    private App app;
    private ArrayList<Float> ballXPositions;
    private static final float[] TARGET_POSITIONS = {20, 50, 80, 110, 140};
    private static final int MAX_DISPLAY_BALLS = 5;
    private boolean ballsAreMoving = false;

    public BallQueue(ArrayList<String> queue, App app) {
        this.queue = queue;
        this.app = app;
        this.ballXPositions = new ArrayList<>();
        int numBalls = Math.min(MAX_DISPLAY_BALLS, queue.size());
        for (int i = 0; i < numBalls; i++) {
            ballXPositions.add(TARGET_POSITIONS[i]);
        }

    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public String dequeue() {
        if (!queue.isEmpty()) {
            String removedBall = queue.remove(0);
            ballXPositions.remove(0);
            ballsAreMoving = true;
            if (queue.size() >= MAX_DISPLAY_BALLS) {
                ballXPositions.add(TARGET_POSITIONS[MAX_DISPLAY_BALLS - 1] + 30);
            }
            return removedBall;
        }
        return null;
    }

    public void enqueue(String ballColor) {
        queue.add(ballColor);
        if (queue.size() <= MAX_DISPLAY_BALLS) {
            ballXPositions.add(TARGET_POSITIONS[queue.size() - 1]);
        } else {
            ballXPositions.add(TARGET_POSITIONS[MAX_DISPLAY_BALLS - 1] + 30);
        }
    }

    public void displayNextBalls() {
        app.rect(15, 10, 160, 38);
        int numBalls = Math.min(MAX_DISPLAY_BALLS, queue.size());
        for (int i = 0; i < numBalls; i++) {
            String ballColor = queue.get(i);
            PImage ballImage = app.getBallImage(ballColor);
            float currentX = ballXPositions.get(i);
            float targetX = TARGET_POSITIONS[i];

            if (ballsAreMoving && currentX > targetX) {
                currentX -= 1;
                if (currentX < targetX) {
                    currentX = targetX;
                }
                ballXPositions.set(i, currentX);
            }

            app.image(ballImage, currentX, 15, 24, 24);
        }

        boolean allBallsAtTarget = true;
        for (int i = 0; i < numBalls; i++) {
            if (ballXPositions.get(i) > TARGET_POSITIONS[i]) {
                allBallsAtTarget = false;
                break;
            }
        }
        if (allBallsAtTarget) {
            ballsAreMoving = false;
        }
    }

    public void clear() {
        queue.clear();
        ballXPositions.clear();
    }
}
