
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.BufferUtils;


public class Model {
    private int draw_count;
    private int v_id,t_id,c_id;

    public Model(float[] vertices, float[] tex_coords, float[] colors) {

        draw_count = vertices.length/2;

        v_id=GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, v_id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, createBuffer(vertices), GL15.GL_STATIC_DRAW);

        t_id=GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, t_id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, createBuffer(tex_coords), GL15.GL_STATIC_DRAW);

        c_id=GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, c_id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, createBuffer(colors), GL15.GL_STATIC_DRAW);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

    }

    public void render() {
	/*	GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);*/

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, v_id);
        GL20.glVertexAttribPointer(0,3, GL11.GL_FLOAT, false,0,0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, t_id);
        GL20.glVertexAttribPointer(1,2, GL11.GL_FLOAT, false,0,0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, c_id);
        GL20.glVertexAttribPointer(2,3, GL11.GL_FLOAT, false,0,0);

        GL11.glDrawArrays(GL11.GL_QUADS, 0, draw_count);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		/*GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);*/

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
    }

    public FloatBuffer createBuffer(float[] vertices) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.flip();
        return buffer;
    }

    public void setVertices(float[] vertices) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, v_id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, createBuffer(vertices), GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //Not sure if necessary
    }

    public int getV_id(){
        return v_id;
    }
    public int getT_id(){
        return t_id;
    }
    public int getC_id(){
        return c_id;
    }
}
