package com.tema1.Players;

import com.tema1.common.Constants;

import java.util.*;

import com.tema1.goods.*;
import org.apache.commons.collections.SortedBag;

import javax.sound.midi.Soundbank;


public class Player {
    int potentialBribe;
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
    private int declaredGoodsId;
    private Map<Integer, Goods> allGoods;
    private List<Integer> kingGoods = new ArrayList<>();
    private List<Integer> QueenGoods = new ArrayList<>();
    private Map<Integer, Integer> marketFreqMap;

    // Constructor
    public Player(final String playerType, final int id, final int budget) {
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
        this.round = 1;
    }

    public String typeToString() {
        return playerType;
    }

    // Setteri
    public void setRound(final int round) {
        this.round = round;
    }

    public Map<Integer, Integer> getMarketFreqMap() {
        return marketFreqMap;
    }

    public void pay(Player player, int sum) {
        budget -= sum;
        player.budget += sum;
    }

    public void takeMoney(Player player, int sum) {
        budget += sum;
        player.budget -= sum;
    }

    public int getId() {
        return id;
    }

    public void putInHand(final List<Integer> chosenCards) {
        hand  = chosenCards;
    }

    public void addToStore(final List<Integer> items) {
        for (Integer i : items) {
            store.add(i);
        }
    }

