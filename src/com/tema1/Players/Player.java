package com.tema1.Players;

import com.tema1.common.Constants;

import java.util.*;
import java.util.Collections;

import com.tema1.goods.Goods;


public class Player {
    private Constants constants;
    private int id;
    private int budget;
    private String playerType;
    private String playerRole;
    private boolean isSherriff = false;
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

    public void makeSherriff() {
        isMerchant = false;
        isSherriff = true;
    }

    public void makeMerchant() {
        isMerchant = true;
        isSherriff = false;
    }

    public boolean isSherriff() {
        return isSherriff;
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

    }

    public List<Integer> getHand() {
        return hand;
    }

    public void makeBriberBag() {
        List<Integer> toSort;
        toSort = getHand();
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> outputArray = new ArrayList<>();
        for (int current : toSort) {
            int count = map.getOrDefault(current, 0);
            map.put(current, count + 1);
            outputArray.add(current);
        }

        CardsComparator comp = new CardsComparator(map);
        Collections.sort(outputArray, comp);
        for (int i = 0; i < outputArray.size(); ++i) {
            System.out.print(outputArray.get(i) + " ");
        }
        System.out.println();
    }

    public void makeGreedyBag() {

    }
}
