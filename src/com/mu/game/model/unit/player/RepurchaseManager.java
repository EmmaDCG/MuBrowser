package com.mu.game.model.unit.player;

import com.mu.game.model.item.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepurchaseManager {
   private static final int Max_Repurchase = 10;
   private static HashMap roleItems = new HashMap();

   public static void addSellItem(Player player, Item item) {
      List roleItem = (List)roleItems.get(player.getID());
      if (roleItem == null) {
         roleItem = new ArrayList();
         roleItems.put(player.getID(), roleItem);
      }

      if (((List)roleItem).size() >= 10) {
         ((List)roleItem).remove(0);
      }

      ((List)roleItem).add(item);
   }

   public static List getRoleRepurchaseItems(long roleID) {
      return (List)roleItems.get(roleID);
   }

   public static Item deleteItem(long roleID, long itemID) {
      List roleItem = (List)roleItems.get(roleID);
      if (roleItem == null) {
         return null;
      } else {
         int index = -1;

         for(int i = 0; i < roleItem.size(); ++i) {
            Item item = (Item)roleItem.get(i);
            if (item.getID() == itemID) {
               index = i;
               break;
            }
         }

         return index != -1 ? (Item)roleItem.remove(index) : null;
      }
   }

   public static Item getItem(long roleID, long itemID) {
      List roleItem = (List)roleItems.get(roleID);
      if (roleItem == null) {
         return null;
      } else {
         for(int i = 0; i < roleItem.size(); ++i) {
            Item item = (Item)roleItem.get(i);
            if (item.getID() == itemID) {
               return item;
            }
         }

         return null;
      }
   }
}
