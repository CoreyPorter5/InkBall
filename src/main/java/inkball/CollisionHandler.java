package inkball;

import processing.core.PApplet;
import processing.core.PVector;
import java.util.List;




public class CollisionHandler {

    private App app;
    private Board board;
    protected final int ADJUSTMENT = 6;


    public CollisionHandler(App app, Board board){
        this.app = app;
        this.board = board;
    }


    public void testForCollisions(Ball ball) {
        for (Wall wall : board.getWalls()) {
            if (handleCollisionWithWall(ball, wall)) {
                break;
            }

        }

        for (Brick brick : board.getBricks()) {
            if (handleCollisionWithBrick(ball, brick)) {
                break;
            }
        }

        for (PlayerLine line : app.playerLines) {
            if (handleCollisionwithPlayerLines(ball, line)) {
                break;
            }
        }



        for (Hole hole : board.getHoles()) {
            if (handleCollisionWithHole(ball, hole)) {
                break;
            }
        }

    }







    public boolean handleCollisionwithPlayerLines(Ball ball, PlayerLine playerLine) {
        PVector ballNextPos = new PVector((float) (ball.getX() + ball.getX_velocity()), (float) (ball.getY() + ball.getY_velocity()));
        for(int i = 0; i < playerLine.getPoints().size() - 1; i++) {
            PVector p1 = playerLine.getPoints().get(i);
            PVector p2 = playerLine.getPoints().get(i + 1);
            if (isCollidingWithPlayerLineSegment(ballNextPos, p1, p2)) {
                handleCollisionResponse(ball, p1, p2, ballNextPos);
                app.playerLines.remove(playerLine);
                return true;
            }
        }
        return false;
    }

    public boolean handleCollisionWithHole(Ball ball, Hole hole) {
        PVector ballNextPos = new PVector((float) (ball.getX() + ball.getX_velocity()), (float) (ball.getY() + ball.getY_velocity()));
        PVector holeCentre = new PVector(hole.getCentreX(), hole.getCentreY());

        float distance = PVector.dist(ballNextPos, holeCentre);

        if (distance < 32) {
            PVector attractionForce = PVector.sub(holeCentre, ballNextPos).mult(0.05f);
            ball.applyAttractionForce(attractionForce);

            float sizeChangeFactor = 1 - (0.05f * (32 - distance) / 32);
            ball.updateRadius(sizeChangeFactor);

        }
        return false;
    }


        public boolean handleCollisionWithWall (Ball ball, Wall wall){



            PVector ballNextPos = new PVector((float) (ball.getX() + ball.getX_velocity()), (float) (ball.getY() + ball.getY_velocity()));


            PVector[] points = {
                    new PVector(wall.getX(), wall.getY()),
                    new PVector(wall.getX() + App.CELLSIZE, wall.getY()),
                    new PVector(wall.getX() + App.CELLSIZE, wall.getY() + App.CELLSIZE),
                    new PVector(wall.getX(), wall.getY() + App.CELLSIZE)
            };


            for (int i = 0; i < points.length; i++) {
                PVector p1 = points[i];
                PVector p2 = points[(i + 1) % points.length];

                if (isCollidingWithLineSegment(ballNextPos, p1, p2)) {
                    ball.changeColour(wall.getColour());
                    handleCollisionResponse(ball, p1, p2, ballNextPos);
                    return true;
                }
            }


            return false;

        }


    public boolean handleCollisionWithBrick (Ball ball, Brick brick){



        PVector ballNextPos = new PVector((float) (ball.getX() + ball.getX_velocity()), (float) (ball.getY() + ball.getY_velocity()));


        PVector[] points = {
                new PVector(brick.getX(), brick.getY()),
                new PVector(brick.getX() + App.CELLSIZE, brick.getY()),
                new PVector(brick.getX() + App.CELLSIZE, brick.getY() + App.CELLSIZE),
                new PVector(brick.getX(), brick.getY() + App.CELLSIZE)
        };


        for (int i = 0; i < points.length; i++) {
            PVector p1 = points[i];
            PVector p2 = points[(i + 1) % points.length];

            if (isCollidingWithLineSegment(ballNextPos, p1, p2)) {
                if (brick.canBeDamagedBy(ball.getColour())) {
                    brick.hit();
                    handleCollisionResponse(ball, p1, p2, ballNextPos);
                    return true;
                }else{
                    handleCollisionResponse(ball, p1, p2, ballNextPos);
                    return true;
                }

            }
        }


        return false;

    }


        protected boolean isCollidingWithLineSegment (PVector ballPos, PVector p1, PVector p2){
            float distanceP1Ball = PVector.dist(p1, ballPos);
            float distanceP2Ball = PVector.dist(p2, ballPos);
            float distanceP1P2 = PVector.dist(p1, p2);

            return (distanceP1Ball + distanceP2Ball) < (distanceP1P2 + ADJUSTMENT);
        }

        protected boolean isCollidingWithPlayerLineSegment (PVector ballPos, PVector p1, PVector p2){
            float distanceP1Ball = PVector.dist(p1, ballPos);
            float distanceP2Ball = PVector.dist(p2, ballPos);
            float distanceP1P2 = PVector.dist(p1, p2);

            return (distanceP1Ball + distanceP2Ball) < (distanceP1P2 + Board.BALLRADIUS + ADJUSTMENT);
        }


        private void handleCollisionResponse (Ball ball, PVector p1, PVector p2, PVector ballPos){

            double dx = p2.x - p1.x;
            double dy = p2.y - p1.y;
            PVector normal1 = new PVector((float) -dy, (float) dx).normalize();
            PVector normal2 = new PVector((float) dy, (float) -dx).normalize();

            PVector chosenNormal = chooseCloserNormal(p1, p2, normal1, normal2, ballPos);

            double dotProduct = PVector.dot(chosenNormal, new PVector((float) ball.getX_velocity(), (float) ball.getY_velocity()));
            double xVelocity = ball.getX_velocity() - 2 * dotProduct * chosenNormal.x;
            double yVelocity = ball.getY_velocity() - 2 * dotProduct * chosenNormal.y;


            ball.setX_velocity((float) xVelocity);
            ball.setY_velocity((float) yVelocity);


        }


        protected PVector chooseCloserNormal (PVector p1, PVector p2, PVector normal1, PVector normal2, PVector ballPos)
        {
            PVector midPoint = PVector.add(p1, p2).mult(0.5f);

            PVector testPoint1 = PVector.add(midPoint, normal1);
            PVector testPoint2 = PVector.add(midPoint, normal2);

            float dist1 = PVector.dist(testPoint1, ballPos);
            float dist2 = PVector.dist(testPoint2, ballPos);

            if (dist1 < dist2) {
                return normal1;
            } else {
                return normal2;
            }
        }
    }
