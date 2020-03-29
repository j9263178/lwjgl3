package Scenes;
import static MainGame.GlobalObjects.*;
import static MainGame.HelloWorld.*;

import Font.Truetype;
import Map.*;
import java.io.IOException;
import java.util.*;
import Render.*;
import GUI.Timer;
import GUI.*;
import Entity.*;
import com.sun.source.tree.TryTree;
import org.joml.Matrix2f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class bulletTest implements Scene {
    private Random r;
    private ArrayList<Entity> bulletManager;
    private ArrayList<Timer> timers;
    private Sheet s4bullet,s4m;
    private int stateCounter,currentState;
    private boolean fire;

    public bulletTest(long window) throws IOException {
        init();
    }
    @Override
    public void render() {

        MainShader.bind();

        mainCamera.getPosition().lerp(new Vector3f(0.64f*FocusedPlayer.getPos().x,0.86f*FocusedPlayer.getPos().y,0).mul(-1,new Vector3f()), 0.07f);
        MainShader.setUniform("projection",mainCamera.getProjection().scale(256));

        s4m.bind();
        CurrentTileMap.render();

     //   onBossStates();
     //   onBossAttack();
    //    onPlayerDamage();
    //    onCollision();
        onEntitiesDraw();
        onAnimations();


        MainShader.setUniform("projection",guiCamera.getProjection().scale(256));
        onGUIupdate();
        TestImage.draw();
        MainShader.unbind();

        for(Truetype t:CurrentText){
            t.loop();
        }
        onInput();
        MainInput.update();

    }
    @Override
    public void init() throws IOException {
        TestImage=new Entity(1.2f,-1f,2.5f,new Sheet("69992",1,1,MainShader));
        Truetype title=new Truetype(30);
        title.yo=1200f;
        title.setText("Fusi");
        CurrentText.add(title);
        Truetype context=new Truetype(24);
        context.yo=1300f;
        context.setText("Djowqwefcuhwㄙhuasdhisa\b\nIDHUH@&#)!@*#)");
        CurrentText.add(context);

        gui.visible=false;

        dead=false;
        s4bullet = new Sheet("a_ln_t(已去底)", 3, 4,MainShader);
        s4m =new Sheet("pixil-frame-0",32,18,MainShader);

        Entity hi2 = new Entity(0.2f,-0.4f,0.3f,0.4f,
                player);
        MainPlayer = hi2;
        FocusedPlayer=MainPlayer;

        Entity hi = new Entity(0f, 0f, 0.3f,0.4f,
                s4bullet);
        hi.setPose(2);
        TestPlayer = hi;

        CurrentEntities =new ArrayList<>();
        timers =new ArrayList<>();
        bulletManager = new ArrayList<>();

        timers.add(new Timer(5));
        timers.add(new Timer(3));
        timers.add(new Timer(1));

        CurrentEntities.add(hi);
        CurrentEntities.add(hi2);

        stateCounter = 0;
        currentState = 0;
        fire = true;

        r=new Random();

        TileMap fuck =new TileMap(32,18,s4m,0.1f,mainCamera,MainPlayer);
        fuck.loadMap("/Users/joseph/lwjgl3/src/test/java/Map/h.txt");
        CurrentTileMap=fuck;

    }
    public void onInput(){

        if (MainInput.isKeyDown(GLFW_KEY_S)){
            MainPlayer.setPose(2);
            MainPlayer.addPos(0,-0.01f);}
        else if (MainInput.isKeyDown(GLFW_KEY_W)){
            MainPlayer.setPose(0);
            MainPlayer.addPos(0,0.01f);}
        else if (MainInput.isKeyDown(GLFW_KEY_A)){
            MainPlayer.setPose(3);
            MainPlayer.addPos(-0.01f,0f);}
        else if (MainInput.isKeyDown(GLFW_KEY_D)){
            MainPlayer.setPose(1);
            MainPlayer.addPos(0.01f,0f);}

        if (MainInput.isKeyPressed(GLFW_KEY_SPACE)){
                //animation = new Animation(0f,0f,0.6f,0.3f,new Sheet("hihi5",12,1),MainShader);
                gui.onRecover(1);
                MainPlayer.visible=true;
                dead=false;
                CurrentText.clear();
        }
        if (MainInput.isKeyPressed(GLFW_KEY_J)){
            FocusedPlayer=MainPlayer;
        }
        if (MainInput.isKeyPressed(GLFW_KEY_K)){
            FocusedPlayer=TestPlayer;
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
                            tem.setAcc(0.018f * (MainPlayer.getPos().x - tem.getPos().x), 0.018f * (MainPlayer.getPos().y - tem.getPos().y));
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
            if(Math.abs(e.getPos().x-MainPlayer.getPos().x)<0.042f &&
                    Math.abs(e.getPos().y-MainPlayer.getPos().y)<0.042f){
                if(e.damageble){
                    e.visible=false;
                    e.damageble=false;
                    gui.onDamaged(1);
                }
            }
        }
    }
    public void onCollision(){

        Entity a=MainPlayer;
        Entity b=CurrentEntities.get(0);
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
        for(Tile t: CurrentTileMap.Solids){
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

        for(int i=0;i<CurrentEntities.size()-1;i++){
            for(int j=i+1;j<CurrentEntities.size();j++){
            }
        }
    }
    public void onEntitiesDraw(){
        //update the y position order
       // Collections.sort(CurrentEntities);
        timers.get(0).update();
        if (timers.get(0).isReset()) {
            for(Entity e :CurrentEntities) if(!e.stopAnimation) e.nextFrame();
            for(Entity e:gui.getLife()){
                e.nextFrame();
            }
            for(Entity e:bulletManager){
                if(!e.stopAnimation) e.nextFrame();
            }
        }
        timers.get(0).reset();

        //update and draw character
        for(Entity e :CurrentEntities){
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
        if(gui.visible) gui.draw();
        if(gui.getLife().isEmpty()) {MainPlayer.visible=false; dead=true;}
    }
    public void onAnimations(){
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
