package com.tema1.Players;

import com.tema1.common.Constants;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;


import com.tema1.goods.IllegalGoods;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.LegalGoods;


public class Player {
    private int numPlayers;
    private int bribe;
    private int round;
    private int id;
    private int budget;
    private boolean isSheriff = false;
    private boolean isMerchant = false;
    private boolean isBasic = false;
    private boolean isGreedy = false;
    private boolean isBriber = false;
    private List<Integer> hand;
    private List<Integer> bag;
    private List<Integer> store;
    private List<Integer> confiscated;
    private List<Integer> toAdd;
    private int declaredGoodsId;
    private Map<Integer, Goods> allGoods;
    private List<Integer> kingGoods = new ArrayList<>();
    private List<Integer> queenGoods = new ArrayList<>();
    private List<Integer> sortedLegals = new ArrayList<>();
    private List<Integer> sortedIllegals = new ArrayList<>();
    private Map<Integer, Integer> marketFreqMap;

    // Constructor
    public Player(final int id, final int budget, final int numPlayers) {
        this.numPlayers = numPlayers;
        hand = new ArrayList<>();
        this.budget = budget;
        this.id = id;
        bag = new ArrayList<>(Constants.BAG_SIZE);
        GoodsFactory goods = GoodsFactory.getInstance();
        allGoods = goods.getAllGoods();
        marketFreqMap = new HashMap<>();
        store = new ArrayList<>(0);
        confiscated = new ArrayList<>(0);
        toAdd = new ArrayList<>(0);
        this.round = 1;
    }

    public final void setRound(final int round) {
        this.round = round;
    }

    final void pay(final Player player, final int sum) {
        budget -= sum;
        player.budget += sum;
    }

    final void getPaid(final Player player, final int sum) {
        budget += sum;
        player.budget -= sum;
    }


    public final int getId() {
        return id;
    }

    public final void putInHand(final List<Integer> chosenCards) {
        hand = chosenCards;
    }

    final void addToStore(final List<Integer> items) {
        for (Integer i : items) {
            store.add(i);
        }
    }

    public final void addToBudget(final int sum) {
        budget += sum;
    }

    public final String showHand() {
        StringBuilder toShow = new StringBuilder();
        for (Integer integer : hand) {
            toShow.append(integer).append(" ");
        }
        return toShow.toString();
    }

    public final void setRole() {
        isMerchant = false;
        isSheriff = true;
    }

    public final void resetRole() {
        isMerchant = !isMerchant;
        isSheriff = !isSheriff;
    }

    public final boolean isSheriff() {
        return isSheriff;
    }

    public final List<Integer> getHand() {
        return hand;
    }

    public final Map<Integer, Integer> getMarketFreqMap() {
        return marketFreqMap;
    }

    public final void strategySet() {
        if (getPlayerType().equals(PlayersType.basic)) {
            isBasic = true;
        }
        if (getPlayerType().equals(PlayersType.greedy)) {
            isGreedy = true;
        }
        if (getPlayerType().equals(PlayersType.bribed)) {
            isBriber = true;
        }
    }

    public final void setToAdd(final List<Integer> toAdd) {
        this.toAdd.addAll(toAdd);
    }

    public final boolean isBasic() {
        return isBasic;
    }

    public final boolean isGreedy() {
        return isGreedy;
    }

    public final boolean isBriber() {
        return isBriber;
    }

    public final void declareGoodsId(final int goodId) {
        this.declaredGoodsId = goodId;
    }

    public final void offerBribe(final int pBribe) {
        this.bribe = pBribe;
    }

    public final int getBribe() {
        return bribe;
    }

    public final void resetBribe() {
        bribe = 0;
    }

    public final void takeBribe(final Player player, final int pBribe) {
        getPaid(player, pBribe);
    }

    public String getPlayerType() { return null; }

    // Getteri

    public final List<Integer> getKingGoods() {
        return kingGoods;
    }

    public final List<Integer> getQueenGoods() {
        return queenGoods;
    }

    public final List<Integer> getToAdd() {
        return toAdd;
    }

    public final List<Integer> getBag() {
        return bag;
    }

    public final int getDeclaredGoodsId() {
        return declaredGoodsId;
    }

    public final List<Integer> getStore() {
        return store;
    }

    public Map<Integer, Goods> getAllGoods() {
        return allGoods;
    }

    public final List<Integer> getSortedLegals() {
        return sortedLegals;
    }

