package com.mu.io.game.packet.imp.player;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.sevenDay.SevenDayTreasure;
import com.mu.game.model.unit.player.sevenDay.SevenDayTreasureData;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SevenDayPush extends ReadAndWritePacket {
   public SevenDayPush(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public SevenDayPush() {
      super(46401, (byte[])null);
   }

   public static void sendWhenLogin(Player player) {
      if (player.getSevenManager().isShowInMap()) {
         sendSevenDayData(player);
      }
   }

   public static void sendSevenDayData(Player player) {
      try {
         SevenDayTreasure seven = player.getSevenManager();
         SevenDayPush push = new SevenDayPush();
         push.writeByte(seven.getShowLoginTotalDay());
         push.writeByte(seven.getRemainCount());
         List items = SevenDayTreasureData.getShowItems();
         push.writeByte(items.size());

         for(int i = 0; i < items.size(); ++i) {
            Item item = (Item)items.get(i);
            GetItemStats.writeItem(item, push);
            push.writeBoolean(seven.hasFoundByShowIndex(i));
         }

         HashMap dataMap = SevenDayTreasureData.getTreasureMap();
         push.writeByte(dataMap.size());
         Iterator var6 = dataMap.values().iterator();

         while(var6.hasNext()) {
            SevenDayTreasureData data = (SevenDayTreasureData)var6.next();
            push.writeByte(data.getIndex());
            Item item = seven.getShowItem(data.getIndex());
            if (item == null) {
               push.writeBoolean(false);
            } else {
               push.writeBoolean(true);
               GetItemStats.writeItem(item, push);
            }
         }

         player.writePacket(push);
         push.destroy();
         push = null;
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public void process() throws Exception {
      sendSevenDayData(this.getPlayer());
   }
}
