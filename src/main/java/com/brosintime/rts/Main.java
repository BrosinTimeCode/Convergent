package com.brosintime.rts;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.View.GuiClient;

public class Main {

    public static void main(String[] args) {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setIdleFPS(60);
        config.useVsync(true);
        config.setTitle("Convergent");

        config.setWindowedMode(1280, 720);

        if (args.length != 0) {
            if (args[0].equals("nogui")) {
                GameController controller = new GameController();
                controller.run();
                System.exit(0);
            }
        }
        new Lwjgl3Application(new GuiClient(), config);

    }
}
