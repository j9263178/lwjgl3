package GUI;

import Entity.Entity;
import Render.Sheet;

import java.util.ArrayList;

import static MainGame.GlobalObjects.MainShader;
import static MainGame.GlobalObjects.dead;

public class GUI {
    private ArrayList<Entity> life;
    private Sheet s4life;
    private boolean visible=true;

    public GUI(){
        s4life=new Sheet("life",7,1,MainShader);
        life=new ArrayList<>();
        for(int i=0;i<7;i++){
            life.add(new Entity(-1.4f+0.15f*i,0.6f,0.15f,
                    s4life));
        }
    }

    public void draw(){

        for(Entity e:life){
            e.draw();
        }

    }

    public void onDamaged(int times){
        while(times--!=0)
         if(!life.isEmpty()) life.remove(life.size()-1);
    }

    public void onRecover(int times){
        if(dead){
            for(int i=0;i<7;i++){
                life.add(new Entity(-1.4f+0.15f*i,0.6f,0.15f,
                        new Sheet("life",7,1,MainShader)));
            }
        }else{
            for(int i=0;i<times;i++){
                life.add(new Entity(-1.4f+0.15f*(life.size()+i),0.6f,0.15f,
                        new Sheet("life",7,1,MainShader)));
            }
        }
    }

    public ArrayList<Entity> getLife(){
        return life;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
