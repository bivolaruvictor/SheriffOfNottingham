package com.tema1.main;
import com.tema1.common.Constants;
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
        Constants constants = new Constants();
        //TODO implement homework logic
        //TODO Open input and output file
        try {
            FileSystem fs = new FileSystem(args[0], args[1]);
            for (int i = 0; i < gameInput.getPlayerNames().size(); ++i) {
                players.add(new Player(gameInput.getPlayerNames().get(i), i,
                        constants.STARTING_BUDGET));
                players.get(i).strategySet();
            }

            int whereAmI = 0;

            for (int j = 0; j < gameInput.getRounds(); j++) {
                for (int k = 0; k < gameInput.getPlayerNames().size(); ++k) {
                    fs.writeWord("Runda " + (j + 1) + " , " + "Subrunda " + (k + 1));
                    fs.writeNewLine();
                    players.get(k).makeSherriff();
                    for (Player gamer : players) {
                        if (!gamer.isSherriff()) {
                            List<Integer> cards = new ArrayList<>(constants.HAND_SIZE);
                            for (int q = whereAmI; q < whereAmI + constants.HAND_SIZE; ++q) {
                                cards.add(gameInput.getAssetIds().get(q));
                            }
                            gamer.putInHand(cards);
                            gamer.makeBag();
                            whereAmI += constants.HAND_SIZE;
                        }
                    }

                    for (Player player : players) {
                        if (!player.isSherriff()) {
                            fs.writeWord(player.showHand());
                            fs.writeNewLine();
                        } else {
                            fs.writeWord("N-am carti ca sunt serif");
                            fs.writeNewLine();
                        }
                    }
                    fs.writeWord("--------------------------");
                    fs.writeNewLine();
                    fs.writeNewLine();
                    players.get(k).makeMerchant();
                }
            }



            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
