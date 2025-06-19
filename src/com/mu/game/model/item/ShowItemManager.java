package com.mu.game.model.item;

import com.mu.game.CenterManager;
import com.mu.game.model.item.show.ShowItem;
import com.mu.game.model.unit.player.Player;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ShowItemManager {
   private static Object obj = new Object();
   private static ConcurrentHashMap showItemMap = new ConcurrentHashMap();
   private static HashMap marketItemMap = new HashMap();
   private static HashMap wellDropItemMap = new HashMap();
   private static HashMap magicBoxItemMap = new HashMap();

   public static void checkExpire() {
      long now = System.currentTimeMillis();
      Iterator it = showItemMap.values().iterator();

      while(it.hasNext()) {
         ShowItem showItem = (ShowItem)it.next();
         if (showItem.isTimeOver(now)) {
            removeAddDestroyItem(showItem.getItem().getID());
         }
      }

   }

   public static void addShowItem(long roleID, long itemID) {
      Player player = CenterManager.getPlayerByRoleID(roleID);
      if (player != null) {
         Item item = player.getBackpack().getItemByID(itemID);
         if (item == null) {
            item = player.getEquipment().getItemByID(itemID);
         }

         if (item == null) {
            item = player.getDepot().getItemByID(itemID);
         }

         if (item != null) {
            addShowItem(item);
         }
      }
   }

   public static void addShowItem(Item item) {
      Object var1 = obj;
      synchronized(obj) {
         if (item != null) {
            ShowItem oldItem = (ShowItem)showItemMap.remove(item.getID());
            if (oldItem != null) {
               oldItem.destroy();
            }

            Item cloneItem = item.cloneItem(2);
            cloneItem.setId(item.getID());
            ShowItem showItem = new ShowItem(cloneItem);
            showItemMap.put(showItem.getItem().getID(), showItem);
         }
      }
   }

   public static Item getShowItem(long id) {
      if (showItemMap.containsKey(id)) {
         return ((ShowItem)showItemMap.get(id)).getItem();
      } else if (marketItemMap.containsKey(id)) {
         return (Item)marketItemMap.get(id);
      } else if (wellDropItemMap.containsKey(id)) {
         return (Item)wellDropItemMap.get(id);
      } else {
         return magicBoxItemMap.containsKey(id) ? (Item)magicBoxItemMap.get(id) : null;
      }
   }

   public static void addMarketItem(Item item) {
      Item tmpItem = item.cloneItem(2);
      tmpItem.setId(item.getID());
      marketItemMap.put(tmpItem.getID(), tmpItem);
   }

   public static Item getMarketItem(long itemID) {
      return (Item)marketItemMap.get(itemID);
   }

   public static void removeMarketItem(long itemID) {
      marketItemMap.remove(itemID);
   }

   public static void addDropItem(Item item) {
      wellDropItemMap.put(item.getID(), item);
   }

   public static Item getWellDropItem(long id) {
      return (Item)wellDropItemMap.get(id);
   }

   public static void removeWellDropItem(long id) {
      wellDropItemMap.remove(id);
   }

   public static void addMagicBoxItem(Item item) {
      magicBoxItemMap.put(item.getID(), item);
   }

   public static Item getMagicBoxItem(long id) {
      return (Item)magicBoxItemMap.get(id);
   }

   public static void removeMagicBoxItem(long id) {
      magicBoxItemMap.remove(id);
   }

   public static void removeAddDestroyItem(long id) {
      Object var2 = obj;
      synchronized(obj) {
         ShowItem item = (ShowItem)showItemMap.remove(id);
         if (item != null) {
            item.destroy();
            item = null;
         }

      }
   }
}
