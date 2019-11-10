package com.tema1.Players;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;

import java.util.Comparator;
import java.util.Map;

public class ProfitComparator implements Comparator<Integer> {
    private GoodsFactory goods;
    private Map<Integer, Goods> allGoods;

    public ProfitComparator() {
        goods = GoodsFactory.getInstance();
        allGoods = goods.getAllGoods();
    }

    public final int compare(final Integer c1, final Integer c2) {
        int profit1 = allGoods.get(c1).getProfit();
        int profit2 = allGoods.get(c2).getProfit();
        if (profit1 == profit2) {
            return idComparator(c1, c2);
        } else {
            return (profit1 < profit2) ? 1 : -1;
        }
    }

    public final int idComparator(final Integer c1, final Integer c2) {
        return (c1 < c2) ? 1 : ((c1 == c2) ? 0 : -1);
    }
}
