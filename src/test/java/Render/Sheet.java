package Render;

import org.joml.Matrix4f;

public class Sheet extends Texture {
    private Shader shader;
    private Matrix4f scale,translation;
    private int xn,yn;

    public Sheet(String filename, int xn, int yn){
        super(filename);
        this.xn=xn;
        this.yn=yn;

        scale = new Matrix4f().scale(1.0f /((float)xn),1.0f /((float)yn),0);
        translation = new Matrix4f();
    }

    public int getXn(){return this.xn;}
    public int getYn(){return this.yn;}

    public void setShader(Shader shader){
        this.shader=shader;
    }

    public void bind_on_frame(int x, int y) {
        this.bind();
        scale.translate(x,y,0,translation);
        shader.setUniform("sampler", 0);
        shader.setUniform("texmodifier", translation);
        //texture.unbind();
    }
    public void bind_on_tile(int x, int y) {
        this.bind();
        scale.translate(x-1,y-1,0,translation);
        shader.setUniform("sampler", 0);
        shader.setUniform("texmodifier", translation);
        //texture.unbind();
    }

}
