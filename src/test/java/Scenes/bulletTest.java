package Scenes;
import Font.EasyFont;
import static MainGame.GlobalObjects.*;
import static MainGame.HelloWorld.*;
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

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class bulletTest implements Scene {
    private Random r;
    private TileMap fuck;
    private Entity main;
    private ArrayList<Entity> entitieList,bulletManager;
    private ArrayList<Timer> timers;
    private Sheet s4bullet,s4m;
    private int stateCounter,currentState;
    private boolean fire;
    private Animation animation;
    EasyFont shit;

    public bulletTest(long window) throws IOException {
        init();
    }
    @Override
    public void render() {


        glEnable(GL_TEXTURE_2D);
        MainShader.bind();

        mainCamera.getPosition().lerp(new Vector3f(0.64f*main.getPos().x,0.86f*main.getPos().y,0).mul(-1,new Vector3f()), 0.07f);
        MainShader.setUniform("projection",mainCamera.getProjection().scale(256));

        s4m.bind();
        fuck.render();

        onBossStates();
        onBossAttack();
        onPlayerDamage();

        onCollision();
        onEntitiesDraw();
        onAnimations();

        MainShader.setUniform("projection",guiCamera.getProjection().scale(256));
        onGUIupdate();

        MainShader.unbind();


        glDisable(GL_TEXTURE_2D);
        shit.loop();

        onInput();
        MainInput.update();

    }
    @Override
    public void init() throws IOException {
        shit= new EasyFont(window);

        dead=false;
        s4bullet = new Sheet("test2", 7, 1,MainShader);
        s4m =new Sheet("pixil-frame-0",32,18,MainShader);


        Entity hi2 = new Entity(0.2f,-0.4f,0.3f,
                player);

        main = hi2;

        Entity hi = new Entity(0f, 0f, 0.3f,
                s4bullet);

        entitieList =new ArrayList<>();
        timers =new ArrayList<>();
        bulletManager = new ArrayList<>();

        timers.add(new Timer(fps));
        timers.add(new Timer(3));
        timers.add(new Timer(1));

        entitieList.add(hi);
        entitieList.add(hi2);

        stateCounter = 0;
        currentState = 0;
        fire = true;

        r=new Random();

        fuck =new TileMap(32,18,s4m,0.1f,mainCamera,main);
        fuck.loadMap("/Users/joseph/lwjgl3/src/test/java/Map/h.txt");

    }
    public void onInput(){

        if (MainInput.isKeyReleased(GLFW_KEY_D)){
            main.addVel(-0.1f,0);}

        if (MainInput.isKeyPressed(GLFW_KEY_D)){
            main.addVel(0.1f,0);}

        if (MainInput.isKeyReleased(GLFW_KEY_A)){
            main.addVel(0.1f,0);}

        if (MainInput.isKeyPressed(GLFW_KEY_A)){
            main.addVel(-0.1f,0);}

        if (MainInput.isKeyReleased(GLFW_KEY_S)){
            main.addVel(0,0.1f);}

        if (MainInput.isKeyPressed(GLFW_KEY_S)){
            main.addVel(0,-0.1f);}

        if (MainInput.isKeyReleased(GLFW_KEY_W)){
            main.addVel(0f,-0.1f);}

        if (MainInput.isKeyPressed(GLFW_KEY_W)){
            main.addVel(0,0.1f);}

        if (MainInput.isKeyPressed(GLFW_KEY_SPACE)){
                //animation = new Animation(0f,0f,0.6f,0.3f,new Sheet("hihi5",12,1),MainShader);
                gui.onRecover(1);
                main.visible=true;
                dead=false;
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
                            Entity tem = new Entity(10f * pos[0], 10f * pos[1], 0.13f,
                                    s4bullet);
                            tem.setAcc(0.018f * (main.getPos().x - tem.getPos().x), 0.018f * (main.getPos().y - tem.getPos().y));
                            bulletManager.add(tem);
                        }

                        break;
                    case 3:
                        Rotate(circleBulletsPos);
                        for (float[] pos : circleBulletsPos) {
                            Entity tem = new Entity(1f * pos[0], 1f * pos[1], 0.13f, s4bullet);
                            tem.setVel(4f * pos[0], 4f *pos[1]);
                            bulletManager.add(tem);
                        }
                        Rotate(circleBulletsPos);
                        for (float[] pos : circleBulletsPos) {
                            Entity tem = new Entity(1f * pos[0], 1f * pos[1], 0.13f,
                                    s4bullet);
                            tem.setVel(3.3f * pos[0], 3.3f *pos[1]);
                            bulletManager.add(tem);
                        }
                        Rotate(circleBulletsPos);
                        for (float[] pos : circleBulletsPos) {
                            Entity tem = new Entity(1f * pos[0], 1f * pos[1], 0.13f,
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
                if(e.damageble){
                    e.visible=false;
                    e.damageble=false;
                    gui.onDamaged(1);
                }
            }
        }
    }
    public void onCollision(){

        Entity a=main;
        Entity b=entitieList.get(0);
        if(a.box.test(b.box)){
            //System.out.println("FUCK");
            Vector2f posa=a.getPos();
            Vector2f posb=b.getPos();
            Vector2f dis=new Vector2f();
            posa.sub(posb,dis);
            if(Math.abs(dis.x)>Math.abs(dis.y)){
                if(posa.x>posb.x)
                    posa.x=posb.x+b.d;
                else if(posa.x<posb.x){
                    posa.x=posb.x-b.d;}
            }
            else if (Math.abs(dis.x)<Math.abs(dis.y)){
                if(posa.y>posb.y)
                    posa.y=posb.y+b.d;
                else if(posa.y<posb.y){
                    posa.y=posb.y-b.d;}
            }

        }
       // System.out.println(fuck.Solids.size());
        for(Tile t: fuck.Solids){
            if(a.box.test(t.box)){
              //  System.out.println("FUCK");
                Vector2f posa=a.getPos();
                Vector2f posb=t.pos;
                Vector2f dis=new Vector2f();
                posa.sub(posb,dis);
                if(Math.abs(dis.x)>Math.abs(dis.y)){
                    if(posa.x>posb.x)
                        posa.x=posb.x+2*t.d;
                    else if(posa.x<posb.x){
                        posa.x=posb.x-2*t.d;}
                }
                else if (Math.abs(dis.x)<Math.abs(dis.y)){
                    if(posa.y>posb.y)
                        posa.y=posb.y+2*t.d;
                    else if(posa.y<posb.y){
                        posa.y=posb.y-2*t.d;}
                }
            }
        }

        for(int i=0;i<entitieList.size()-1;i++){
            for(int j=i+1;j<entitieList.size();j++){
            }
        }
    }
    public void onEntitiesDraw(){
        //update the y position order
       // Collections.sort(entitieList);
        timers.get(0).update();
        if (timers.get(0).isReset()) {
            for(Entity e :entitieList) if(!e.stopAnimation) e.nextFrame();
            for(Entity e:gui.getLife()){
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
                e.draw();
            }
        }

        //update and draw bullets
        for(Entity e :bulletManager){
            e.update();
            if(e.visible)  e.draw();
        }

    }
    public void onGUIupdate(){
        gui.draw();
        if(gui.getLife().isEmpty()) {main.visible=false; dead=true;}
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
