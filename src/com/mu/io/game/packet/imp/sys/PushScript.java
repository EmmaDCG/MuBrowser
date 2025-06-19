package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class PushScript extends WriteOnlyPacket {
   public PushScript() {
      super(1016);
   }

   public static void push(Player player, ArrayList list) {
      PushScript ps = new PushScript();

      try {
         ps.writeByte(list.size());
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            String s = (String)var4.next();
            ps.writeUTF(s);
         }

         player.writePacket(ps);
         ps.destroy();
         ps = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
