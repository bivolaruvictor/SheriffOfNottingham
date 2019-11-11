package com.tema1.main;
import com.tema1.Players.Player;
import com.tema1.Players.BasicPlayer;
import com.tema1.Players.BriberPlayer;
import com.tema1.Players.GreedyPlayer;
import com.tema1.Players.PlayerComparator;
import com.tema1.common.Constants;
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
        List<Player> players;
        players = new ArrayList<>(gameInput.getPlayerNames().size());
        try {
            FileSystem fs = new FileSystem(args[0], args[1]);
            for (int i = 0; i < gameInput.getPlayerNames().size(); ++i) {
                if (gameInput.getPlayerNames().get(i).equals("basic")) {
                    players.add(new BasicPlayer(i, Constants.STARTING_BUDGET,
                            gameInput.getPlayerNames().size()));
                } else {
                    if (gameInput.getPlayerNames().get(i).equals("greedy")) {
                        players.add(new GreedyPlayer(i, Constants.STARTING_BUDGET,
                                gameInput.getPlayerNames().size()));
                    } else {
                        if (gameInput.getPlayerNames().get(i).equals("bribed")) {
                            players.add(new BriberPlayer(i, Constants.STARTING_BUDGET,
                                    gameInput.getPlayerNames().size()));
                        }
                    }
                }
                players.get(i).strategySet();
            }

            int whereAmI = 0;

            for (int j = 0; j < gameInput.getRounds(); j++) {
                for (int k = 0; k < gameInput.getPlayerNames().size(); ++k) {
                    players.get(k).setRole();
                    players.get(k).setRound(j + 1);
                    for (Player gamer : players) {
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
                        if (player.isSheriff()) {
                            player.controlPlayers(players);
                        }
                    }

                    players.get(k).resetRole();
                    gameInput.getAssetIds().addAll(players.get(k).getToAdd());
                }
            }

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
                            if (players.get(i).getMarketFreqMap().get(itemId)
                                    == firstLargest && queenId == -1
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

            for (Player player : players) {
                player.giveBonuses();
            }

            List<Player> finalPlayers = new ArrayList<>(players);
            PlayerComparator cmp = new PlayerComparator();
            Collections.sort(finalPlayers, cmp);
            for (Player player : finalPlayers) {
                System.out.println(player.getId() + " "
                        + player.getPlayerType() + " " + player.getBudget());
            }
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

