package com.mu.io.game.packet.imp.equip;

import com.mu.config.BroadcastManager;
import com.mu.db.log.IngotChangeType;
import com.mu.executor.imp.log.ItemForgingExecutor;
import com.mu.game.model.equip.zhuijia.ZhuijiaForgingData;
import com.mu.game.model.item.Item;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Rnd;
import java.util.HashMap;
import java.util.List;

public class ZhuijiaEquipment extends ReadAndWritePacket {
   public ZhuijiaEquipment(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      boolean useIngot = this.readBoolean();
      boolean defaultBind = true;
      Item item = StrengthEquipment.getForgingItem(player, itemID);
      int[] results = canZhuijia(player, item, useIngot);
      int result = results[0];
      int rtype = 2;
      if (result == 1) {
         rtype = doZhuijia(player, item, results[1], defaultBind);
      }

      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

      this.writeDouble((double)itemID);
      this.writeBoolean(result == 1);
      this.writeByte(rtype);
      player.writePacket(this);
   }

   public static int doZhuijia(Player player, Item item, int consumeIngot, boolean defaultBind) {
      int rtype = 2;
      int zhuijiaLevel = item.getZhuijiaLevel();
      ZhuijiaForgingData data = ZhuijiaForgingData.getNextLevelData(item);
      HashMap map = data.getItemMap();
      int result = player.getItemManager().deleteAndAddModel(map, 21, defaultBind, (List)null).getResult();
      map.clear();
      map = null;
      if (result == 1) {
         if (consumeIngot > 0) {
            PlayerManager.reduceIngot(player, consumeIngot, IngotChangeType.ZHUIJIA, String.valueOf(zhuijiaLevel));
         }

         int newLevel = zhuijiaLevel;
         int rnd = Rnd.get(100000);
         if (rnd <= data.getRate()) {
            newLevel = zhuijiaLevel + 1;
            rtype = 1;
         } else {
            if (consumeIngot <= 0) {
               newLevel = 0;
            }

            if (newLevel != zhuijiaLevel) {
               rtype = 3;
            }
         }

         player.getItemManager().updateZhuijiaLevel(item, newLevel);
         player.getTaskManager().onEventCheckCount(TargetType.CountType.ZhuiJia);
         if (rtype == 1) {
            BroadcastManager.broadcastAdditional(player, item);
         }

         ItemForgingExecutor.logFoging(player, item, zhuijiaLevel, 1, rtype == 1, consumeIngot, 0);
      }

      return rtype;
   }

   public static int[] canZhuijia(Player player, Item item, boolean useIngot) {
      if (item != null && item.getCount() >= 1) {
         if (item.isTimeLimited()) {
            return new int[]{5068, 0};
         } else if (!ZhuijiaForgingData.hasZhuijia(item.getItemType())) {
            return new int[]{5005, 0};
         } else {
            int zhuijiaLevel = item.getZhuijiaLevel();
            if (zhuijiaLevel >= ZhuijiaForgingData.getMaxLevel()) {
               return new int[]{5006, 0};
            } else {
               ZhuijiaForgingData data = ZhuijiaForgingData.getNextLevelData(item);
               int ingot = 0;
               if (!player.getBackpack().hasEnoughItem(data.getItemID(), data.getCount())) {
                  return new int[]{3001, 0};
               } else {
                  if (useIngot) {
                     if (player.getIngot() < data.getIngot()) {
                        return new int[]{1015, 0};
                     }

                     ingot = data.getIngot();
                  }

                  return new int[]{1, ingot};
               }
            }
         }
      } else {
         return new int[]{3002, 0};
      }
   }
}
