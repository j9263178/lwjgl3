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
    //	Camara camara;
    //  ArrayList<sheet> sheets;
    //  public Matrix2f rotate;
    //  public AABB box;

    public Entity(float x, float y, float size, Camera camera, Shader shader, Sheet sheet) {
        super(x, y, size);
        model=super.model;
        this.shader=shader;
        this.camera=camera;
        this.sheet = sheet;
        this.sheet.setShader(this.shader);
    }

    public void setShader(Shader shader){
        this.shader=shader;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
        this.sheet.setShader(shader);
    }

    public void draw() {
        shader.bind();
        shader.setUniform("projection",this.camera.getProjection().scale(256));
        sheet.bind_on_frame(i,j);
        model.render();
        shader.unbind();
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

