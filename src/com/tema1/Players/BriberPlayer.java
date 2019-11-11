package com.tema1.Players;

import com.tema1.common.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BriberPlayer extends BasicPlayer {
    private PlayersType type;

    @Override
    public final PlayersType getType() {
        return type;
    }

    @Override
    public final void setType() {
        this.type = PlayersType.bribed;
    }

    public final String getPlayerType() {
        return "BRIBED";
    }

    public BriberPlayer(final int id, final int budget, final int numPlayers) {
        super(id, budget, numPlayers);
        setType();
    }

    @Override
    public final void makeBag() {
        emptyBag();
        arrangeCards();
        ProfitComparator cmp = new ProfitComparator();

        List<Integer> tmpIllegals = new ArrayList<>(getSortedIllegals());
        List<Integer> tmpLegals = new ArrayList<>(getSortedLegals());

        Collections.sort(tmpIllegals, cmp);
        Collections.sort(tmpLegals, cmp);

        resetBribe();

        if (getBudget() >= Constants.BRIBER_LOWER_LIMIT) {
            int possibleLoss = 0;
            int numberOfGoods = 0;
            int numberIllegalGoods = 0;
            int possibleBribe = Constants.LESS_THAN_TWO_ILLEGAL_MONEY;

            if (tmpIllegals.size() != 0) {
                for (Integer item : tmpIllegals) {

                    // TODO : AICI > ?
                    if (getBudget() - (possibleLoss + getAllGoods().get(item).getPenalty()) > 0) {
                        if (numberIllegalGoods < Constants.BAG_SIZE) {
                            numberIllegalGoods++;
                            getBag().add(item);
                            possibleLoss += getAllGoods().get(item).getPenalty();
                        }
                    }
                }

                if (numberIllegalGoods > 2) {
                    possibleBribe = Constants.MORE_THAN_TWO_ILLEGAL_MONEY;
                }

                numberOfGoods += numberIllegalGoods;

                if (getBag().size() != Constants.BAG_SIZE) {
                    for (Integer item : tmpLegals) {
                        possibleLoss += getAllGoods().get(item).getPenalty();
                        // TODO : AICI > ?
                        if (getBudget() - possibleLoss > 0 && numberOfGoods < Constants.BAG_SIZE) {
                            getBag().add(item);
                            numberOfGoods++;
                        }
                    }
                }

                declareGoodsId(0);
                offerBribe(possibleBribe);
            } else {
                super.makeBag();
            }
        } else {
            super.makeBag();
        }
    }

    @Override
    public final void controlPlayers(final List<Player> players) {
        List<Player> unluckyPlayers = new ArrayList<>(0);
        if (getId() == getNumPlayers() - 1) {
            unluckyPlayers.add(players.get(getId() - 1));
            unluckyPlayers.add(players.get(0));
        } else {
            if (getId() == 0) {
                unluckyPlayers.add(players.get(getNumPlayers() - 1));
                unluckyPlayers.add(players.get(1));
            } else {
                unluckyPlayers.add(players.get(getId() - 1));
                unluckyPlayers.add(players.get(getId() + 1));
            }
        }

        if (unluckyPlayers.get(0).getId() == unluckyPlayers.get(1).getId()) {
            unluckyPlayers.remove(0);
            if (getBudget() >= Constants.MINIMUM_BUDGET) {
                super.controlPlayers(unluckyPlayers);
            } else {
                unluckyPlayers.get(0).addToStore(unluckyPlayers.get(0).getBag());
            }
            for (Player player : players) {
                if (player.getId() != unluckyPlayers.get(0).getId()) {
                    getPaid(player, player.getBribe());
                }
            }
        } else {
            if (getBudget() >= Constants.MINIMUM_BUDGET) {
                super.controlPlayers(unluckyPlayers);
            } else {
                unluckyPlayers.get(0).addToStore(unluckyPlayers.get(0).getBag());
                unluckyPlayers.get(1).addToStore(unluckyPlayers.get(1).getBag());
            }
            for (Player player : players) {
                if (player.getId() != unluckyPlayers.get(0).getId()
                        && player.getId() != unluckyPlayers.get(1).getId()) {
                    getPaid(player, player.getBribe());
                    player.addToStore(player.getBag());
                }
            }
        }
    }
}
