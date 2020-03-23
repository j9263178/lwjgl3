package GUI;
import GUI.Camera;
import Entity.Movable;
import org.joml.*;

public class Camera extends Movable {
    private int scale=1;
    public  Vector3f position;
    private Matrix4f projection;
    public Camera(float width,float height) {
        super(0,0,0);
        position = new Vector3f(0,0,0);
        projection = new Matrix4f().setOrtho2D(-width/2,width/2,-height/2,height/2).scale(scale);
    }


    public void update() {
        super.update();
        position.x=super.pos.x;
        position.y=super.pos.y;
    }

    public Matrix4f getProjection() {
        Matrix4f target = new Matrix4f();
        Matrix4f pos = new Matrix4f().setTranslation(position);
        projection.mul(pos,target);
        return target;
    }

    public void zoomIn(float i) {
        projection.scale(i);
    }
    public void zoomOut(float i) {
        scale-=i;
        projection.scale(scale);
    }

}
