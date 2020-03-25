package Entity;
import  Render.*;
import Render.Camera;

public class Entity extends Movable {

    private Model model;
    private Sheet sheet;
    private int i=0,j=0;
    private Shader shader;
    public boolean stopAnimation = false,visible=true,damageble=true;
    private Camera camera;

    public Entity(float x, float y, float size, Camera camera, Sheet sheet) {
        super(x, y, size);
        model=super.model;
        this.camera=camera;
        this.sheet = sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public void draw() {
        sheet.bind_on_frame(i,j);
        model.render();
    }

    public void setFrame(int i,int j) {
        this.i=i;
        this.j=j;
    }

    public void setPose(int j) {
        this.j=(j-1)%sheet.getYn()+1;
    }

    private boolean dir=true;

    public void nextFrame() {
        if(this.i==sheet.getXn()-1) dir=false;
        if(this.i==0) dir=true;
        if(dir) i++;
        else i--;
    }

}

