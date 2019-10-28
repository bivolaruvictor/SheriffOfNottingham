package com.tema1.main;
import fileio.FileSystem;

import java.io.IOException;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        try {
            FileSystem fs = new FileSystem(args[0], args[1]);
            for (int i = 0; i < gameInput.getPlayerNames().size(); ++i) {
                fs.writeWord(gameInput.getPlayerNames().get(i));
            }
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO implement homework logic
    }
}
