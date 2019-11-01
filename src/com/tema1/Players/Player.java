package com.tema1.Players;

import com.tema1.common.Constants;

import java.util.*;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;


public class Player {
    private Constants constants;
    private int id;
    private int budget;
    private String playerType;
    private String playerRole;
    private boolean isSheriff = false;
    private boolean isMerchant = false;
    private boolean isBasic = false;
    private boolean isGreedy = false;
    private boolean isBriber = false;
    private List<Integer> hand;
    private List<Integer> bag;
    private List<Integer> market;

    public Player(final String playerType, final int id, final int budget) {
        constants = new Constants();
        this.budget = budget;
        this.id = id;
        this.playerType = playerType;
        bag = new ArrayList<>(constants.BAG_SIZE);
    }

    public void putInHand(final List<Integer> chosenCards) {
        this.hand  = chosenCards;
    }

    public String showHand() {
        StringBuilder toShow = new StringBuilder();
        for (Integer integer : hand) {
            toShow.append(integer).append(" ");
        }
        return toShow.toString();
    }

    public void makeSheriff() {
        isMerchant = false;
        isSheriff = true;
    }

    public void makeMerchant() {
        isMerchant = true;
        isSheriff = false;
    }

    public boolean isSheriff() {
        return isSheriff;
    }

    public List<Integer> getHand() {
        return hand;
    }

    public boolean isMerchant() {
        return isMerchant;
    }

    public int getBudget() {
        return budget;
    }

    public String typeToString() {
        return playerType;
    }

    public void strategySet() {
        if (playerType.equals("basic")) {
            isBasic = true;
        }
        if (playerType.equals("Greedy")) {
            isGreedy = true;
        }
        if (playerType.equals("bribed")) {
            isBriber = true;
        }
    }

    public boolean isBasic() {
        return isBasic;
    }

    public boolean isGreedy() {
        return isGreedy;
    }

    public boolean isBriber() {
        return isBriber;
    }

    public void makeBag() {
        List<Integer> currentHand;
        List<Integer> illegals = new LinkedList<>();
        List<Integer> legals = new LinkedList<>();
        List<Integer> toSort;
        List<Integer> sortedIllegals = new LinkedList<>();
        List<Integer> sortedLegals = new LinkedList<>();
        currentHand = getHand();
        toSort = currentHand;
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> outputArray = new ArrayList<>();
        for (int current : toSort) {
            int count = map.getOrDefault(current, 0);
            map.put(current, count + 1);
            if (current <= constants.MAX_LEGAL_INDEX) {
                legals.add(current);
            } else {
                illegals.add(current);
            }
        }

        sortedIllegals= illegals;
        sortedLegals = legals;
        CardsComparator comp = new CardsComparator(map);
        Collections.sort(toSort, comp);
        Collections.sort(sortedLegals, comp);
        Collections.sort(sortedIllegals, comp);
        System.out.println("ALL CARDS : ");
        for (int i = 0; i < toSort.size(); ++i) {
            System.out.print(toSort.get(i) + " ");
        }
        System.out.println("-----------------------");
        System.out.println("LEGALS : ");
        for (int i = 0; i < sortedLegals.size(); ++i) {
            System.out.print(sortedLegals.get(i) + " ");
        }
        System.out.println("-----------------------");
        System.out.println("ILLEGALS : ");
        for (int i = 0; i < sortedIllegals.size(); ++i) {
            System.out.print(sortedIllegals.get(i) + " ");
        }
        System.out.println("-----------------------");

        if (isBasic()) {
            makeBasicBag();
        }

        if (isBriber()) {
            makeBriberBag();
        }

        if (isGreedy()) {
            makeGreedyBag();
        }
    }

    public List<Integer> getBag() {
        return bag;
    }

    public void makeBasicBag() {
        System.out.println("SUNT AICI");
    }

    public final void makeBriberBag() {

    }

    public void makeGreedyBag() {

    }
}
