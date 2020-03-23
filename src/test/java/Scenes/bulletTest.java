package Scenes;

import java.util.*;
import Render.*;
import GUI.Timer;
import GUI.*;
import Entity.*;
import org.joml.Matrix2f;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class bulletTest implements Scene {
    Random r;
    private Entity main;
    private Shader shader;
    private double fps=55;
    private Input input;
    private ArrayList<Entity> entitieList;
    private ArrayList<Entity> life;
    private ArrayList<Timer> timers;
    private ArrayList<Entity> bulletManager;
    private Camera mainCamera,testc;
    private Sheet s4bullet;
    private int stateCounter,currentState;
    private boolean dead,fire;
    private Animation animation;

    float[][] circleBulletsPos={{0,-0.2f},{0.13f,-0.14f},{0.20f,0f},{0.13f,0.14f},{0,0.2f},{-0.13f,0.14f},{-0.20f,0f},{-0.13f,-0.14f}};
    int[] stateTime={10,10,1,30};

    //float[][] circleBulletsPos={{0,-0.2f}};
    public bulletTest(long window){
        input = new Input(window);
        init();
    }

    public void handleInput(Entity hi){

        if (input.isKeyReleased(GLFW_KEY_D)){
            hi.addVel(-0.2f,0);}

        if (input.isKeyPressed(GLFW_KEY_D)){
            hi.addVel(0.2f,0);}

        if (input.isKeyReleased(GLFW_KEY_A)){
            hi.addVel(0.2f,0);}

        if (input.isKeyPressed(GLFW_KEY_A)){
            hi.addVel(-0.2f,0);}

        if (input.isKeyReleased(GLFW_KEY_S)){
            hi.addVel(0,0.2f);}

        if (input.isKeyPressed(GLFW_KEY_S)){
            hi.addVel(0,-0.2f);}

        if (input.isKeyReleased(GLFW_KEY_W)){
            hi.addVel(0f,-0.2f);}

        if (input.isKeyPressed(GLFW_KEY_W)){
            hi.addVel(0,0.2f);}

        if (input.isKeyPressed(GLFW_KEY_SPACE)){
            if(life.isEmpty()){
                animation = new Animation(0f,0f,0.6f,0.3f,new Sheet("hihi5",12,1),shader);
                for(int i=0;i<7;i++){
                    life.add(new Entity(-1.4f+0.15f*i,-0.6f,0.15f,testc,shader,
                            new Sheet("life",7,1)));
                    hi.visible=true;
                    dead=false;
                }
            }
        }

    }

    @Override
    public void render() {

        handleInput(main);
        input.update();

        timers.get(0).update();
        if (timers.get(0).isReset()) {
            for(Entity e :entitieList) if(!e.stopAnimation) e.nextFrame();
            for(Entity e:life){
                e.nextFrame();
            }
            for(Entity e:bulletManager){
                if(!e.stopAnimation) e.nextFrame();
            }
        }
        timers.get(0).reset();

        timers.get(1).update();
        if(timers.get(1).isReset()){
            stateCounter++;
            if(fire) {
                switch (currentState) {
                    case 0:
                    for (float[] pos : circleBulletsPos) {
                        Entity tem = new Entity(10f * pos[0], 10f * pos[1], 0.13f, mainCamera, shader,
                                s4bullet);
                        tem.setAcc(0.018f * (main.getPos().x - tem.getPos().x), 0.018f * (main.getPos().y - tem.getPos().y));
                        bulletManager.add(tem);
                    }

                    break;
                    case 3:
                        Rotate(circleBulletsPos);
                        for (float[] pos : circleBulletsPos) {
                            Entity tem = new Entity(1f * pos[0], 1f * pos[1], 0.13f, mainCamera, shader,
                                    s4bullet);
                            tem.setVel(4f * pos[0], 4f *pos[1]);
                            bulletManager.add(tem);
                        }
                        Rotate(circleBulletsPos);
                        for (float[] pos : circleBulletsPos) {
                            Entity tem = new Entity(1f * pos[0], 1f * pos[1], 0.13f, mainCamera, shader,
                                    s4bullet);
                            tem.setVel(3.3f * pos[0], 3.3f *pos[1]);
                            bulletManager.add(tem);
                        }
                        Rotate(circleBulletsPos);
                        for (float[] pos : circleBulletsPos) {
                            Entity tem = new Entity(1f * pos[0], 1f * pos[1], 0.13f, mainCamera, shader,
                                    s4bullet);
                            tem.setVel(3.0f * pos[0], 3.0f *pos[1]);
                            bulletManager.add(tem);
                        }
                    break;
                }
            }
        }
        timers.get(1).reset();


        if(dead){
            fire=false;
            stateCounter = 0;
            currentState = 1;
        }

        if(stateCounter>stateTime[currentState]){
            stateCounter=0;
            switch (currentState){
                case 0:
                case 3:
                    currentState = 1;
                    fire=false;
                    break;
                case 1:
                    while(!bulletManager.isEmpty()) bulletManager.clear();
                    currentState = 2;
                    break;
                case 2:
                    fire = true;
                    int[] temp ={3,0};
                    currentState = temp[r.nextInt(2)];
                    break;
            }
        }

        //update and draw character
        for(Entity e :entitieList){
            if(e.visible){
                e.update();
                e.draw();}
        }
        //update and draw bullets
        for(Entity e :bulletManager){
            e.update();
            if(e.visible)  e.draw();
        }
        //judge
        for(Entity e :bulletManager){
                if(Math.abs(e.getPos().x-main.getPos().x)<0.042f &&
                        Math.abs(e.getPos().y-main.getPos().y)<0.042f){
                    if(!life.isEmpty()){
                        e.visible=false;
                        life.remove(life.size()-1);}
                }
        }

        //update and draw GUI
        if(life.isEmpty()) {main.visible=false; dead=true;}
        for(Entity e:life){
            e.draw();
        }

        if(animation!=null&&!(animation.finish)){
            animation.draw();
        }

    }

    @Override
    public void init() {
        dead=false;
        Sheet s4life=new Sheet("life",7,1);
        s4bullet = new Sheet("test2", 7, 1);

        shader = new Shader("cs");
        mainCamera =new Camera(800,600);
        testc = new Camera(800,400);

        Entity hi2 = new Entity(0f,-0.3f,0.3f,mainCamera,shader,
                new Sheet("test",11,1));

        Entity hi = new Entity(0f, 0f, 0.5f, mainCamera, shader,
                s4bullet);

        entitieList =new ArrayList<>();
        timers =new ArrayList<>();

        timers.add(new Timer(fps));
        timers.add(new Timer(3));
        timers.add(new Timer(1));

        bulletManager = new ArrayList<>();

        main = hi2;
        entitieList.add(hi2);
        entitieList.add(hi);
        stateCounter = 0;
        currentState = 0;
        fire = true;
        life = new ArrayList<>();
        for(int i=0;i<14;i++){
            life.add(new Entity(-1.4f+0.15f*i,-0.6f,0.15f,testc,shader,
                    s4life));
        }
        r=new Random();
    }

    private void Rotate(float[][] in){
        for(float[] vec:in){
            Matrix2f rot=new Matrix2f().rotate(0.1f);
            Vector2f tem=new Vector2f(vec[0],vec[1]);
            vec[0]=tem.mul(rot).get(0);
            vec[1]=tem.mul(rot).get(1);
        }
    }
}
