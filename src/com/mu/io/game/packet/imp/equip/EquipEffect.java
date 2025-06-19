package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class EquipEffect extends WriteOnlyPacket {
   public EquipEffect() {
      super(20228);
   }

   public void setData(HashMap itemMap) throws Exception {
      this.writeByte(itemMap.size());
      Iterator it = itemMap.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         this.writeDouble((double)((Long)entry.getKey()).longValue());
         this.writeBoolean(((Boolean)entry.getValue()).booleanValue());
      }

   }

   public static void sendToClient(Player player, HashMap itemMap) {
      try {
         if (itemMap.size() < 1) {
            return;
         }

         EquipEffect ee = new EquipEffect();
         ee.setData(itemMap);
         player.writePacket(ee);
         ee.destroy();
         ee = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
