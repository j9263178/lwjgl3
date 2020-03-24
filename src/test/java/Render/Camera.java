package Render;
import Entity.Movable;
import org.joml.*;

public class Camera {
    private int scale = 1;
    private Vector3f position;
    private Matrix4f projection;

    public Camera(float width, float height) {
        this.position = new Vector3f(0, 0, 0);
        projection = new Matrix4f().setOrtho2D(-width / 2, width / 2, -height / 2, height / 2).scale(scale);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getProjection() {
        Matrix4f target = new Matrix4f();
        Matrix4f pos = new Matrix4f().setTranslation(position);
        pos.mul(projection, target);
        return target;
    }

}
