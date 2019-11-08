package com.tema1.Players;

import java.util.Comparator;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;

import java.util.Map;

public class CardsComparator implements Comparator<Integer> {
    private final Map<Integer, Integer> freqMap;
    private GoodsFactory goods;
    private Map<Integer, Goods> allGoods;


    public CardsComparator(final Map<Integer, Integer> freqMap) {
        this.freqMap = freqMap;
        goods = GoodsFactory.getInstance();
        allGoods = goods.getAllGoods();
    }

    public final int compare(final Integer c1, final Integer c2) {
        int freqCompare = freqMap.get(c2).compareTo(freqMap.get(c1));
        //System.out.println("Compar " + c1 + " : care apare de " + freqMap.get(c1) + " ori\n" + " cu " + c2 +  " : care apare de " + freqMap.get(c2) + " ori\n");
        int profitCompare = compareProfit(c1, c2);
        int indexCompare = compareIndex(c1, c2);
        if (freqCompare == 0) {
            if (profitCompare == 0) {
                return indexCompare;
            } else {
                return profitCompare;
            }
        } else {
            return freqCompare;
        }
    }

    public final int compareProfit(final Integer c1, final Integer c2) {
        int profit1 = allGoods.get(c1).getProfit();
        int profit2 = allGoods.get(c2).getProfit();
        return (profit1 < profit2) ? 1 : ((profit1 == profit2) ? 0 : -1);
    }

    public final int compareIndex(final Integer c1, final Integer c2) {
        int id1 = allGoods.get(c1).getId();
        int id2 = allGoods.get(c2).getId();
        return (id1 < id2) ? 1 : ((id1 == id2) ? 0 : -1);
    }
}
