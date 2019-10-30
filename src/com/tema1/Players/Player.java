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
    private List<String> hand;
    private List<String> bag;
    private List<String> market;

    public Player(final String playerType) {
        this.playerType = playerType;
    }

    public void makeSherriff() {
        isMerchant = false;
        isSherriff = true;
    }

    public void makeMerchant() {
        isMerchant = true;
        isSherriff = false;
    }

    public void putInHand(final List<String> chosenCards) {
        this.hand  = chosenCards;
    }

    public void showHand() {
        for (int i = 0; i < hand.size(); ++i) {
            System.out.println(hand.get(i));
        }
    }

    public String typeToString() {
        return playerType;
    }
}
