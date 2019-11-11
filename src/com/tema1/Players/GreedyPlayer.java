package com.tema1.Players;

import com.tema1.common.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GreedyPlayer extends BasicPlayer {
    private PlayersType type;


    @Override
    public final PlayersType getType() {
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
// TODO : Caz in care runda e impara
        if (getRound() % 2 == 1) {
            super.makeBag();
        } else {
            // TODO : Caz in care runda e para
            super.makeBag();
            if (getBag().size() < Constants.BAG_SIZE) {
                if (!getSortedIllegals().isEmpty()) {
                    List<Integer> tmp = new ArrayList<>(getSortedIllegals());
                    if (getBag().size() == 1) {
                        tmp.remove(0);
                    }
                    Collections.sort(tmp, cmp);
                    if (!tmp.isEmpty()) {
                        getBag().add(tmp.get(0));
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
                for (Integer good : control) {
                    if (good != whatIsDeclared) {
                        getConfiscated().add(good);
                    } else {
                        legalGoodsToAdd.add(good);
                    }
                }
                player.addToStore(legalGoodsToAdd);
                if (getConfiscated().size() == 0) {
                    // TODO : DA BANII MERCHANT
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
                takeBribe(player, player.getBribe());
                player.addToStore(player.getBag());
                player.resetBribe();
            }
            player.emptyBag();
            player.clearConfiscated();
        }
    }
}
