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
    private String playerType;
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
    private Map<Integer, Integer> marketFreqMap;
    private Integer timesSherriff;

    // Constructor
    public Player(final String playerType, final int id, final int budget, final int numPlayers) {
        this.numPlayers = numPlayers;
        timesSherriff = 0;
        hand = new ArrayList<>();
        this.budget = budget;
        this.id = id;
        this.playerType = playerType;
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

    private void pay(final Player player, final int sum) {
        budget -= sum;
        player.budget += sum;
    }

    private void getPaid(final Player player, final int sum) {
        budget += sum;
        player.budget -= sum;
    }


    public final int getId() {
        return id;
    }

    public void putInHand(final List<Integer> chosenCards) {
        hand = chosenCards;
    }

    private void addToStore(final List<Integer> items) {
        for (Integer i : items) {
            store.add(i);
        }
    }

    public final void incSherriff() {
        timesSherriff++;
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

    public final void makeSheriff() {
        isMerchant = false;
        isSheriff = true;
    }

    public final void makeMerchant() {
        isMerchant = true;
        isSheriff = false;
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
        if (playerType.equals("basic")) {
            isBasic = true;
        }
        if (playerType.equals("greedy")) {
            isGreedy = true;
        }
        if (playerType.equals("bribed")) {
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

    public final String getPlayerType() {
        if (isBasic()) {
            return "BASIC";
        } else {
            if (isGreedy()) {
                return "GREEDY";
            } else {
                return "BRIBED";
            }
        }
    }

    public final void makeBag() {
        if (getBag().size() != 0) {
            emptyBag();
        }
        List<Integer> currentHand = new ArrayList<>(getHand());
        List<Integer> illegals = new ArrayList<>();
        List<Integer> legals = new ArrayList<>();
        List<Integer> sortedHand;
        sortedHand = currentHand;

        Map<Integer, Integer> map = new HashMap<>();
        for (int current : sortedHand) {
            int count = map.getOrDefault(current, 0);
            map.put(current, count + 1);
            if (current <= Constants.MAX_LEGAL_INDEX) {
                legals.add(current);
            } else {
                illegals.add(current);
            }
        }

        List<Integer> sortedIllegals = illegals;
        List<Integer> sortedLegals = legals;
        CardsComparator comp = new CardsComparator(map);
        Collections.sort(sortedHand, comp);
        Collections.sort(sortedLegals, comp);
        Collections.sort(sortedIllegals, comp);

        if (isBasic()) {
            makeBasicBag(sortedHand, sortedLegals, sortedIllegals);
        }

        if (isBriber()) {
            makeBriberBag(sortedHand, sortedLegals, sortedIllegals);
        }

        if (isGreedy()) {
            makeGreedyBag(sortedHand, sortedLegals, sortedIllegals);
        }

        showBag();
    }

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

    public final void makeBasicBag(final List<Integer> sortedHand, final List<Integer> sortedLegals,
                             final List<Integer> sortedIllegals) {
        emptyBag();
        if (sortedLegals.size() != 0) {
            int declaredGood = sortedLegals.get(0);
            for (Integer legal : sortedLegals) {
                if (legal == declaredGood) {
                    if (bag.size() < Constants.BAG_SIZE) {
                        bag.add(legal);
                    }
                }
            }
            declareGoodsId(declaredGood);
        } else {
            emptyBag();
            List<Integer> dishonest = sortedIllegals;
            ProfitComparator comp = new ProfitComparator();
            Collections.sort(dishonest, comp);
            bag.add(dishonest.get(0));
            declareGoodsId(0);
        }
    }

    public final void makeGreedyBag(final List<Integer> sortedHand,
                                    final List<Integer> sortedLegals,
                                    final List<Integer> sortedIllegals) {
        ProfitComparator cmp = new ProfitComparator();
// TODO : Caz in care runda e impara
        if (getRound() % 2 == 1) {
            makeBasicBag(sortedHand, sortedLegals, sortedIllegals);
        } else {
            // TODO : Caz in care runda e para
            makeBasicBag(sortedHand, sortedLegals, sortedIllegals);
            if (bag.size() < Constants.BAG_SIZE) {
                if (!sortedIllegals.isEmpty()) {
                    List<Integer> tmp = new ArrayList<>(sortedIllegals);
                    if (bag.size() == 1) {
                        tmp.remove(0);
                    }
                    Collections.sort(tmp, cmp);
                    if (!tmp.isEmpty()) {
                        bag.add(tmp.get(0));
                    }
                }
            }
        }
    }

    public final void makeBriberBag(final List<Integer> sortedHand,
                                    final List<Integer> sortedLegals,
                              final List<Integer> sortedIllegals) {

        ProfitComparator cmp = new ProfitComparator();

        List<Integer> tmpIllegals = new ArrayList<>(sortedIllegals);
        List<Integer> tmpLegals = new ArrayList<>(sortedLegals);


        Collections.sort(tmpIllegals, cmp);
        Collections.sort(tmpLegals, cmp);

        resetBribe();

        if (getBudget() >= Constants.BRIBER_LOWER_LIMIT) {
            int possibleLoss = 0;
            int numberOfGoods = 0;
            int numberIllegalGoods = 0;
            int possibleBribe = Constants.LESS_THAN_TWO_ILLEGAL_MONEY;

            if (tmpIllegals.size() != 0) {
                for (Integer item : tmpIllegals) {

                    // TODO : AICI > ?
                    if (getBudget() - (possibleLoss + allGoods.get(item).getPenalty()) > 0) {
                        if (numberIllegalGoods < Constants.BAG_SIZE) {
                            numberIllegalGoods++;
                            getBag().add(item);
                            possibleLoss += allGoods.get(item).getPenalty();
                        }
                    }
                }

                if (numberIllegalGoods > 2) {
                    possibleBribe = Constants.MORE_THAN_TWO_ILLEGAL_MONEY;
                }

                numberOfGoods += numberIllegalGoods;

                if (getBag().size() != Constants.BAG_SIZE) {
                    for (Integer item : tmpLegals) {
                        possibleLoss += allGoods.get(item).getPenalty();
                        // TODO : AICI > ?
                        if (getBudget() - possibleLoss > 0 && numberOfGoods < Constants.BAG_SIZE) {
                            getBag().add(item);
                            numberOfGoods++;
                        }
                    }
                }

                declareGoodsId(0);
                offerBribe(possibleBribe);
            } else {
                makeBasicBag(sortedHand, sortedLegals, sortedIllegals);
            }
        } else {
            makeBasicBag(sortedHand, sortedLegals, sortedIllegals);
        }
    }


    public final void controlPlayers(final List<Player> players) {
        if (isBasic() && getBudget() >= Constants.MINIMUM_BUDGET) {
            basicControl(players);
        }

        if (isGreedy() && getBudget() >= Constants.MINIMUM_BUDGET) {
            greedyControl(players);
        }

        if (isBriber()) {
            briberControl(players);
        }
    }

    public final void basicControl(final List<Player> players) {
        clearConfiscated();
        emptyBag();
        for (Player player : players) {
            List<Integer> legalGoodsToAdd = new ArrayList<>();
            List<Integer> control = new ArrayList<>(player.getBag());
            Integer whatIsDeclared = player.getDeclaredGoodsId();
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
                int sum = player.getBag().size() * allGoods.get(getDeclaredGoodsId()).getPenalty();
                pay(player, sum);
                if (legalGoodsToAdd.size() == 0) {
                    player.addToStore(player.getBag());
                }
                player.emptyBag();
            } else {
                int sum = 0;
                for (Integer item : getConfiscated()) {
                    sum += allGoods.get(item).getPenalty();
                }
                getPaid(player, sum);
                player.emptyBag();
                setToAdd(addConfiscatedToDeck(player));
            }
        }
        emptyBag();
        clearConfiscated();
    }

    public final void greedyControl(final List<Player> players) {
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
                            * allGoods.get(getDeclaredGoodsId()).getPenalty();
                    pay(player, sum);
                    if (legalGoodsToAdd.size() == 0) {
                        player.addToStore(player.getBag());
                    }
                    player.emptyBag();
                } else {
                    int sum = 0;
                    for (Integer item : getConfiscated()) {
                        sum += allGoods.get(item).getPenalty();
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

    public final void briberControl(final List<Player> players) {
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

        if (unluckyPlayers.get(0).getId() == unluckyPlayers.get(1).getId()) {
            unluckyPlayers.remove(0);
            if (getBudget() >= Constants.MINIMUM_BUDGET) {
                basicControl(unluckyPlayers);
            } else {
                unluckyPlayers.get(0).addToStore(unluckyPlayers.get(0).getBag());
            }
            for (Player player : players) {
                if (player.getId() != unluckyPlayers.get(0).getId()) {
                    getPaid(player, player.getBribe());
                }
            }
        } else {
            if (getBudget() >= Constants.MINIMUM_BUDGET) {
                basicControl(unluckyPlayers);
            } else {
                unluckyPlayers.get(0).addToStore(unluckyPlayers.get(0).getBag());
                unluckyPlayers.get(1).addToStore(unluckyPlayers.get(1).getBag());
            }
            for (Player player : players) {
                if (player.getId() != unluckyPlayers.get(0).getId()
                        && player.getId() != unluckyPlayers.get(1).getId()) {
                    getPaid(player, player.getBribe());
                    player.addToStore(player.getBag());
                }
            }
        }
    }

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

    public final Integer getTimesSherriff() {
        return timesSherriff;
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
        int count = 0;
        List<Integer> addable = new ArrayList<>();
        for (Integer i1 : player.getBag()) {
            for (Integer i2 : getConfiscated()) {
                if (i1 == i2) {
                    addable.add(i1);
                }
            }
        }

        clearConfiscated();
        return addable;
    }

}


