package MainGame;
import Entity.Entity;
import Font.Truetype;
import GUI.*;
import Map.TileMap;
import Render.Camera;
import Render.Shader;
import Render.Sheet;
import Scenes.Scene;

import java.awt.*;
import java.security.PublicKey;
import java.util.ArrayList;

public class GlobalObjects {
    /**-----------------render-------------------------**/
    public static long window;
    public static Shader MainShader=new Shader("cs");
    public static double fps=60;
    public static Camera mainCamera=new Camera(800,600),
                         guiCamera=new Camera(800,600);
    /**-----------------Sheets-------------------------**/
    public static Sheet player=new Sheet("星野(已去底)",3,4,MainShader);
    /**-----------------Entities-------------------------**/
    public static ArrayList<Entity> CurrentEntities=new ArrayList<>();
    public static ArrayList<Truetype> CurrentText=new ArrayList<>();
    public static ArrayList<Animation> CurrentAnime=new ArrayList<>();
    public static Entity MainPlayer;
    public static Entity FocusedPlayer;
    public static Entity TestPlayer;
    /**-----------------TileMaps-------------------------**/
    public static TileMap CurrentTileMap;
    /**-----------------PlayerStates-------------------------**/
    public static boolean dead;
    /**-----------------UI-------------------------**/
    public static GUI gui=new GUI();
    public static Input MainInput;
    /**-----------------Constants-------------------------**/
    public static float[][] circleBulletsPos={{0,-0.2f},{0.13f,-0.14f},{0.20f,0f},{0.13f,0.14f},{0,0.2f},{-0.13f,0.14f},{-0.20f,0f},{-0.13f,-0.14f}};
    public static int[] stateTime={10,10,1,30};
    /**-----------------Transitions-------------------------**/
    public static Scene CurrentScene;
    public static Scene NextScene;
    /**-----------------Images-----------------------------**/
    public static Entity TestImage;

    public static void initObj(long window){
        GlobalObjects.window = window;
        MainInput=new Input(window);
    }
}
