
import org.joml.Vector2f;

public class Movable {

    protected float[] vertices,tex_coords,colors;
    protected float d;
    protected Vector2f pos,vel,acc; //,center;
    protected Model model;


    public Movable(float x, float y, float size) {

        // sheets = new ArrayList<>();
        // center=new Vector2f(x-0.05f,y);
        // this.box=new AABB(pos,size/2);

        pos=new Vector2f(x,y);
        vel=new Vector2f(0,0);
        acc=new Vector2f(0,0);
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

    public void update() {
        //  this.box=new AABB(pos,size/2);
        if(!vel.equals(0, 0) || !acc.equals(0,0)) {
            vel.x+=0.1*acc.x;
            vel.y+=0.1*acc.y;
            pos.x+=0.1*vel.x;
            pos.y+=0.1*vel.y;

            vertices = new float[] {
                    pos.x-d,pos.y+d,0,  //top left
                    pos.x+d,pos.y+d,0,	//top right
                    pos.x+d,pos.y-d,0,	//bottom right
                    pos.x-d,pos.y-d,0   //bottom left
            };
            model.setVertices(vertices);
        }
    }


}

