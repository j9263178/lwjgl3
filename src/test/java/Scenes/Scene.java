package Scenes;

import Entity.Entity;

public interface Scene {
    void render();
    void init();
    void handleInput(Entity entity);
}
