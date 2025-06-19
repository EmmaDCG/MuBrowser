package com.mu.io.game.packet.imp.player;

import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.properties.newPotentail.PotentialData;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class PotentialRocommoned extends ReadAndWritePacket {
   public PotentialRocommoned(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      HashMap recommonMap = PotentialData.allocat(player, player.getPotential());
      if (recommonMap != null && recommonMap.size() >= 1) {
         this.writeByte(recommonMap.size());
         Iterator it = recommonMap.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            this.writeShort(((StatEnum)entry.getKey()).getStatId());
            this.writeInt(((Integer)entry.getValue()).intValue());
         }

         recommonMap.clear();
         recommonMap = null;
         player.writePacket(this);
         ArrowGuideManager.pushArrow(player, 5, (String)null);
      } else {
         SystemMessage.writeMessage(player, 1008);
      }
   }
}
