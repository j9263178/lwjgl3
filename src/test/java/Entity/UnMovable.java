package Entity;

import Collision.AABB;
import Render.Model;
import org.joml.Vector2f;

public class UnMovable {
    protected float[] vertices,tex_coords,colors;
    public float d;
    public Vector2f pos;
    protected Model model;

    public AABB box;

    public UnMovable(float x, float y, float size) {

        // sheets = new ArrayList<>();
        // center=new Vector2f(x-0.05f,y);
        // this.box=new AABB(pos,size/2);

        pos=new Vector2f(x,y);
        d = size /2;

        vertices = new float[] {
                pos.x -d,pos.y +d,0,    //top left
                pos.x +d,pos.y +d,0,    //top right
                pos.x +d,pos.y -d,0,    //bottom right
                pos.x -d,pos.y -d,0     //bottom left
        };

        tex_coords = new float[] {
                0,0,  //top left
                1,0,  //top right
                1,1,  //bottom right
                0,1   //bottom left
        };

        colors = new float[] {
                1.0f,1.0f,1.0f, //top left
                1.0f,1.0f,1.0f,	//top right
                1.0f,1.0f,1.0f,	//bottom right
                1.0f,1.0f,1.0f  //bottom left
        };

        this.box=new AABB(this.pos,new Vector2f(d,d));
        model=new Model(vertices,tex_coords,colors);
    }
}
