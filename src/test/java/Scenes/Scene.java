package Scenes;

import Entity.Entity;
import GUI.Timer;
import Render.Camera;
import Render.Shader;
import Render.Sheet;

import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public interface Scene {
    void render();
    void init();
    void onInput();
    void onBossStates();
    void onBossAttack();
    void onPlayerDamage();
    void onEntitiesDraw();
    void onGUIupdate();
    void onAnimations();
}