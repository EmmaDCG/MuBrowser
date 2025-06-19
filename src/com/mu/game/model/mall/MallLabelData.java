package com.mu.game.model.mall;

import java.util.ArrayList;
import java.util.List;

public class MallLabelData {
   private String name;
   private int priceType = 2;
   private List itemList;

   public MallLabelData(String name) {
      this.name = name;
      this.itemList = new ArrayList();
   }

   public String getName() {
      return this.name;
   }

   public List getItemList() {
      return this.itemList;
   }

   public int getPriceType() {
      return this.priceType;
   }

   public void setPriceType(int priceType) {
      this.priceType = priceType;
   }
}
