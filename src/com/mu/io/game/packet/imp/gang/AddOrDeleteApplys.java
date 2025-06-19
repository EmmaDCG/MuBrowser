package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.activity.ActivityChangeDigital;
import java.util.Iterator;

public class AddOrDeleteApplys extends WriteOnlyPacket {
   public AddOrDeleteApplys() {
      super(10610);
   }

   public static void applyChanged(Gang gang, long id) {
      try {
         AddOrDeleteApplys aa = new AddOrDeleteApplys();
         aa.writeDouble((double)id);
         Iterator it = gang.getMemberMap().values().iterator();

         while(true) {
            GangMember member;
            do {
               if (!it.hasNext()) {
                  aa.destroy();
                  aa = null;
                  return;
               }

               member = (GangMember)it.next();
            } while(member.getPost() != 2 && member.getPost() != 1);

            Player player = member.getPlayer();
            if (player != null) {
               player.writePacket(aa);
               ActivityChangeDigital.pushDigital(player, 10, gang.getApplySize());
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }
   }

   public static void applyChanged(Player player, long id) {
      try {
         AddOrDeleteApplys aa = new AddOrDeleteApplys();
         aa.writeDouble((double)id);
         player.writePacket(aa);
         aa.destroy();
         aa = null;
         ActivityChangeDigital.pushDigital(player, 10, player.getGang().getApplySize());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
