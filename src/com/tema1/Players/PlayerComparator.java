package com.tema1.Players;


import java.util.Comparator;
import java.util.Map;

public class PlayerComparator implements Comparator<Player> {


    public final int compare(final Player p1, final Player p2) {
        int money1 = p1.getBudget();
        int money2 = p2.getBudget();
        if (money1 == money2) {
            return (p1.getId() < p2.getId()) ? 1 : ((p1.getId() == p2.getId()) ? 0 : -1);
        } else {
            return  (money1 < money2) ? 1 : -1;
        }
    }
}

