package Map;

import Entity.UnMovable;
import Render.Camera;
import Render.Shader;
import Render.Sheet;

public class Tile extends UnMovable {
    public int[] id={0,0};
    public boolean isSolid;
    private Sheet tileset;
    private Shader shader;
    private Camera camera;

    Tile(float x, float y, float size, Shader shader, Camera camera,Sheet tileset){
        super(x,y,size);
        this.shader=shader;
        this.camera=camera;
        this.tileset = tileset;
    }

    public void setId(int i,int j){
        id[0]=i;
        id[1]=j;
    }

    public void setId(int id){
        this.id[0]=(id % tileset.getXn()!=0)?id % tileset.getXn():tileset.getXn();
        this.id[1]=id / (tileset.getXn()+1) + 1;
    }

    public void setSolid(boolean is){
        isSolid=is;
    }

    public void draw(){
        shader.bind();
        shader.setUniform("projection",this.camera.getProjection().scale(256));
        tileset.bind_on_tile(id[0],id[1]);
        model.render();
        shader.unbind();
    }

    public float[] getXY(){
        return new float[]{this.pos.x,this.pos.y};
    }
}