    public void addToBudget(int sum) {
        budget += sum;
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

    public void strategySet() {
        if (playerType.equals("basic")) {
            isBasic = true;
        }
        if (playerType.equals("greedy")) {
            isGreedy = true;
        }
        if (playerType.equals("briber")) {
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

    public void declareGoodsId(final int goodId) {
        this.declaredGoodsId = goodId;
    }

    public void offerBribe(final int bribe) {
        potentialBribe = bribe;
    }

    public void takeBribe(final int bribe) {
        budget += bribe;
    }

    public String getPlayerType() {
        if (isBasic()) {
            return "BASIC";
        }

        if (isGreedy()) {
            return "GREEDY";
        }

        if(isBriber()) {
            return "BRIBER";
        }

        return null;
    }

    public void makeBag() {
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
            makeBriberBag();
        }

        if (isGreedy()) {
            makeGreedyBag(sortedHand, sortedLegals, sortedIllegals);
        }

        showBag();
    }

    // Getteri

    public List<Integer> getKingGoods() {
        return kingGoods;
    }

    public List<Integer> getQueenGoods() {
        return QueenGoods;
    }

    public LegalGoods getlGoods() {
        return null;
    }

    public List<Integer> getBag() {
        return bag;
    }

    public int getDeclaredGoodsId() {
        return declaredGoodsId;
    }

    public List<Integer> getStore() {
        return store;
    }

    public int getBudget() {
        return budget;
    }

    public List<Integer> getConfiscated() {
        return confiscated;
    }

    public void clearConfiscated() {
        getConfiscated().clear();
    }

    public int getPotentialBribe() {
        return potentialBribe;
    }

    public int getRound() {
        return round;
    }

    public void emptyBag() {
        bag.clear();
    }

    // Show

    public String showBag() {
        StringBuilder toShow = new StringBuilder();
        for (Integer integer : getBag()) {
            toShow.append(integer).append(" ");
        }
        return toShow.toString();
    }

    public String showMarket() {
        StringBuilder toShow = new StringBuilder();
        for (Integer integer : getStore()) {
            toShow.append(integer).append(" ");
        }
        return toShow.toString();
    }

    public String showMoney() {
        return String.valueOf(budget);
    }

    // Game Mechanics

    public void makeBasicBag(final List<Integer> sortedHand, final List<Integer> sortedLegals,
                             final List<Integer> sortedIllegals) {

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
            List<Integer> dishonest = sortedIllegals;
            ProfitComparator comp = new ProfitComparator();
            Collections.sort(dishonest, comp);
            bag.add(dishonest.get(0));
            declareGoodsId(0);
        }
    }

    public final void makeGreedyBag(final List<Integer> sortedHand, final List<Integer> sortedLegals,
                                    final List<Integer> sortedIllegals) {
// TODO : Caz in care runda e para
        if (getRound() % 2 == 1) {
            makeBasicBag(sortedHand, sortedLegals, sortedIllegals);
        } else {
            makeBasicBag(sortedHand, sortedLegals, sortedIllegals);
            if (bag.size() < Constants.BAG_SIZE) {
                if (!sortedIllegals.isEmpty()) {
                    bag.add(sortedIllegals.get(0));
                }
            }
        }
    }

    public void makeBriberBag() {

    }


    public void controlPlayers(List<Player> players) {
        if (isBasic() && getBudget() >= Constants.MINIMUM_BUDGET) {
            basicControl(players);
        }

        if (isGreedy()) {
            greedyControl(players);
        }

        if (isBriber()) {
            briberControl(players);
        }
    }

    public void basicControl(final List<Player> players) {
        for (Player player : players) {
            List<Integer> control = new ArrayList<>();
            control = player.getBag();
            Integer whatIsDeclared = player.getDeclaredGoodsId();
            for (Integer good : control) {
                if (good != whatIsDeclared) {
                    for (Integer goods1 : control) {
                        getConfiscated().add(goods1);
                    }
                    //player.emptyBag();
                    break;
                }
            }
            if (getConfiscated().size() == 0) {
                // TODO : DA BANII MERCHANT
                int sum = player.getBag().size() * allGoods.get(getDeclaredGoodsId()).getPenalty();
                pay(player, sum);
                player.addToStore(player.getBag());
                bag.clear();
            } else {
                int sum = 0;
                for (Integer item : getConfiscated()) {
                    sum += allGoods.get(item).getPenalty();
                    takeMoney(player, sum);
                }
                clearConfiscated();
            }
            player.emptyBag();
        }
    }

    public void greedyControl(List<Player> players) {
        for (Player player : players) {
            List<Integer> control = player.getBag();
            Integer whatIsDeclared = player.getDeclaredGoodsId();
            if (getPotentialBribe() == 0) {
                for (Integer good : control) {
                    if (good != whatIsDeclared) {
                        for (Integer goods1 : control) {
                            getConfiscated().add(goods1);
                        }
                        player.emptyBag();
                        break;
                    }
                }
                if (getConfiscated().size() == 0) {
                    // TODO : DA BANII MERCHANT
                    int sum = player.getBag().size() * allGoods.get(getDeclaredGoodsId()).getPenalty();
                    pay(player, sum);
                    player.addToStore(player.getBag());
                }
            } else {
                takeBribe(getPotentialBribe());
            }
        }
    }

    public void briberControl(List<Player> players) {

    }

    public void setStore(List<Integer> store) {
        this.store = store;
    }

    public void giveBonuses() {
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

    public void transformStore(final List<Integer> untransformed) {
        List<Integer> transformed = new ArrayList<>(0);
        for (Integer item : untransformed) {
            if (item <= Constants.MAX_LEGAL_INDEX) {
                transformed.add(item);
            } else {
                IllegalGoods illegal = (IllegalGoods) allGoods.get(item);
                Map<Goods, Integer> toAdd = illegal.getIllegalBonus();
                addToBudget(allGoods.get(item).getProfit());
                // TODO : ofera legal bonus cards pentru illegal cards
                for (Map.Entry<Goods, Integer> entry : toAdd.entrySet()) {
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

    public void setFreq() {
        for (int current : getStore()) {
            int count = marketFreqMap.getOrDefault(current, 0);
            marketFreqMap.put(current, count + 1);
        }
    }


    public List<Integer> addConfiscatedToDeck() {
        int count = 0;
        List<Integer> toAdd = new ArrayList<>();
        System.out.println(showHand());
        for (Integer i2 : getConfiscated()) {
            System.out.print(i2 + " ");
            }
        System.out.println();
        for (Integer i1 : getHand()) {
            for (Integer i2 : getConfiscated()) {
                if (i1 == i2) {
                    toAdd.add(i1);
                    count++;
                }
            }
        }
        clearConfiscated();
        return toAdd;
    }
}


