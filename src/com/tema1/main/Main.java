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
                    players.get(k).makeSheriff();
                    for (Player gamer : players) {
                        if (!gamer.isSheriff()) {
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
                        if (!player.isSheriff()) {
                            fs.writeWord("Ce are " + " player " + player.getId() + " in mana :");
                            fs.writeWord(player.showHand());
                            fs.writeNewLine();
                            fs.writeNewLine();
                            fs.writeWord("Ce isi pune " + " player " + player.getId() + " in sac :");
                            fs.writeWord(player.showBag());
                        } else {
                            fs.writeNewLine();
                            fs.writeWord("Player " + player.getId() + " spune : N-am carti ca sunt serif");
                            fs.writeNewLine();
                            player.controlPlayers(players);
                        }
                    }
                    fs.writeNewLine();
                    fs.writeWord("--------------------------");
                    fs.writeNewLine();
                    for (Player player : players) {
                        fs.writeWord("Player " + player.getId() + " are ");
                        fs.writeWord(player.showMoney());
                        fs.writeWord(" bani ");
                        fs.writeNewLine();
                    }
                    players.get(k).makeMerchant();
                }
            }

//            for (Player player : players) {
//                player.giveBonuses();
//
//            }

            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
