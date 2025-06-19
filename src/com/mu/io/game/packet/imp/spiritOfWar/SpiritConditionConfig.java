package com.mu.io.game.packet.imp.spiritOfWar;

import com.mu.game.model.spiritOfWar.filter.FilterCondition;
import com.mu.game.model.spiritOfWar.filter.FilterGroup;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.List;

public class SpiritConditionConfig extends ReadAndWritePacket {
   public SpiritConditionConfig(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      List groups = FilterGroup.getGroupList();
      this.writeByte(groups.size());
      Iterator var4 = groups.iterator();

      while(var4.hasNext()) {
         FilterGroup group = (FilterGroup)var4.next();
         List conditions = group.getConditionList();
         this.writeByte(group.getGroupID());
         this.writeUTF(group.getPrefixName());
         this.writeByte(conditions.size());
         Iterator var7 = conditions.iterator();

         while(var7.hasNext()) {
            FilterCondition condition = (FilterCondition)var7.next();
            this.writeUTF(condition.getName());
         }

         this.writeByte(group.getDefaultIndex());
      }

      player.writePacket(this);
   }
}
