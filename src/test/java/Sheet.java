
import de.matthiasmann.twl.utils.PNGDecoder;
import org.joml.Matrix4f;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Sheet {
    private Shader shader;
    private int id;
    private Texture texture;
    private Matrix4f scale,translation;

    Sheet(int id){
        this.id=id;
    }

    Sheet(int id,String filename,int xn,int yn){
        this.id=id;
        this.setTexture(filename,xn,yn);
    }

    public int getName(){
        return this.id;
    }

    public void setTexture(String filename, int xn, int yn) {
        //this.n = n;

        try{
            texture = loadTexture("/Users/joseph/lwjgl3/src/main/resources/textures/"+filename+".PNG");
        } catch(IOException e) {
            e.printStackTrace();
        }

        assert this.texture != null;

        //float yfix = (float) this.texture.getTextureHeight() / (float) this.texture.getImageHeight();
        //private int n=1;
        //float xfix = (float) this.texture.getTextureWidth() / (float) this.texture.getImageWidth();
        //scale = new Matrix4f().scale(1.0f /((float)xn* xfix),1.0f /((float)yn* yfix),0);

        scale = new Matrix4f().scale(1.0f /((float)xn),1.0f /((float)yn),0);

        translation = new Matrix4f();
        //System.out.println(this.texture.getTextureID()+" "+scale);
    }

    void setShader(Shader shader){
        this.shader=shader;
    }

    void bind_on_frame(int x, int y) {
        texture.bind();
        scale.translate(x,y,0,translation);
        shader.setUniform("sampler", 0);
        shader.setUniform("texmodifier", translation);
    }


	/*public void bind(int pos,Shader shader) {
		int x =  pos / n;
		int y =  pos % n;
		bind(x,y,shader);
	}*/

    private static Texture loadTexture(String fileName) throws IOException {

        //load png file
        FileInputStream in = new FileInputStream(fileName);
        PNGDecoder decoder = new PNGDecoder(in);

        //create a byte buffer big enough to store RGBA values
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

        //decode
        decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);

        //flip the buffer so its ready to read
        buffer.flip();

        //create a texture
        int id = glGenTextures();

        //bind the texture
        glBindTexture(GL_TEXTURE_2D, id);

        //tell opengl how to unpack bytes
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //set the texture parameters, can be GL_LINEAR or GL_NEAREST
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //upload texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);
        return new Texture(id);
    }

}
