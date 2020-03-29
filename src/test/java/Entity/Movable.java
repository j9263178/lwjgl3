package Entity;

import Collision.AABB;
import Render.Model;
import org.joml.AABBf;
import org.joml.Vector2f;

public class Movable {

    protected float[] vertices,tex_coords,colors;
    public float w,h,d=0;
    protected Vector2f pos,vel,acc; //,center;
    protected Model model;
    public AABB box;

    public Movable(float x, float y, float size) {

        // sheets = new ArrayList<>();
        // center=new Vector2f(x-0.05f,y);
        // this.box=new AABB(pos,size/2);
        this.w=size;
        this.h=size;
        pos=new Vector2f(x,y);
        vel=new Vector2f(0,0);
        acc=new Vector2f(0,0);
        float d = size /2;

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

       // this.box=new AABB(new Vector2f(pos.x,pos.y),new Vector2f(d,d));
        this.box=new AABB(this.pos,new Vector2f(d,d));
        model=new Model(vertices,tex_coords,colors);
    }

    public Movable(float x, float y, float w,float h) {
        this.w=w; this.h=h;
        pos=new Vector2f(x,y);
        vel=new Vector2f(0,0);
        acc=new Vector2f(0,0);
        w = w/2;
        h= h/2;
        vertices = new float[] {
                pos.x -w,pos.y +h,0,    //top left
                pos.x +w,pos.y +h,0,    //top right
                pos.x +w,pos.y -h,0,    //bottom right
                pos.x -w,pos.y -h,0     //bottom left
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
        this.box=new AABB(this.pos,new Vector2f(w/2,h/2));
        model=new Model(vertices,tex_coords,colors);
    }

    public void setPos(float x,float y) {
        pos.x=x;
        pos.y=y;
    }
    public void setVel(float x,float y) {
        vel.x=x;
        vel.y=y;
    }
    public void setAcc(float x,float y) {
        acc.x=x;
        acc.y=y;
    }
    public void addPos(float x,float y) {
        pos.x+=x;
        pos.y+=y;
    }
    public void addVel(float x,float y) {
        vel.x+=x;
        vel.y+=y;
    }
    public void addAcc(float x,float y) {
        acc.x+=x;
        acc.y+=y;
    }
    public Vector2f getPos(){
        return this.pos;
    }
    public Vector2f getVel(){
        return this.vel;
    }
    public Vector2f getAcc(){
        return this.acc;
    }
    public void update() {
          this.box=new AABB(new Vector2f(pos.x,pos.y),new Vector2f(w/2,h/2));

            vel.x+=0.1*acc.x;
            vel.y+=0.1*acc.y;
            pos.x+=0.1*vel.x;
            pos.y+=0.1*vel.y;

            vertices = new float[] {
                    pos.x-w/2,pos.y+h/2,0,  //top left
                    pos.x+w/2,pos.y+h/2,0,	//top right
                    pos.x+w/2,pos.y-h/2,0,	//bottom right
                    pos.x-w/2,pos.y-h/2,0   //bottom left
            };
            model.setVertices(vertices);
    }


}

