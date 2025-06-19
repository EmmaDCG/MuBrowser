package com.mu.io.game.packet.imp.equip;

import com.mu.config.MessageText;
import com.mu.game.model.equip.star.StarForgingData;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.Iterator;
import java.util.List;

public class RequestStrength extends ReadAndWritePacket {
   public RequestStrength(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      long luckyItemID = (long)this.readDouble();
      Item item = StrengthEquipment.getForgingItem(player, itemID);
      int result = this.canStrength(player, item, luckyItemID);
      this.writeDouble((double)itemID);
      this.writeBoolean(result == 1);
      if (result == 1) {
         StarForgingData data = StarForgingData.getNextLevelData(item);
         this.writeByte(data.getNeedItems().size());
         Iterator var10 = data.getNeedItems().iterator();

         while(var10.hasNext()) {
            Item tmpItem = (Item)var10.next();
            GetItemStats.writeItem(tmpItem, this);
         }

         StringBuffer sb = new StringBuffer();
         sb.append(MessageText.getText(5037) + "#n:{1}" + "+" + data.getRate() / 1000 + "%");
         sb.append("#b");
         Item luckyItem = player.getBackpack().getItemByID(luckyItemID);
         int targetRate = data.getRate() + StrengthEquipment.getOtherRate(player, item, luckyItem == null ? null : luckyItem.getModel(), sb);
         targetRate = Math.min(100000, targetRate);
         this.writeByte(targetRate / 1000);
         this.writeInt(data.getMoney());
         String s = "";
         List newBase = GetItemStats.getBaseStats(item, 2);

         String[] newStrings;
         for(Iterator var15 = newBase.iterator(); var15.hasNext(); s = s + newStrings[1] + "#b") {
            newStrings = (String[])var15.next();
         }

         this.writeUTF(s);
         this.writeUTF(sb.toString());
         boolean canBack = data.getBackRate() > 0;
         this.writeBoolean(canBack);
         if (canBack) {
            this.writeInt(data.getProtectIngot());
         }
      }

      player.writePacket(this);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   private int canStrength(Player player, Item item, long luckyItemID) {
      if (item != null && item.getCount() >= 1) {
         if (!StarForgingData.canForging(item.getItemType())) {
            return 5002;
         } else if (item.getStarLevel() >= StarForgingData.getMaxStarLevel(item.getItemType())) {
            return 5003;
         } else {
            StarForgingData data = StarForgingData.getNextLevelData(item);
            if (luckyItemID != -1L) {
               Item luckyItem = player.getBackpack().getItemByID(luckyItemID);
               if (luckyItem == null) {
                  return 3002;
               }

               if (!data.getLuckyMap().containsKey(luckyItem.getModelID())) {
                  return 5004;
               }
            }

            return 1;
         }
      } else {
         return 3002;
      }
   }

   public static int canStrength(Item item) {
      if (item != null && item.getCount() >= 1) {
         if (item.isTimeLimited()) {
            return 5068;
         } else if (!StarForgingData.canForging(item.getItemType())) {
            return 5002;
         } else {
            return item.getStarLevel() >= StarForgingData.getMaxStarLevel(item.getItemType()) ? 5003 : 1;
         }
      } else {
         return 3002;
      }
   }
}
