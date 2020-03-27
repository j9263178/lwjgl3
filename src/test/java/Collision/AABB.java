package Collision;
import org.joml.AABBf;
import org.joml.Vector2f;

public class AABB {
    private Vector2f center;
    private float half;

    public AABB(Vector2f center, float half){
        this.center=center;
        this.half=half;
    }

    public boolean test(AABB box2){
        if (Math.abs(this.center.x - box2.center.x) > 0.5*(this.half + box2.half))
            return false;

        if (Math.abs(this.center.y - box2.center.y) > 0.5*(this.half + box2.half))
            return false;

        return true;
    }


}
