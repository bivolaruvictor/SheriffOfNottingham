GoodsFactory goods = GoodsFactory.getInstance();
        Map<Integer, Goods> AllGoods = goods.getAllGoods();
        for (Map.Entry<Integer, Goods> entry : AllGoods.entrySet()) {
                // Asa verifici daca e legal sau nu
                if (entry.getValue().getType().equals(GoodsType.Legal)) {
                    System.out.println("AAAAAAAAAA");
                } else {
                    System.out.println(entry.getKey()+" : "+entry.getValue().getType() + " " + entry.getValue().getProfit()
                            + " " + entry.getValue().getPenalty());
                }
        }