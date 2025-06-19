package com.mu.io.game.packet.imp.player;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class AttributeChange extends WriteOnlyPacket {
   public AttributeChange() {
      super(10043);
   }

   public static void sendToClient(Player player, HashMap changeMap) {
      try {
         if (changeMap.size() < 1) {
            return;
         }

         AttributeChange ac = new AttributeChange();
         ac.writeByte(changeMap.size());
         Iterator it = changeMap.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            ac.writeUTF(((StatEnum)entry.getKey()).getName() + (((Integer)entry.getValue()).intValue() > 0 ? "+" : "") + entry.getValue() + (((StatEnum)entry.getKey()).isPercent() ? "%" : ""));
         }

         player.writePacket(ac);
         ac.destroy();
         ac = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
