package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.zhuijia.ZhuijiaForgingData;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.Iterator;
import java.util.List;

public class RequestZhuija extends ReadAndWritePacket {
   public RequestZhuija(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      Item item = StrengthEquipment.getForgingItem(player, itemID);
      int result = canZhuijia(item);
      this.writeDouble((double)itemID);
      this.writeBoolean(result == 1);
      if (result == 1) {
         ZhuijiaForgingData data = ZhuijiaForgingData.getNextLevelData(item);
         GetItemStats.writeItem(data.getNeedShowItem(), this);
         this.writeByte(ZhuijiaForgingData.getMaxLevel());
         this.writeByte(data.getRate() / 1000);
         String s = "";
         List newBase = GetItemStats.getZhuijiaStats(item, 3);
         String[] newStrings;
         if (newBase != null) {
            for(Iterator var10 = newBase.iterator(); var10.hasNext(); s = s + newStrings[1] + "#b") {
               newStrings = (String[])var10.next();
            }
         }

         this.writeUTF(s);
         boolean canBack = item.getZhuijiaLevel() > 0;
         this.writeBoolean(canBack);
         if (canBack) {
            this.writeInt(data.getIngot());
         }
      } else {
         SystemMessage.writeMessage(player, result);
      }

      player.writePacket(this);
   }

   public static int canZhuijia(Item item) {
      if (item != null && item.getCount() >= 1) {
         if (item.isTimeLimited()) {
            return 5068;
         } else if (!ZhuijiaForgingData.hasZhuijia(item.getItemType())) {
            return 5005;
         } else {
            int zhuijiaLevel = item.getZhuijiaLevel();
            return zhuijiaLevel >= ZhuijiaForgingData.getMaxLevel() ? 5006 : 1;
         }
      } else {
         return 3002;
      }
   }
}
