package Entity;
import Collision.AABB;
import  Render.*;
import Render.Camera;
import org.joml.AABBf;

public class Entity extends Movable implements Comparable<Entity>{

    private Model model;
    private Sheet sheet;
    private int i=0,j=0;
    private Shader shader;
    public boolean stopAnimation = false,visible=true,damageble=true;

    public Entity(float x, float y, float size,Sheet sheet) {
        super(x, y, size);
        model=super.model;
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

    @Override
    public int compareTo(Entity o) {
        int cq=(int)(o.getPos().y*10000f);
        return cq-(int)(this.pos.y*10000f);
    }
}

