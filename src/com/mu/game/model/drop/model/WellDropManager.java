package com.mu.game.model.drop.model;

import com.mu.config.BroadcastManager;
import com.mu.config.MessageText;
import com.mu.game.model.drop.DropItem;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ShowItemManager;
import com.mu.game.model.item.model.ItemColor;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.drop.WellDrop;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import jxl.Sheet;

public class WellDropManager {
   private static HashMap wellDropModelMap = new HashMap();
   public static final int wellDropMaxCount = 50;
   private static ConcurrentLinkedQueue wellItemList = new ConcurrentLinkedQueue();

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int modelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         wellDropModelMap.put(modelID, Integer.valueOf(1));
      }

   }

   public static String spellName(Monster monster, Map map) {
      String msg = MessageText.getText(3051);
      msg = msg.replace("%s%", map.getName()).replace("%n%", monster.getName() + "[" + monster.getLevel());
      return msg;
   }

   public static String spellNewName(Monster monster, Map map) {
      String msg = MessageText.getText(3057);
      msg = msg.replace("%s%", map.getName()).replace("%n%", monster.getName() + "[" + monster.getLevel());
      return msg;
   }

   public static String spellOpenBoxReason(Item item) {
      String name = "#c:{" + ItemColor.find(item.getQuality()).getColor() + "}" + item.getName() + "#c";
      String msg = MessageText.getText(3052);
      msg = msg.replace("%s%", name);
      return msg;
   }

   public static String spellOpenBoxNewReason(Item item) {
      String name = "#F{e=" + ItemColor.find(item.getQuality()).getNewColor() + "}" + item.getName() + "#F";
      String msg = MessageText.getText(3052);
      msg = msg.replace("%s%", name);
      return msg;
   }

   public static void broastcast(List dropItemList, Player player, String reason, String newReason) {
      if (reason.trim().length() >= 1) {
         List itemList = null;
         Iterator var6 = dropItemList.iterator();

         while(var6.hasNext()) {
            DropItem item = (DropItem)var6.next();
            WellShowItem dsItem = createWellShowItem(item.getItem(), player, reason, newReason);
            if (dsItem != null) {
               if (itemList == null) {
                  itemList = new ArrayList();
               }

               itemList.add(dsItem);
            }
         }

         doSend(player, itemList, false);
         itemList = null;
      }
   }

   private static void doSend(Player player, List itemList, boolean self) {
      if (itemList != null && itemList.size() >= 1) {
         WellDrop.sendToClient(player, itemList, false);
         BroadcastManager.broadcastWellItem(itemList);
         itemList.clear();
      }
   }

   public static void broadcastOpenBox(Item item, Player player, String reason, String newReason) {
      WellShowItem wsItem = createWellShowItem(item, player, reason, newReason);
      if (wsItem != null) {
         List itemList = new ArrayList();
         itemList.add(wsItem);
         doSend(player, itemList, false);
         itemList = null;
      }
   }

   private static WellShowItem createWellShowItem(Item item, Player player, String reason, String newReason) {
      if (item.isEquipment()) {
         if (!wellDropModelMap.containsKey(item.getModelID())) {
            return null;
         }

         int size = item.getBonusStatSize(3);
         if (size < 2) {
            return null;
         }
      } else if (!wellDropModelMap.containsKey(item.getModelID())) {
         return null;
      }

      WellShowItem dsItem = new WellShowItem(item, reason, newReason, player);
      dsItem.addLink();
      wellItemList.add(dsItem);
      ShowItemManager.addDropItem(dsItem.getItem());
      if (wellItemList.size() > 50) {
         WellShowItem oldItem = (WellShowItem)wellItemList.poll();
         if (oldItem != null) {
            if (needRemove(oldItem.getID())) {
               ShowItemManager.removeWellDropItem(oldItem.getID());
            }

            oldItem.destroy();
         }
      }

      return dsItem;
   }

   private static boolean needRemove(long itemID) {
      Iterator var3 = wellItemList.iterator();

      while(var3.hasNext()) {
         WellShowItem showItem = (WellShowItem)var3.next();
         if (showItem.getID() == itemID) {
            return false;
         }
      }

      return true;
   }

   public static List getWellItemList() {
      List itemList = new ArrayList();
      itemList.addAll(wellItemList);
      return itemList;
   }

   public static int getWellItemSize() {
      return wellItemList.size();
   }
}
