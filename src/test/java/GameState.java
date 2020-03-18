public class GameState {
    private int state;
    private Entity hi;

    GameState(int state){
        this.state=state;
        hi = new Entity(0,0,0.4f);
        hi.setSheet(new Sheet(1,"hihi",1,1));
        hi.setShader(new Shader("hihi"));
    }

    public void setState(int state){
        this.state=state;
    }

    public void Update(){
        if(state==0){
            hi.update();
            hi.draw();
        }
    }
}
