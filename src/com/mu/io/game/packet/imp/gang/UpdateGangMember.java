package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;

public class UpdateGangMember extends WriteOnlyPacket {
   public UpdateGangMember() {
      super(10602);
   }

   public UpdateGangMember(GangMember member) {
      super(10602);

      try {
         this.writeDouble((double)member.getId());
         AddGangMember.writeMemberInfo(this, member);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void updateGangMember(long rid, Gang gang) {
      if (gang != null) {
         GangMember member = gang.getMember(rid);
         if (member != null) {
            try {
               UpdateGangMember um = new UpdateGangMember();
               GangBaseInfo.writeMemberInfo(member, um);
               Iterator it = gang.getMemberMap().values().iterator();

               while(it.hasNext()) {
                  GangMember m = (GangMember)it.next();
                  Player p = m.getPlayer();
                  if (p != null && p.isEnterMap()) {
                     p.writePacket(um);
                  }
               }

               um.destroy();
               um = null;
            } catch (Exception var8) {
               var8.printStackTrace();
            }

         }
      }
   }
}
