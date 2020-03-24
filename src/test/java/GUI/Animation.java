package GUI;

import Entity.Movable;
import Render.Camera;
import Render.Shader;
import Render.Sheet;

enum InMode{
    None,FlyInFromRight,FlyInFromLeft,FlyInFromTop,FlyInFromDown,ZoomIn
}
enum OutMode{
    None,FlyOutFromRight,FlyOutFromLeft,FlyOutFromTop,FlyOutFromDown,ZoomOut
}
enum Process{
    In,Display,Out
}

public class Animation extends Movable {
    Camera camera;
    Sheet sheet;
    Shader shader;
    int frame=0,scale=0;
    public boolean finish=false;
    int perFrameCounter=0,perFrameTimes,DisplayTimes,DisplayCounter=0;
    InMode inMode;
    OutMode outMode;
    Process process;

    public Animation(float x, float y, float w, float h, Sheet sheet, Shader shader) {
        super(x, y, w ,h);
        this.sheet=sheet;
        this.shader=shader;
        sheet.setShader(this.shader);
        camera = new Camera(800,600);
    }

    public void setPerFrameTimes(int t){
        this.perFrameTimes=t;
    }

    public void draw(){

        switch (process){
            case In:
                switch (inMode){
                    case None:
                        break;
                    case FlyInFromRight:
                        break;
                    case FlyInFromLeft:
                        break;
                    case FlyInFromTop:
                        break;
                    case FlyInFromDown:
                        break;
                    case ZoomIn:
                        break;
                }
                process=Process.Display;
                break;
            case Out:
                switch (outMode){
                    case None:
                        break;
                    case FlyOutFromRight:
                        break;
                    case FlyOutFromLeft:
                        break;
                    case FlyOutFromTop:
                        break;
                    case FlyOutFromDown:
                        break;
                    case ZoomOut:
                        break;
                }
                break;
            case Display:
                if(DisplayCounter++>DisplayTimes)
                    process=Process.Out;
                break;
        }

        if(perFrameCounter>perFrameTimes)
            if(frame<sheet.getXn()) frame++;
        shader.bind();
        shader.setUniform("projection",this.camera.getProjection().scale(scale));
        sheet.bind_on_frame(frame,0);
        model.render();
        shader.unbind();
    }

    private void InModeZoomIn(){
    }

    public void setInMode(InMode m){
        this.inMode=m;
    }

    public void setFlyInFromRight(OutMode m){
        this.outMode=m;
    }
}
