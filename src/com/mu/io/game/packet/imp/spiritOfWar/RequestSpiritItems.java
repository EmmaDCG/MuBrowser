package com.mu.io.game.packet.imp.spiritOfWar;

import com.mu.game.model.item.Item;
import com.mu.game.model.spiritOfWar.filter.FilterCondition;
import com.mu.game.model.spiritOfWar.filter.FilterGroup;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RequestSpiritItems extends ReadAndWritePacket {
   public RequestSpiritItems(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      List conditions = new ArrayList();
      int size = this.readByte();

      for(int i = 0; i < size; ++i) {
         int groupID = this.readByte();
         int index = this.readByte();
         FilterGroup group = FilterGroup.getFilterGroup(groupID);
         if (group == null) {
            return;
         }

         FilterCondition condition = group.getFilterCondition(index);
         if (condition != null) {
            conditions.add(condition);
         }
      }

      List items = new ArrayList();
      player.getBackpack().getSpiritItem(items, conditions);
      this.writeByte(items.size());
      Iterator var11 = items.iterator();

      while(var11.hasNext()) {
         Item item = (Item)var11.next();
         this.writeDouble((double)item.getID());
      }

      player.writePacket(this);
   }
}
