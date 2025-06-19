package com.mu.game.model.item.container.imp;

public class TreasureHouse extends Storage {
   public TreasureHouse() {
      super(14, 1);
   }

   protected int getMaxCount() {
      return 300;
   }

   public int getDefaultCount() {
      return 300;
   }

   protected int getAddtionCount() {
      return 300;
   }

   protected int getDefaultPage() {
      return 1;
   }

   protected int getMaxPage() {
      return 1;
   }
}
