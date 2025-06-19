package com.mu.io.game.packet.imp.equip;

import com.mu.executor.imp.log.ItemForgingExecutor;
import com.mu.game.model.equip.rune.RuneForgingData;
import com.mu.game.model.equip.rune.RuneInheritData;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class RuneInherit extends ReadAndWritePacket {
   public RuneInherit(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long tagertItemID = (long)this.readDouble();
      long materialItemID = (long)this.readDouble();
      Item targetItem = StrengthEquipment.getForgingItem(player, tagertItemID);
      Item materialItem = StrengthEquipment.getForgingItem(player, materialItemID);
      int result = this.canInherit(player, targetItem, materialItem);
      if (result == 1) {
         RuneInheritData data = RuneInheritData.getInherite(materialItem.getRunes().size());
         result = player.getItemManager().inheriteRune(targetItem, materialItem, data.getItemID(), data.getCount()).getResult();
         if (result == 1) {
            ItemForgingExecutor.logFoging(player, targetItem, targetItem.getStarLevel(), 4, true, 0, 0);
         }
      }

      this.writeDouble((double)tagertItemID);
      this.writeDouble((double)materialItemID);
      this.writeBoolean(result == 1);
      if (result == 1) {
         RequestRuneInherit.writeRunes(targetItem, materialItem, true, this);
      }

      player.writePacket(this);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   private int canInherit(Player player, Item targetItem, Item materialItem) {
      if (targetItem != null && targetItem.getCount() >= 1 && materialItem != null && materialItem.getCount() >= 1) {
         if (targetItem.isTimeLimited()) {
            return 5068;
         } else if (!RuneForgingData.canForging(targetItem.getItemType())) {
            return 5041;
         } else if (!RuneForgingData.canForging(materialItem.getItemType())) {
            return 5042;
         } else if (materialItem.getRunes().size() < 1) {
            return 5044;
         } else {
            int number = materialItem.getRunes().size();
            if (targetItem.getRunes().size() > 0) {
               return 5046;
            } else {
               number = Math.min(number, RuneForgingData.getMaxcount());
               int needStarLevel = RuneForgingData.needStarLevel(number);
               if (needStarLevel > targetItem.getStarLevel()) {
                  return 5043;
               } else {
                  RuneInheritData riData = RuneInheritData.getInherite(number);
                  return !player.getBackpack().hasEnoughItem(riData.getItemID(), riData.getCount()) ? 3001 : 1;
               }
            }
         }
      } else {
         return 3002;
      }
   }
}
