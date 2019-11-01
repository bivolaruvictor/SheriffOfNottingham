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

    public int compare(final Integer c1, final Integer c2) {
        int freqCompare = freqMap.get(c2).compareTo(freqMap.get(c1));
        int profitCompare = 1;
        if (allGoods.get(c1).getProfit() < allGoods.get(c2).getProfit()) {
            profitCompare = 1;
        } else if (allGoods.get(c1).getProfit() == allGoods.get(c2).getProfit()) {
            profitCompare = 0;
        } else {
            profitCompare = -1;
        }
        int indexCompare;
        if (allGoods.get(c1).getId() < allGoods.get(c2).getId()) {
            indexCompare = 1;
        } else if (allGoods.get(c1).getId() == allGoods.get(c2).getId()) {
            indexCompare = 0;
        } else {
            indexCompare = -1;
        }
        if (freqCompare == 0) {
            if (profitCompare == 0) {
                return indexCompare;
            } else {
                return profitCompare;
            }
        }
        return freqCompare;
    }
}
