package com.tema1.Players;

import com.tema1.common.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GreedyPlayer extends BasicPlayer {
    private PlayersType type;


    @Override
    public PlayersType getType() {
        return type;
    }

    @Override
    public final void setType() {
        this.type = PlayersType.greedy;
    }

    public final String getPlayerType() {
        return "GREEDY";
    }

    public GreedyPlayer(final int id, final int budget, final int numPlayers) {
        super(id, budget, numPlayers);
        setType();
    }

    public final void makeBag() {
        arrangeCards();
        ProfitComparator cmp = new ProfitComparator();
        if (getRound() % 2 == 1) {
            /* In rundele impare se comporta ca un basic*/
            super.makeBag();
        } else {
            super.makeBag();
            /*In rundele pare dupa ce isi face sacul ca un jucator basic,
            incearca sa isi mai puna un bun ilegal cu profitul maxim*/
            if (getBag().size() < Constants.BAG_SIZE) {
                if (!getBag().isEmpty()) {
                    /*In cazul in care si-a pus deja un bun ilegal pe taraba
                    dupa strategia de basic, scoatem acel bun din vectorul de bunuri ilegale
                    pe care il sortam, de aceasta data doar in functie de profit*/
                    if (getBag().get(0) > Constants.MAX_LEGAL_INDEX) {
                        getSortedIllegals().remove(getBag().get(0));
                        if (!getSortedIllegals().isEmpty()) {
                            Collections.sort(getSortedIllegals(), cmp);
                            getBag().add(getSortedIllegals().get(0));
                        }
                    } else {
                        /* Altfel punem primul bun din vectorul de ilegale,
                        dupa ce il sortam in functie de profit*/
                        if (!getSortedIllegals().isEmpty()) {
                            Collections.sort(getSortedIllegals(), cmp);
                            getBag().add(getSortedIllegals().get(0));
                        }
                    }
                }
            }
        }
    }

    public final void controlPlayers(final List<Player> players) {
        clearConfiscated();
        emptyBag();

        for (Player player : players) {
            List<Integer> legalGoodsToAdd = new ArrayList<>();
            List<Integer> control = player.getBag();
            Integer whatIsDeclared = player.getDeclaredGoodsId();
            if (player.getBribe() == 0) {
                /* Daca nu da mita il controleaza cum ar face-o un jucator basic*/
                for (Integer good : control) {
                    if (good != whatIsDeclared) {
                        getConfiscated().add(good);
                    } else {
                        legalGoodsToAdd.add(good);
                    }
                }
                player.addToStore(legalGoodsToAdd);
                if (getConfiscated().size() == 0) {
                    int sum = player.getBag().size()
                            * getAllGoods().get(getDeclaredGoodsId()).getPenalty();
                    pay(player, sum);
                    if (legalGoodsToAdd.size() == 0) {
                        player.addToStore(player.getBag());
                    }
                    player.emptyBag();
                } else {
                    int sum = 0;
                    for (Integer item : getConfiscated()) {
                        sum += getAllGoods().get(item).getPenalty();
                    }
                    getPaid(player, sum);
                    player.emptyBag();
                    setToAdd(addConfiscatedToDeck(player));
                }
            } else {
                /* Daca da mita, ii ia mita si il lasa sa treaca fara sa verifice*/
                takeBribe(player, player.getBribe());
                player.addToStore(player.getBag());
                player.resetBribe();
            }
            player.emptyBag();
            player.clearConfiscated();
        }
    }
}
