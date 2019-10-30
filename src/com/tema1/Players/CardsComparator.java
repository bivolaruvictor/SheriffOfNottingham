package com.tema1.Players;

import java.util.Comparator;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;

import java.util.Map;

public class CardsComparator implements Comparator<Integer> {
    private GoodsFactory goods;
    private Map<Integer, Goods> allGoods;

    public int compare(final Integer c1, final Integer c2) {
        goods = GoodsFactory.getInstance();
        allGoods = goods.getAllGoods();
        return - allGoods.get(c1).getProfit() + allGoods.get(c2).getProfit();
    }
}
