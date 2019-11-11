package com.tema1.Players;

import com.tema1.common.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BriberPlayer extends BasicPlayer {
    private PlayersType type;

    @Override
    public PlayersType getType() {
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

        if (getBudget() > Constants.BRIBER_LOWER_LIMIT) {
            int possibleLoss = 0;
            int numberOfGoods = 0;
            int numberIllegalGoods = 0;
            int possibleBribe = Constants.LESS_THAN_TWO_ILLEGAL_MONEY;

            if (tmpIllegals.size() != 0) {
                for (Integer item : tmpIllegals) {
                    /*Pentru fiecare bun ilegal, face verificarea*/
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

                /* Daca mai are loc in sac, incearca
                sa completeze cu bunuri legale, daca isi permite*/
                if (getBag().size() != Constants.BAG_SIZE) {
                    for (Integer item : tmpLegals) {
                        possibleLoss += getAllGoods().get(item).getPenalty();
                        if (getBudget() - possibleLoss > 0 && numberOfGoods < Constants.BAG_SIZE) {
                            getBag().add(item);
                            numberOfGoods++;
                        }
                    }
                }

                declareGoodsId(0);
                offerBribe(possibleBribe);
            } else {
                /* Daca nu are niciun bun ilegal , va proceda ca un jucator basic*/
                super.makeBag();
            }
        } else {
            /* Daca nu are suficienti bani va proceda ca un jucator basic*/
            super.makeBag();
        }
    }

    @Override
    public final void controlPlayers(final List<Player> players) {
        emptyBag();
        /* Verificarile facute vecinilor*/
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

        /* Cazul in care sunt doar 2 jucatori si vecinii sunt acelasi jucator*/
        if (unluckyPlayers.get(0).getId() == unluckyPlayers.get(1).getId()) {
            unluckyPlayers.remove(0);
            if (getBudget() >= Constants.MINIMUM_BUDGET) {
                /* Daca isi permite, atunci il verifica*/
                super.controlPlayers(unluckyPlayers);
            } else {
                /* Altfel isi pune tot pe taraba*/
                unluckyPlayers.get(0).addToStore(unluckyPlayers.get(0).getBag());
            }
        } else {
            if (getBudget() >= Constants.MINIMUM_BUDGET) {
                /* Daca isi permite, atunci ii verifica*/
                super.controlPlayers(unluckyPlayers);
            } else {
                /* Altfel isi pun tot pe taraba*/
                unluckyPlayers.get(0).addToStore(unluckyPlayers.get(0).getBag());
                unluckyPlayers.get(1).addToStore(unluckyPlayers.get(1).getBag());
            }
            for (Player player : players) {
                if (player.getId() != unluckyPlayers.get(0).getId()
                        && player.getId() != unluckyPlayers.get(1).getId()) {
                    /*Le ia celorlalti jucatori mita fara sa ii verifice*/
                    getPaid(player, player.getBribe());
                    player.addToStore(player.getBag());
                }
            }
        }
    }
}
