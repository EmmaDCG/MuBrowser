package com.mu.io.game.packet.imp.equip;

import com.mu.config.BroadcastManager;
import com.mu.config.MessageText;
import com.mu.config.VariableConstant;
import com.mu.db.log.IngotChangeType;
import com.mu.executor.imp.log.ItemForgingExecutor;
import com.mu.game.model.equip.star.StarForgingData;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.rewardhall.vitality.VitalityTaskType;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.game.model.unit.player.popup.imp.EquipStrengthAdvancePopup;
import com.mu.game.model.unit.player.popup.imp.EquipStrengthMiddlePopup;
import com.mu.game.model.unit.player.popup.imp.EquipStrengthPrimaryPopup;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Rnd;
import java.util.HashMap;
import java.util.List;

public class StrengthEquipment extends ReadAndWritePacket {
   public static final int Result_success = 1;
   public static final int Result_unchanged = 2;
   public static final int Result_fail = 3;
   public static final int Result_destroy = 4;

   public StrengthEquipment(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public StrengthEquipment() {
      super(20214, (byte[])null);
   }

   public static Item getForgingItem(Player player, long itemID) {
      Item item = player.getBackpack().getItemByID(itemID);
      if (item == null) {
         item = player.getEquipment().getItemByID(itemID);
      }

      return item;
   }

   public static int getOtherRate(Player player, Item item, ItemModel luckyModel, StringBuffer sb) {
      int otherRate = player.getStatValue(StatEnum.STRENGTH_LUCKY);
      if (item.getFirstModify(2) != null) {
         otherRate += VariableConstant.Strength_Lucky;
         if (sb != null) {
            sb.append(MessageText.getText(5038) + " #n:{12}+" + VariableConstant.Strength_Lucky / 1000 + "%");
            sb.append("#b");
         }
      }

      if (luckyModel != null) {
         int luckyRate = luckyModel.getFirstStatValue(StatEnum.STRENGTH_LUCKY);
         otherRate += luckyRate;
         if (sb != null && luckyRate > 0) {
            sb.append(MessageText.getText(5039) + "#n:{12}+" + luckyRate / 1000 + "%");
         }
      }

      return otherRate;
   }

   public static void sendToClient(Player player, long itemID, boolean isSuccess, int rtype, long luckyID) {
      try {
         StrengthEquipment se = new StrengthEquipment();
         se.writeDouble((double)itemID);
         se.writeDouble((double)luckyID);
         se.writeBoolean(isSuccess);
         se.writeByte(rtype);
         player.writePacket(se);
         se.destroy();
         se = null;
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      long luckyItemID = (long)this.readDouble();
      boolean useProtect = this.readBoolean();
      boolean useBindFrist = true;
      Item item = getForgingItem(player, itemID);
      int result = canStrength(player, item, luckyItemID, useProtect);
      int rtype = 2;
      if (result == 1) {
         StarForgingData data = StarForgingData.getNextLevelData(item);
         if (needToPopup(player, data, useProtect)) {
            EquipStrengthPrimaryPopup pop = null;
            switch(data.getPopupID()) {
            case 13:
               pop = new EquipStrengthMiddlePopup(player.createPopupID(), data.getDes(), itemID, luckyItemID, useProtect, useBindFrist);
               break;
            case 14:
               pop = new EquipStrengthAdvancePopup(player.createPopupID(), data.getDes(), itemID, luckyItemID, useProtect, useBindFrist);
               break;
            default:
               pop = new EquipStrengthPrimaryPopup(player.createPopupID(), data.getDes(), itemID, luckyItemID, useProtect, useBindFrist);
            }

            ShowPopup.open(player, (Popup)pop);
            return;
         }

         doStrength(player, true, itemID, luckyItemID, useProtect, useBindFrist);
      } else {
         sendToClient(player, itemID, result == 1, rtype, luckyItemID);
         if (result != 1) {
            SystemMessage.writeMessage(player, result);
         }
      }

   }

   public static boolean needToPopup(Player player, StarForgingData data, boolean useProtect) {
      if (useProtect) {
         return false;
      } else if (!data.needToPopup()) {
         return false;
      } else {
         return !player.popNoAgain(data.getPopupID());
      }
   }

   public static void doStrength(Player player, boolean hasCheck, long itemID, long luckyItemID, boolean useProtect, boolean useBind) {
      Item item = getForgingItem(player, itemID);
      int result = 1;
      if (!hasCheck) {
         result = canStrength(player, item, luckyItemID, useProtect);
      }

      int rtype = 2;
      boolean success = false;
      if (result == 1) {
         StarForgingData data = StarForgingData.getNextLevelData(item);
         HashMap delMap = data.getNeedMap();
         result = player.getItemManager().deleteAndAddModel(delMap, 21, true, (List)null).getResult();
         ItemModel luckyModel = null;
         if (result == 1 && luckyItemID != -1L) {
            Item luckyItem = player.getBackpack().getItemByID(luckyItemID);
            luckyModel = luckyItem.getModel();
            player.getItemManager().deleteItem(luckyItem, 1, 21);
         }

         delMap.clear();
         if (result == 1) {
            int rnd = Rnd.get(1, 100000);
            int targetRate = data.getRate() + getOtherRate(player, item, luckyModel, (StringBuffer)null);
            int starLevel = item.getStarLevel();
            int preLevel = starLevel;
            if (rnd <= targetRate || starLevel + 1 == 7 && item.getOnceMaxStarLevel() < 7) {
               success = true;
            }

            if (success) {
               ++starLevel;
               rtype = 1;
            } else {
               rnd = Rnd.get(1, 100000);
               if (rnd <= data.getBackRate() && !useProtect) {
                  starLevel = data.getBackLevel();
               }

               if (starLevel != item.getStarLevel()) {
                  rtype = 3;
               }
            }

            if (starLevel != item.getStarLevel()) {
               player.getItemManager().updateStarLevel(item, starLevel, (List)null);
            }

            PlayerManager.reduceMoney(player, data.getMoney());
            if (useProtect) {
               PlayerManager.reduceIngot(player, data.getProtectIngot(), IngotChangeType.StrengthEquip, String.valueOf(preLevel));
            }

            player.getVitalityManager().onTaskEvent(VitalityTaskType.QHZB, 0, 1);
            player.getTaskManager().onEventCheckCount(TargetType.CountType.QiangHua);
            if (rtype == 1) {
               BroadcastManager.broadcastStrengthen(player, item);
            }

            ItemForgingExecutor.logFoging(player, item, preLevel, 0, rtype == 1, useProtect ? data.getProtectIngot() : 0, data.getMoney());
         }
      }

      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

      luckyItemID = player.getBackpack().getItemByID(luckyItemID) == null ? -1L : luckyItemID;
      sendToClient(player, itemID, result == 1, rtype, luckyItemID);
   }

   private static int canStrength(Player player, Item item, long luckyItemID, boolean useProtect) {
      if (item != null && item.getCount() >= 1) {
         if (item.isTimeLimited()) {
            return 5068;
         } else if (!StarForgingData.canForging(item.getItemType())) {
            return 5002;
         } else if (item.getStarLevel() >= StarForgingData.getMaxStarLevel(item.getItemType())) {
            return 5003;
         } else {
            StarForgingData data = StarForgingData.getNextLevelData(item);
            if (luckyItemID != -1L) {
               Item luckyItem = player.getBackpack().getItemByID(luckyItemID);
               if (luckyItem == null || luckyItem.getCount() < 1) {
                  return 3002;
               }

               if (!data.getLuckyMap().containsKey(luckyItem.getModelID())) {
                  return 5004;
               }
            }

            if (useProtect && player.getIngot() < data.getProtectIngot()) {
               return 1015;
            } else {
               return !PlayerManager.hasEnoughMoney(player, data.getMoney()) ? 1011 : 1;
            }
         }
      } else {
         return 3002;
      }
   }
}
