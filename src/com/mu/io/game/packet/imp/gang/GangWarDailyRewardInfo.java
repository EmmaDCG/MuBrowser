package com.mu.io.game.packet.imp.gang;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.Iterator;

public class GangWarDailyRewardInfo extends ReadAndWritePacket {
   public GangWarDailyRewardInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      try {
         ArrayList list = DungeonManager.getLuolanManager().getTemplate().getDailyItemList();
         this.writeByte(list.size());
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            Item item = (Item)var3.next();
            GetItemStats.writeItem(item, this);
         }

         Player player = this.getPlayer();
         this.writeByte(GangManager.getWarDailyReceiveStatus(player));
         player.writePacket(this);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
