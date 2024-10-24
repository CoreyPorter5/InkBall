package inkball;

public class ScoreBoard {



    private App app;
    protected int remainingTime;
    private int lastUpdateTime;


    public ScoreBoard(App app) {
        this.app = app;
        this.remainingTime = 0;
        this.lastUpdateTime = app.millis();
    }



    public void display(){
        app.fill(205, 204, 205);
        app.noStroke();
        app.rect(455, 12, 150, 30);
        app.textSize(21);
        app.fill(0);
        app.text("Score:  " + app.score, 455, 30);
    }



    public void addTimeToScore(int time){
        this.remainingTime = time;
    }


    public void updateScoreWithTime(){
        if(remainingTime > 0){
            int currentTime = app.millis();
            if (currentTime - lastUpdateTime >= 67) {
                app.score++;
                remainingTime--;
                app.timer.setTime(remainingTime);
                lastUpdateTime = currentTime;

            }
        }
    }
}
