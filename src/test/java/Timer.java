public class Timer {
    private double resetTime;
    private double currentTime;
    private double elapsedTime=0;
    private double lastTime=0;
    public Timer (double resetTime){
        this.resetTime=resetTime;
    }
    public void update(){
        this.currentTime=System.nanoTime()/(double)1000000000L;
        this.elapsedTime += currentTime - lastTime;
    }
    public boolean isReset(){
        if(this.elapsedTime>1/resetTime){
            this.elapsedTime=0;
            return true;}
        else return false;
    }
    public void reset(){
        this.lastTime = this.currentTime;
    }
}
