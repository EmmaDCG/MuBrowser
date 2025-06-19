package com.mu.game.model.item.model;

import com.mu.game.IDFactory;

public class ItemCreateType {
   public static final int Type_Default = 1;
   public static final int Type_System = 2;
   public static final int Type_Drop = 3;

   public static long getItemObjId(int type) {
      switch(type) {
      case 2:
      case 3:
         return IDFactory.getTemporaryID();
      default:
         return IDFactory.getItemID();
      }
   }
}
