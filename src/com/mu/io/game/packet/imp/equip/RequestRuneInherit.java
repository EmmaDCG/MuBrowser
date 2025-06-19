package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.rune.RuneForgingData;
import com.mu.game.model.equip.rune.RuneInheritData;
import com.mu.game.model.equip.star.StarForgingData;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemRune;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.List;

public class RequestRuneInherit extends ReadAndWritePacket {
   public RequestRuneInherit(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long tagertItemID = (long)this.readDouble();
      long materialItemID = (long)this.readDouble();
      Item targetItem = StrengthEquipment.getForgingItem(player, tagertItemID);
      Item materialItem = StrengthEquipment.getForgingItem(player, materialItemID);
      int result = this.canInherit(player, targetItem, materialItem);
      this.writeDouble((double)tagertItemID);
      this.writeDouble((double)materialItemID);
      this.writeBoolean(result == 1);
      if (result == 1) {
         writeRunes(targetItem, materialItem, false, this);
         RuneInheritData data = RuneInheritData.getInherite(materialItem.getRunes().size());
         GetItemStats.writeItem(data.getItem(), this);
      } else {
         SystemMessage.writeMessage(player, result);
      }

      player.writePacket(this);
   }

   public static void writeRunes(Item targetItem, Item materialItem, boolean hasDone, WriteOnlyPacket packet) throws Exception {
      packet.writeByte(RuneForgingData.getMaxcount() * 2);
      List targetRunes = targetItem.getRunes();

      int i;
      for(i = 1; i <= RuneForgingData.getMaxcount(); ++i) {
         i = RuneForgingData.needStarLevel(i);
         packet.writeByte(i);
         ItemRune rune = targetRunes.size() >= i ? (ItemRune)targetRunes.get(i - 1) : null;
         packet.writeBoolean(rune != null);
         if (rune != null) {
            Item tmpItem = ItemTools.createItem(rune.getModelID(), 1, 2);
            GetItemStats.writeItem(tmpItem, packet);
         }
      }

      List materialRunes = materialItem.getRunes();

      for(i = 1; i <= RuneForgingData.getMaxcount(); ++i) {
         int openLevel = RuneForgingData.needStarLevel(i);
         if (hasDone) {
            openLevel = StarForgingData.getMaxStarLevel(materialItem.getItemType()) + 1;
         }

         packet.writeByte(openLevel);
         if (hasDone) {
            packet.writeBoolean(false);
         } else {
            ItemRune rune = materialRunes.size() >= i ? (ItemRune)materialRunes.get(i - 1) : null;
            packet.writeBoolean(rune != null);
            if (rune != null) {
               Item tmpItem = ItemTools.createItem(rune.getModelID(), 1, 2);
               GetItemStats.writeItem(tmpItem, packet);
            }
         }
      }

   }

   private int canInherit(Player player, Item targetItem, Item materialItem) {
      if (targetItem != null && targetItem.getCount() >= 1 && materialItem != null && materialItem.getCount() >= 1) {
         if (!RuneForgingData.canForging(targetItem.getItemType())) {
            return 5041;
         } else if (!RuneForgingData.canForging(materialItem.getItemType())) {
            return 5042;
         } else {
            return materialItem.getRunes().size() < 1 ? 5044 : 1;
         }
      } else {
         return 3002;
      }
   }

   public static int canInherit(Item item) {
      if (item != null && item.getCount() >= 1) {
         if (item.isTimeLimited()) {
            return 5068;
         } else {
            return !RuneForgingData.canForging(item.getItemType()) ? 5041 : 1;
         }
      } else {
         return 3002;
      }
   }
}
