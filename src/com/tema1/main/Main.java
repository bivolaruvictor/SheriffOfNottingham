package com.tema1.main;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.goods.LegalGoods;
import fileio.FileSystem;

import java.io.IOException;
import java.util.Map;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        //TODO implement homework logic

        //TODO Open input and output file
        try {
            FileSystem fs = new FileSystem(args[0], args[1]);
            for (int i = 0; i < gameInput.getPlayerNames().size(); ++i) {
                fs.writeWord(gameInput.getPlayerNames().get(i));
                fs.writeNewLine();
            }

            for (int i = 0; i < gameInput.getAssetIds().size(); ++i) {
                fs.writeInt(gameInput.getAssetIds().get(i));
                fs.writeNewLine();

            }

            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
