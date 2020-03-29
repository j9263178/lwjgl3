package MainGame;
import GUI.GUI;
import GUI.Input;
import Render.Camera;
import Render.Shader;
import Render.Sheet;

public class GlobalObjects {

    public static Shader MainShader=new Shader("cs");
    public static double fps=60;
    public static Input MainInput;
    public static Camera mainCamera=new Camera(800,600),
                         guiCamera=new Camera(800,400);
    public static  Sheet player=new Sheet("test",11,1,MainShader);
    public static float[][] circleBulletsPos={{0,-0.2f},{0.13f,-0.14f},{0.20f,0f},{0.13f,0.14f},{0,0.2f},{-0.13f,0.14f},{-0.20f,0f},{-0.13f,-0.14f}};
    public static int[] stateTime={10,10,1,30};
    public static boolean dead;
    public static GUI gui=new GUI();

    public static void initObj(long window){
        MainInput=new Input(window);
    }
}
