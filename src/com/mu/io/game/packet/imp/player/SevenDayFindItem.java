package com.mu.io.game.packet.imp.player;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.sevenDay.SevenDayTreasureData;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.item.GetItemStats;

public class SevenDayFindItem extends ReadAndWritePacket {
   public SevenDayFindItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int index = this.readByte();
      int showIndex = player.getSevenManager().findTreasure(index);
      if (showIndex != -1) {
         this.writeByte(index);
         this.writeByte(player.getSevenManager().getRemainCount());
         Item item = SevenDayTreasureData.getShowItem(showIndex);
         GetItemStats.writeItem(item, this);
         player.writePacket(this);
         UpdateMenu.update(player, 31);
      }
   }
}
