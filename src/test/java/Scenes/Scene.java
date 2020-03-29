package Scenes;

import java.io.IOException;
public interface Scene {
    void render();
    void init() throws IOException;
    void onInput();
    void onBossStates();
    void onBossAttack();
    void onPlayerDamage();
    void onEntitiesDraw();
    void onGUIupdate();
    void onAnimations();
}
