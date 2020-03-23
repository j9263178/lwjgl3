package Render;

import static org.lwjgl.opengl.GL20.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Shader {
    private int program;
    private static String path="/Users/joseph/lwjgl3/src/main/resources/shaders/";

    public Shader(String filename) {

        program = glCreateProgram();

        int vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs,readFile(filename+".vs"));
        glCompileShader(vs);

        if(glGetShaderi(vs,GL_COMPILE_STATUS)!=1) {
            System.err.println(glGetShaderInfoLog(vs,100));
            System.exit(1);
        }

        int fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs,readFile(filename+".fs"));
        glCompileShader(fs);

        if(glGetShaderi(fs,GL_COMPILE_STATUS)!=1) {
            System.err.println(glGetShaderInfoLog(fs,100));
            System.exit(1);

        }

        glAttachShader(program, vs);

        glAttachShader(program, fs);

        glBindAttribLocation(program,0,"vertices");
        glBindAttribLocation(program,1,"textures");
        glBindAttribLocation(program,2,"color");

        glLinkProgram(program);

        if(glGetProgrami(program,GL_LINK_STATUS)!= 1) {
            System.err.println(glGetProgramInfoLog(program,100));
            System.exit(1);
        }

        glValidateProgram(program);

        if(glGetProgrami(program,GL_VALIDATE_STATUS)!= 1) {
            System.err.println(glGetProgramInfoLog(program,100));
            System.exit(1);
        }

    }

    public void setUniform(String name,float value) {
        int location = glGetUniformLocation(program , name);
        if(location != -1) {
            glUniform1f(location,value);

        }else {System.out.print(-1);}
    }

    public void setUniform(String name,Matrix4f value) {
        int location = glGetUniformLocation(program , name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        glUniformMatrix4fv(location, false, buffer);
    }

    public void bind() {
        glUseProgram(program);
    }

    public void unbind() {
        glUseProgram(0);
    }

    private String readFile(String filename) {
        StringBuilder string = new StringBuilder();
        BufferedReader br ;
        try {
            br = new BufferedReader(new FileReader(new File(path + filename)));
            String line;

            while((line = br.readLine())!=null) {
                string.append(line);
                string.append("\n");
            }

            br.close();

        }catch(IOException e) {
            e.printStackTrace();
        }

        return string.toString();
    }
}

