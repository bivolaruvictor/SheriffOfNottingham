package com.tema1.main;
import com.tema1.Players.Player;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.goods.LegalGoods;
import fileio.FileSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        List<Player> players = new ArrayList<Player>(gameInput.getPlayerNames().size());
        //TODO implement homework logic
        //TODO Open input and output file
        try {
            FileSystem fs = new FileSystem(args[0], args[1]);
            for (int i = 0; i < gameInput.getPlayerNames().size(); ++i) {
                players.add(new Player(gameInput.getPlayerNames().get(i)));
            }

            for (int i = 0; i < players.size(); ++i) {
                System.out.println(players.get(i).typeToString());
            }

            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
