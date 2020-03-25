package Scenes;
import Map.*;

import java.io.IOException;
import java.util.*;
import Render.*;
import GUI.Timer;
import GUI.*;
import Entity.*;
import org.joml.Matrix2f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class bulletTest implements Scene {
    Random r;
    TileMap fuck;
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
    private float[][] circleBulletsPos={{0,-0.2f},{0.13f,-0.14f},{0.20f,0f},{0.13f,0.14f},{0,0.2f},{-0.13f,0.14f},{-0.20f,0f},{-0.13f,-0.14f}};
    private int[] stateTime={10,10,1,30};

    //float[][] circleBulletsPos={{0,-0.2f}};
    public bulletTest(long window) throws IOException {
        input = new Input(window);
        init();
    }

    @Override
    public void render() {
        fuck.render();
        mainCamera.getPosition().lerp(new Vector3f(0.64f*main.getPos().x,0.86f*main.getPos().y,0).mul(-1,new Vector3f()), 0.05f);
        onInput();
        onBossStates();
        onBossAttack();
        onPlayerDamage();
        onGUIupdate();
        onEntitiesDraw();
        onAnimations();
        input.update();
    }
    @Override
    public void init() throws IOException {
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

        fuck =new TileMap(10,10,new Sheet("鄉村之道",30,16),0.4f,shader,mainCamera);
        fuck.loadMap("/Users/joseph/lwjgl3/src/test/java/Map/h.txt");
       /* for(Tile t:fuck.getTiles()){
            System.out.println("pos:"+t.getXY()[0]+" "+t.getXY()[1]+"  id:"+t.id[0]+" "+t.id[1]+" "+ t.isSolid );
        }*/

    }
    public void onInput(){

        if (input.isKeyReleased(GLFW_KEY_D)){
            main.addVel(-0.2f,0);}

        if (input.isKeyPressed(GLFW_KEY_D)){
            main.addVel(0.2f,0);}

        if (input.isKeyReleased(GLFW_KEY_A)){
            main.addVel(0.2f,0);}

        if (input.isKeyPressed(GLFW_KEY_A)){
            main.addVel(-0.2f,0);}

        if (input.isKeyReleased(GLFW_KEY_S)){
            main.addVel(0,0.2f);}

        if (input.isKeyPressed(GLFW_KEY_S)){
            main.addVel(0,-0.2f);}

        if (input.isKeyReleased(GLFW_KEY_W)){
            main.addVel(0f,-0.2f);}

        if (input.isKeyPressed(GLFW_KEY_W)){
            main.addVel(0,0.2f);}

        if (input.isKeyPressed(GLFW_KEY_SPACE)){
            if(life.isEmpty()){
                //animation = new Animation(0f,0f,0.6f,0.3f,new Sheet("hihi5",12,1),shader);
                for(int i=0;i<7;i++){
                    life.add(new Entity(-1.4f+0.15f*i,-0.6f,0.15f,testc,shader,
                            new Sheet("life",7,1)));
                    main.visible=true;
                    dead=false;
                }
            }
        }

    }
    public void onBossStates(){
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
    }
    public void onBossAttack(){
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
    }
    public void onPlayerDamage(){
        if(dead){
            fire=false;
            stateCounter = 0;
            currentState = 1;
        }
        //judge
        for(Entity e :bulletManager){
            if(Math.abs(e.getPos().x-main.getPos().x)<0.042f &&
                    Math.abs(e.getPos().y-main.getPos().y)<0.042f){
                if(!life.isEmpty() && e.damageble){
                    e.visible=false;
                    e.damageble=false;
                    life.remove(life.size()-1);}
            }
        }
    }
    public void onEntitiesDraw(){

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

    }
    public void onGUIupdate(){
        //update and draw GUI
        if(life.isEmpty()) {main.visible=false; dead=true;}
        for(Entity e:life){
            e.draw();
        }
    }
    public void onAnimations(){
        if(animation!=null&&!(animation.finish)){
            animation.draw();
        }
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
