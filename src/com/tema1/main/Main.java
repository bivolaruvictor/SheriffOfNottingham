package com.tema1.main;
import com.tema1.Players.PlayerComparator;
import com.tema1.common.Constants;
import com.tema1.Players.Player;
import fileio.FileSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
                    fs.writeNewLine();
                    fs.writeWord("--------------------------");
                    fs.writeNewLine();
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
                            fs.writeWord("Ce are " + " player " + gamer.getId() + " in mana :");
                            fs.writeWord(gamer.showHand());
                            fs.writeNewLine();

                            gamer.makeBag();
                            fs.writeWord("Ce isi pune " + " player "
                                    + gamer.getId() + " in sac :");
                            fs.writeWord(gamer.showBag());
                            fs.writeNewLine();
                            fs.writeWord("Declara ca are ");
                            fs.writeInt(gamer.getDeclaredGoodsId());
                            fs.writeNewLine();

                            whereAmI += Constants.HAND_SIZE;
                        }
                    }

                    for (Player player : players) {
                        if (!player.isSheriff()) {
                            fs.writeWord("Player " + player.getId()
                                    + " spune : Nu sunt serif");
                            fs.writeNewLine();
                        } else {
                            fs.writeNewLine();
                            fs.writeWord("Player " + player.getId()
                                    + " spune : N-am carti ca sunt serif");
                            fs.writeNewLine();
                            player.controlPlayers(players);
                            player.addConfiscatedToDeck();
                        }
                    }
                    for (Player player : players) {
                        fs.writeWord("Player " + player.getId() + " are ");
                        fs.writeWord(player.showMoney());
                        fs.writeWord(" bani ");
                        fs.writeNewLine();
                    }
                    players.get(k).makeMerchant();
                }
            }

            fs.writeNewLine();
            fs.writeNewLine();
            fs.writeWord("--------------");
            fs.writeNewLine();
            fs.writeWord("La final de joc");
            fs.writeNewLine();
            for (Player player : players) {
                fs.writeWord("Jucatorul " + player.getId() + " are pe taraba : "
                        + player.showMarket());
                fs.writeNewLine();
            }
            for (Player player : players) {
                fs.writeWord("Jucatorul " + player.getId() + " are " + player.getBudget()
                        + " bani");
                fs.writeNewLine();
            }

            fs.writeNewLine();
            fs.writeWord("Dupa bonusuri de king/ queen");
            fs.writeNewLine();

            for (Player player : players) {
                player.transformStore(player.getStore());
                player.setFreq();
            }

            for (int itemId = 0; itemId <= Constants.MAX_LEGAL_INDEX; ++itemId) {
                int firstLargest = -1;
                int secondLargest = -1;
                int kingId = -1;
                int queenId = -1;
                for (int i = 0; i < players.size(); ++i) {
                    if (players.get(i).getMarketFreqMap().get(itemId) != null) {
                        if (players.get(i).getMarketFreqMap().get(itemId) > firstLargest) {
                            secondLargest = firstLargest;
                            firstLargest = players.get(i).getMarketFreqMap().get(itemId);
                            queenId = kingId;
                            kingId = i;
                        } else {
                            if (players.get(i).getMarketFreqMap().get(itemId) == firstLargest
                                    || players.get(i).getMarketFreqMap().get(itemId)
                                    > secondLargest) {
                                secondLargest = players.get(i).getMarketFreqMap().get(itemId);
                                queenId = i;
                            }
                        }
                        }
                    }
                if (kingId != -1) {
                    players.get(kingId).getKingGoods().add(itemId);
                }
                if (queenId != -1) {
                    players.get(queenId).getQueenGoods().add(itemId);
                }
            }

//            for (Player player : players) {
//                System.out.print(player.getId() + " king pe : ");
//                for (Integer item : player.getKingGoods()) {
//                    System.out.print(item + " ");
//                }
//                System.out.println();
//                System.out.print(player.getId() + " queen pe : ");
//                for (Integer item : player.getQueenGoods()) {
//                    System.out.print(item + " ");
//                }
//                System.out.println();
//            }

            for (Player player : players) {
                player.giveBonuses();
            }

            for (Player player : players) {
                fs.writeWord("Jucatorul " + player.getId() + " are " + player.getBudget()
                        + " bani");
                fs.writeNewLine();
            }

            List<Player> finalPlayers = new ArrayList<Player>(players);
            PlayerComparator cmp = new PlayerComparator();
            Collections.sort(finalPlayers,cmp);
            for (Player player : finalPlayers) {
                System.out.println(player.getId() + " " + player.getPlayerType() + " " + player.getBudget());
            }

            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
