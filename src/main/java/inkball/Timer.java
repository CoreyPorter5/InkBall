package inkball;



public class Timer {

    private int time;
    private App app;
    private int lastUpdateTime;
    private boolean paused;


    public Timer(int time, App app) {
        this.time = time;
        this.app = app;
        this.lastUpdateTime = app.millis();
        this.paused = false;
    }

    public void update(){
        if(!paused && !app.levelWin){
            int currentTime = app.millis();
            if (currentTime - lastUpdateTime > 1000) {
                time--;
                lastUpdateTime += 1000;

                if (time < 0){
                    time = 0;
                }
            }

        }else{
            lastUpdateTime = app.millis();
        }

    }

    public void display() {
        app.fill(205, 204, 205);
        app.noStroke();
        app.rect(450, 30, 150, 30);
        app.textSize(21);
        app.fill(0);
        app.text("Time:   " + time, 455, 55);



    }

    public int getTime(){
        return this.time;
    }

    public void setTime(int time){
        this.time = time;
    }

    public void setPaused(boolean paused){
        this.paused = paused;
    }


}