    public final void clearSortedLegals() {
        sortedLegals.clear();
    }

    public final List<Integer> getSortedIllegals() {
        return sortedIllegals;
    }

    public final void clearSortedIllegals() {
        sortedIllegals.clear();
    }

    public final int getBudget() {
        return budget;
    }

    public final int getNumPlayers() {
        return numPlayers;
    }

    public final List<Integer> getConfiscated() {
        return confiscated;
    }

    public final void clearConfiscated() {
        getConfiscated().clear();
    }

    public final int getRound() {
        return round;
    }

    public final void emptyBag() {
        bag.clear();
    }


    // Show

    public final String showBag() {
        StringBuilder toShow = new StringBuilder();
        for (Integer integer : getBag()) {
            toShow.append(integer).append(" ");
        }
        return toShow.toString();
    }

    public final String showMarket() {
        StringBuilder toShow = new StringBuilder();
        for (Integer integer : getStore()) {
            toShow.append(integer).append(" ");
        }
        return toShow.toString();
    }

    public final String showMoney() {
        return String.valueOf(budget);
    }

    // Game Mechanics

    public final void arrangeCards() {
        if (getBag().size() != 0) {
            emptyBag();
        }

        List<Integer> sortedHand = new ArrayList<>(getHand());

        clearSortedIllegals();
        clearSortedLegals();
        Map<Integer, Integer> map = new HashMap<>();
        for (int current : sortedHand) {
            int count = map.getOrDefault(current, 0);
            map.put(current, count + 1);
            if (current <= Constants.MAX_LEGAL_INDEX) {
                getSortedLegals().add(current);
            } else {
                getSortedIllegals().add(current);
            }
        }

        CardsComparator comp = new CardsComparator(map);
        Collections.sort(sortedHand, comp);
        Collections.sort(getSortedLegals(), comp);
        Collections.sort(getSortedIllegals(), comp);
    }

    public void makeBag() {  }

    public void controlPlayers(final List<Player> players) {    }


    public final void setStore(final List<Integer> store) {
        this.store = store;
    }

    public final void giveBonuses() {
        transformStore(getStore());

        for (Integer item : getStore()) {
           addToBudget(allGoods.get(item).getProfit());
        }

        if (getKingGoods().size() != 0) {
            for (int i = 0; i < getKingGoods().size(); ++i) {
                LegalGoods legal = (LegalGoods) allGoods.get(getKingGoods().get(i));
                addToBudget(legal.getKingBonus());
            }
        }

        if (getQueenGoods().size() != 0) {
            for (int i = 0; i < getQueenGoods().size(); ++i) {
                LegalGoods legal = (LegalGoods) allGoods.get(getQueenGoods().get(i));
                addToBudget(legal.getQueenBonus());
            }
        }
    }

    public final void transformStore(final List<Integer> untransformed) {
        List<Integer> transformed = new ArrayList<>(0);
        for (Integer item : untransformed) {
            if (item <= Constants.MAX_LEGAL_INDEX) {
                transformed.add(item);
            } else {
                IllegalGoods illegal = (IllegalGoods) allGoods.get(item);
                Map<Goods, Integer> illegalToAdd = illegal.getIllegalBonus();
                addToBudget(allGoods.get(item).getProfit());
                // TODO : ofera legal bonus cards pentru illegal cards
                for (Map.Entry<Goods, Integer> entry : illegalToAdd.entrySet()) {
                    Integer goodId = entry.getKey().getId();
                    Integer howMany = entry.getValue();
                    for (int i = 0; i < howMany; ++i) {
                        transformed.add(goodId);
                    }
                }
            }
        }
        // TODO : Poate el organizez in functie de alt criteriu
        ProfitComparator cmp = new ProfitComparator();
        Collections.sort(transformed, cmp);
        setStore(transformed);
    }

    public final void setFreq() {
        for (int current : getStore()) {
            int count = marketFreqMap.getOrDefault(current, 0);
            marketFreqMap.put(current, count + 1);
        }
    }


    public final List<Integer> addConfiscatedToDeck(final Player player) {
        List<Integer> addable = new ArrayList<>();
        for (Integer i1 : player.getBag()) {
            for (Integer i2 : getConfiscated()) {
                if (i1.equals(i2)) {
                    addable.add(i1);
                }
            }
        }

        clearConfiscated();
        return addable;
    }

}


