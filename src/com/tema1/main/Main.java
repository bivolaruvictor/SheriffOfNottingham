package com.tema1.main;
import com.tema1.common.Constants;
import com.tema1.Players.Player;
import fileio.FileSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        List<Player> players = new ArrayList<>(gameInput.getPlayerNames().size());
        //TODO implement homework logic
        //TODO Open input and output file
        try {
            FileSystem fs = new FileSystem(args[0], args[1]);
            for (int i = 0; i < gameInput.getPlayerNames().size(); ++i) {
                players.add(new Player(gameInput.getPlayerNames().get(i), i,
                        Constants.STARTING_BUDGET));
                players.get(i).strategySet();
            }

            int whereAmI = 0;

            for (int j = 0; j < gameInput.getRounds(); j++) {
                for (int k = 0; k < gameInput.getPlayerNames().size(); ++k) {
                    fs.writeWord("Runda " + (j + 1) + " , " + "Subrunda " + (k + 1));
                    fs.writeNewLine();
                    players.get(k).makeSheriff();
                    // POATE E O PROBLEMA
                    players.get(k).setRound(j + 1);
                    for (Player gamer : players) {
                        // POATE E O PROBLEMA
                        gamer.setRound(j + 1);
                        if (!gamer.isSheriff()) {
                            List<Integer> cards = new ArrayList<>(Constants.HAND_SIZE);
                            for (int q = whereAmI; q < whereAmI + Constants.HAND_SIZE; ++q) {
                                cards.add(gameInput.getAssetIds().get(q));
                            }
                            gamer.putInHand(cards);
                            gamer.makeBag();
                            whereAmI += Constants.HAND_SIZE;
                        }
                    }

                    for (Player player : players) {
                        if (!player.isSheriff()) {
                            fs.writeWord("Ce are " + " player " + player.getId() + " in mana :");
                            fs.writeWord(player.showHand());
                            fs.writeNewLine();
                            fs.writeNewLine();
                            fs.writeWord("Ce isi pune " + " player "
                                    + player.getId() + " in sac :");
                            fs.writeWord(player.showBag());
                            fs.writeNewLine();
                            fs.writeWord("Declara ca are ");
                            fs.writeInt(player.getDeclaredGoodsId());
                            fs.writeNewLine();
                        } else {
                            fs.writeNewLine();
                            fs.writeWord("Player " + player.getId()
                                    + " spune : N-am carti ca sunt serif");
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

            for (Player player : players) {
                player.giveBonuses();

            }

            fs.writeNewLine();
            fs.writeNewLine();
            fs.writeWord("--------------");
            fs.writeNewLine();
            fs.writeWord("La final de joc");
            fs.writeNewLine();
            for (Player player : players) {
                fs.writeWord("Jucatorul " + player.getId() + " are pe taraba : " + player.showMarket());
                fs.writeNewLine();
            }
            for (Player player : players) {
                fs.writeWord("Jucatorul " + player.getId() + " are " + player.getBudget() + " bani");
                fs.writeNewLine();
            }

            for (Player player : players) {
                player.giveBonuses();
                if (player.getId() == 1) {
                    player.getKingGoods().add(24);
                }
            }

            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
