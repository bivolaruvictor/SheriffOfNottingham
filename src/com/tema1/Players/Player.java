package com.tema1.Players;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
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

    public Player(final String playerType) {
        this.playerType = playerType;
    }

    public void makeSherriff() {
        isMerchant = false;
        isSherriff = true;
    }

    public boolean isSherriff() {
        return isSherriff;
    }

    public void makeMerchant() {
        isMerchant = true;
        isSherriff = false;
    }

    public boolean isMerchant() {
        return isMerchant;
    }

    public void putInHand(final List<Integer> chosenCards) {
        this.hand  = chosenCards;
    }

    public String showHand() {
        String toShow = "";
        for (int i = 0; i < hand.size(); ++i) {
           toShow += hand.get(i) + " ";
        }
        return toShow;
    }

    public String typeToString() {
        return playerType;
    }
}
