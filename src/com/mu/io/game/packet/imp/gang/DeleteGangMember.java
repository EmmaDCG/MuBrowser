package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;

public class DeleteGangMember extends WriteOnlyPacket {
   public DeleteGangMember() {
      super(10603);
   }

   public DeleteGangMember(long memberId) {
      super(10603);

      try {
         this.writeByte(1);
         this.writeDouble((double)memberId);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void deleteAllMembers(Gang gang, Player player) {
      DeleteGangMember dm = new DeleteGangMember();

      try {
         dm.writeByte(gang.getMemberSize());
         Iterator it = gang.getMemberMap().values().iterator();

         while(it.hasNext()) {
            dm.writeDouble((double)((GangMember)it.next()).getId());
         }

         player.writePacket(dm);
         dm.destroy();
         dm = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
