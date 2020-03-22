
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class EntityMaster {

    private Entity hi,hi2;
    Shader shader,shader2;
    private double elapsedTime,currentTime,lastTime;
    private double fps=30;
    private Input input;
    private ArrayList<Entity> entitieList;
    private ArrayList<Timer> timers;
    Camera mainCamera,testc ;

    float[][] circleBulletsPos={{0,-0.2f},{0.13f,-0.14f},{0.20f,0f},{0.13f,0.14f},{0,0.2f}
    ,{-0.13f,0.14f},{-0.20f,0f},{-0.13f,-0.14f}};

    EntityMaster(long window){
        input = new Input(window);
        shader = new Shader("cs");
        shader2 = new Shader("cs");
        mainCamera =new Camera(800,600);
        testc = new Camera(800,600);

        hi = new Entity(0f,0f,0.5f,mainCamera,shader,
                new Sheet("test",11,1));

        hi2 = new Entity(0f,0f,0.5f,mainCamera,shader,
                new Sheet("test2",7,1));

        entitieList =new ArrayList<>();
        timers =new ArrayList<>();

        timers.add(new Timer(fps));
        timers.add(new Timer(1));


        //entitieList.add(hi2);
        //entitieList.add(hi);
    }

    public void Update(){

        timers.get(0).update();
        if (timers.get(0).isReset()) {
            for(Entity e :entitieList) if(!e.stopAnimation) e.nextFrame();
            //entitieList.clear();
        }
        timers.get(0).reset();


        handleInput(hi2);
        input.update();
        for(Entity e :entitieList){
            e.update();
            e.draw();
        }
        mainCamera.pos=hi2.pos;
    }

    private void handleInput(Entity hi){

        if (input.isKeyReleased(GLFW_KEY_D)){
            hi.addVel(-0.1f,0);}

        if (input.isKeyPressed(GLFW_KEY_D)){
            hi.addVel(0.1f,0);}

        if (input.isKeyReleased(GLFW_KEY_A)){
            hi.addVel(0.1f,0);}

        if (input.isKeyPressed(GLFW_KEY_A)){
            hi.addVel(-0.1f,0);}

        if (input.isKeyReleased(GLFW_KEY_S)){
            hi.addVel(0,0.1f);}

        if (input.isKeyPressed(GLFW_KEY_S)){
            hi.addVel(0,-0.1f);}

        if (input.isKeyReleased(GLFW_KEY_W)){
            hi.addVel(0f,-0.1f);}

        if (input.isKeyPressed(GLFW_KEY_W)){
            hi.addVel(0,0.1f);}
        if (input.isKeyPressed(GLFW_KEY_SPACE)){

            for(float[] pos:circleBulletsPos ){
                Entity tem = new Entity(pos[0],pos[1],0.26f,mainCamera,shader,
                        new Sheet("test2",7,1));
                tem.setVel(pos[0],pos[1]);
                entitieList.add(tem);
            }
        }

    }
}
