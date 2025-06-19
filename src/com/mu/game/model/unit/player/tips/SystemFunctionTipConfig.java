package com.mu.game.model.unit.player.tips;

import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.action.imp.EquipItem;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.model.ItemType;
import com.mu.game.model.item.model.WeaponType;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.player.tips.SystemFunctionTip;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class SystemFunctionTipConfig {
   public static final int TipMaxLevel = 280;
   public static final int PotencailTipCount = 30;
   public static final int Drup_TipLevel = 50;
   public static final int Tip_EquipItem = 1;
   public static final int Tip_Potencial = 2;
   public static final int Tip_NewSkill = 3;
   public static final int Tip_UseItem = 4;
   public static final int Tip_BackPage_Full = 5;
   public static final int Tip_OpenVipShop = 6;
   public static final int Tip_Firecracker = 7;
   public static final int Tip_GetWing = 8;
   public static final int Tip_Welcom = 9;
   public static final int Tip_Composite = 10;
   public static final int Tip_ExpiredPanda = 11;
   public static final int Tip_ExpiredDevil = 12;
   public static final int Tip_ExpiredAngel = 13;
   private static HashMap typeTipIDs = new HashMap();

   public static void initTips(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int itemType = Tools.getCellIntValue(sheet.getCell("A" + i));
         int tipID = Tools.getCellIntValue(sheet.getCell("B" + i));
         typeTipIDs.put(itemType, tipID);
      }

   }

   public static int getTypeTipID(int itemType) {
      return typeTipIDs.containsKey(itemType) ? ((Integer)typeTipIDs.get(itemType)).intValue() : -1;
   }

   public static void potencailTips(Player player) {
      if (player.getPotential() >= 30) {
         SystemFunctionTip.sendToClient(player, 2, Integer.valueOf(0));
         ArrowGuideManager.pushArrow(player, 4, (String)null);
      }
   }

   public static boolean needToPopDrugTip(Player player, int itemType) {
      if (player.getLevel() < 50) {
         return false;
      } else if (player.getVipShowLevel() > 0 && player.isInHanging()) {
         return false;
      } else {
         Storage backpack = player.getBackpack();
         return !backpack.hasItemByType(itemType);
      }
   }

   public static void itemFunctionTips(Player player, Item item) {
      if (item.isEquipment()) {
         equipFunctionTips(player, item);
      } else {
         int tipID = getTypeTipID(item.getItemType());
         if (tipID != -1) {
            ItemAction action = item.getModel().getAction();
            if (action != null && !action.obtainFunctionTip(player, item.getModel())) {
               return;
            }

            SystemFunctionTip.sendToClient(player, 4, item.getID());
            autoUseItem(player, item, 4);
         }
      }

   }

   public static void equipFunctionTips(Player player, Item item) {
      if (player.getLevel() <= 280) {
         if (item.isEquipment()) {
            if (EquipItem.canEquipItem(item, player) == 1) {
               WeaponType wt = ItemType.getWeaponType(item.getItemType());
               if (wt == WeaponType.SecondaryHand) {
                  Item equipItem = player.getEquipment().getItemBySlot(0);
                  if (equipItem != null && ItemType.getWeaponType(equipItem.getItemType()) == WeaponType.BothHands) {
                     return;
                  }
               }

               int slotToEqip = player.getEquipment().getActualEquipSlot(item, -1);
               if (slotToEqip != -1) {
                  Item tempItem = player.getEquipment().getItemBySlot(slotToEqip);
                  if (tempItem == null || isPriority(tempItem, item)) {
                     SystemFunctionTip.sendToClient(player, 1, item.getID());
                     ArrowGuideManager.pushArrow(player, 2, (String)null);
                     autoUseItem(player, item, 1);
                  }

               }
            }
         }
      }
   }

   private static void autoUseItem(Player player, Item item, int type) {
      if (player.getLevel() <= 90) {
         ;
      }
   }

   public static boolean isPriority(Item item, Item targetItem) {
      if (targetItem.getQuality() > item.getQuality()) {
         return true;
      } else {
         if (targetItem.getQuality() == item.getQuality()) {
            if (targetItem.getLevel() > item.getLevel()) {
               return true;
            }

            if (targetItem.getLevel() == item.getLevel() && targetItem.getStarLevel() > item.getStarLevel()) {
               return true;
            }
         }

         return false;
      }
   }
}
