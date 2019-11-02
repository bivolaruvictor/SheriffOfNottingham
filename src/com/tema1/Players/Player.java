package com.tema1.Players;

import com.tema1.common.Constants;

import java.util.*;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.IllegalGoods;
import com.tema1.goods.LegalGoods;


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
    private List<Integer> store;
    private int declaredGoodsId;
    private GoodsFactory goods;
    private LegalGoods lGoods;

    public LegalGoods getlGoods() {
        return lGoods;
    }

    private Map<Integer, Goods> allGoods;
    private List<Integer> kingGoods;
    private List<Integer> queenGoods;
    private Map<Integer, Integer> marketFreqMap;
    public List<Integer> getKingGoods() {
        return kingGoods;
    }

    public List<Integer> getQueenGoods() {
        return queenGoods;
    }

    public int getDeclaredGoodsId() {
        return declaredGoodsId;
    }

    public Player(final String playerType, final int id, final int budget) {
        constants = new Constants();
        this.budget = budget;
        this.id = id;
        this.playerType = playerType;
        bag = new ArrayList<>(constants.BAG_SIZE);
        goods = GoodsFactory.getInstance();
        allGoods = goods.getAllGoods();
        marketFreqMap = new HashMap<>();
        store = new ArrayList<>(0);
    }

    public void putInHand(final List<Integer> chosenCards) {
        this.hand  = chosenCards;
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
        List<Integer> illegals = new ArrayList<>();
        List<Integer> legals = new ArrayList<>();
        List<Integer> sortedHand;
        List<Integer> sortedIllegals = new ArrayList<>();
        List<Integer> sortedLegals = new ArrayList<>();
        currentHand = getHand();
        sortedHand = currentHand;

        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> outputArray = new ArrayList<>();
        for (int current : sortedHand) {
            int count = map.getOrDefault(current, 0);
            map.put(current, count + 1);
            if (current <= constants.MAX_LEGAL_INDEX) {
                legals.add(current);
            } else {
                illegals.add(current);
            }
        }

        sortedIllegals = illegals;
        sortedLegals = legals;
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
            makeGreedyBag();
        }

        showBag();
    }

    public String showBag() {
        StringBuilder toShow = new StringBuilder();
        for (Integer integer : bag) {
            toShow.append(integer).append(" ");
        }
        return toShow.toString();
    }

    public String showMoney() {
        return String.valueOf(budget);
    }

    public void emptyBag() {
        bag.clear();
    }

    public List<Integer> getBag() {
        return bag;
    }

    public void declareGoodsId(final int goodId) {
        this.declaredGoodsId = goodId;
    }

    public void makeBasicBag(final List<Integer> sortedHand, final List<Integer> sortedLegals,
                             final List<Integer> sortedIllegals) {

//        System.out.println("ALL CARDS : ");
//        for (int i = 0; i < sortedHand.size(); ++i) {
//            System.out.print(sortedHand.get(i) + " ");
//        }
//        System.out.println("-----------------------");
//        System.out.println("LEGALS : ");
//        for (Integer sortedLegal : sortedLegals) {
//            System.out.print(sortedLegal + " ");
//        }
//        System.out.println("-----------------------");
//        System.out.println("ILLEGALS : ");
//        for (int i = 0; i < sortedIllegals.size(); ++i) {
//            System.out.print(sortedIllegals.get(i) + " ");
//        }
//        System.out.println("-----------------------");

        if (sortedLegals.size() != 0) {
            int declaredGood = sortedLegals.get(0);
            for (Integer legal : sortedLegals) {
                if (legal == declaredGood) {
                    if (bag.size() < constants.BAG_SIZE) {
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

    public final void makeBriberBag() {

    }

    public void makeGreedyBag() {

    }


    public void controlPlayers(List<Player> players) {
        if (isBasic() && getBudget() >= constants.MINIMUM_BUDGET) {
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
        List<Integer> confiscated = new ArrayList<>(0);
        for (Player player : players) {
            List<Integer> control = player.getBag();
            Integer whatIsDeclared = player.getDeclaredGoodsId();
            for (Integer good : control) {
                if (good != whatIsDeclared) {
                    confiscated = control;
                    player.emptyBag();
                }
            }
            if (confiscated.size() == 0) {
                // TODO : DA BANII MERCHANT
                int sum = player.getBag().size() * allGoods.get(getDeclaredGoodsId()).getPenalty();
                pay(player, sum);
                player.addToStore(player.getBag());
            }
        }
    }

    public void greedyControl(List<Player> players) {

    }

    public void briberControl(List<Player> players) {

    }

    public void pay(Player player, int sum) {
        budget -= sum;
        player.budget += sum;
    }

    public int getId() {
        return id;
    }

    public void giveBonuses() {
        restoreMarket();
        List<Integer> transformedStore = transformStore(store);
        for (Integer item : transformedStore) {
           addToBudget(allGoods.get(item).getProfit());
        }

        setFreq();

        for (int i = 0; i < getKingGoods().size(); ++i) {
            //TODO : NU E BINE - TREBUIE BONUS DE KING
            addToBudget(allGoods.get(i).getProfit());
            kingGoods.remove(i);
        }

        for (int i = 0; i < getQueenGoods().size(); ++i) {
            //TODO : NU E BINE - TREBUIE BONUS DE KING
            addToBudget(allGoods.get(i).getProfit());
            queenGoods.remove(i);
        }
    }

    public List<Integer> transformStore(final List<Integer> untransformed) {
        List<Integer> transformed = new ArrayList<>(0);
        for (Integer item : untransformed) {
            if (item <= constants.MAX_LEGAL_INDEX) {
                transformed.add(item);
            } else {
                addToBudget(allGoods.get(item).getProfit());
                // TODO : ofera legal bonus cards pentru illegal cards
            }
        }
        ProfitComparator cmp = new ProfitComparator();
        Collections.sort(transformed, cmp);
        return transformed;
    }

    public void setFreq() {
        List<Integer> outputArray = new ArrayList<>();
        for (int current : getStore()) {
            int count = marketFreqMap.getOrDefault(current, 0);
            marketFreqMap.put(current, count + 1);
        }
    }

    public Map<Integer, Integer> getMarketFreqMap() {
        return marketFreqMap;
    }

    public void findKingGoods(List<Player> players) {
        kingGoods = new ArrayList<>(0);
        for (int i = 0; i <= constants.MAX_LEGAL_INDEX; ++i) {
            Player king = null;
            int kingNumber = 0;
            for (Player player : players) {
                //TODO : AICI S_AR PUTEA SA CRAPE
                if (player.getMarketFreqMap().get(i) > kingNumber) {
                    king = player;
                    kingNumber = player.getMarketFreqMap().get(i);
                }
            }
            if (kingNumber != 0) {
                int finalI = i;
                king.getStore().removeIf(n -> n == finalI);
                kingGoods.add(i);
            }
        }
    }

    public void findQueenGoods(List<Player> players) {
        queenGoods = new ArrayList<>(0);
        for (int i = 0; i <= constants.MAX_LEGAL_INDEX; ++i) {
            Player queen = null;
            int queenNumber = 0;
            for (Player player : players) {
                //TODO : AICI S_AR PUTEA SA CRAPE
                if (player.getMarketFreqMap().get(i) > queenNumber) {
                    queen = player;
                    queenNumber = player.getMarketFreqMap().get(i);
                }
            }
            if (queenNumber != 0) {
                int finalI = i;
                queen.getStore().removeIf(n -> n == finalI);
                queenGoods.add(i);
            }
        }
    }

    public void restoreMarket() {
        //TODO : POSIBILE ERORI
        for (int i = 0; i <= constants.MAX_LEGAL_INDEX; ++i) {
            int count = getMarketFreqMap().get(i);
            while (count > 0) {
                store.add(i);
                count--;
            }
        }
    }

    public List<Integer> getStore() {
        return store;

    }
}


