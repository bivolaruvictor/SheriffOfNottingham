package com.tema1.Players;

import com.tema1.common.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicPlayer extends Player {
    private PlayersType type;

    public PlayersType getType() {
        return type;
    }

    public void setType() {
        this.type = PlayersType.basic;
    }

    public String getPlayerType() {
        return "BASIC";
    }

    public BasicPlayer(final int id, final int budget, final int numPlayers) {
        super(id, budget, numPlayers);
        setType();
    }

    public void makeBag() {
        arrangeCards();
        emptyBag();
        if (getSortedLegals().size() != 0) {
            int declaredGood = getSortedLegals().get(0);
            for (Integer legal : getSortedLegals()) {
                if (legal == declaredGood) {
                    if (getBag().size() < Constants.BAG_SIZE) {
                        getBag().add(legal);
                    }
                }
            }
            declareGoodsId(declaredGood);
        } else {
            emptyBag();
            List<Integer> dishonest = getSortedIllegals();
            ProfitComparator comp = new ProfitComparator();
            Collections.sort(dishonest, comp);
            getBag().add(dishonest.get(0));
            declareGoodsId(0);
        }
    }

    public void controlPlayers(final List<Player> players) {
        clearConfiscated();
        emptyBag();
        for (Player player : players) {
            List<Integer> legalGoodsToAdd = new ArrayList<>();
            List<Integer> control = new ArrayList<>(player.getBag());
            Integer whatIsDeclared = player.getDeclaredGoodsId();
            for (Integer good : control) {
                if (!good.equals(whatIsDeclared)) {
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
            player.emptyBag();
            player.clearConfiscated();
        }
    }
}
