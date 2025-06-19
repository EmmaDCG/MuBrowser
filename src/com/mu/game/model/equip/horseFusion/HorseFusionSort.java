package com.mu.game.model.equip.horseFusion;

import com.mu.game.model.item.Item;
import java.util.Comparator;

public class HorseFusionSort implements Comparator {
   public int compare(Item o1, Item o2) {
      if (o1.getStarLevel() > o2.getStarLevel()) {
         return 1;
      } else if (o1.getStarLevel() == o2.getStarLevel()) {
         if (o1.getLevel() > o2.getLevel()) {
            return -1;
         } else {
            return o1.getLevel() == o2.getLevel() ? 0 : 1;
         }
      } else {
         return -1;
      }
   }

   @Override
   public int compare(Object o1, Object o2) {
      return 0;
   }
}
