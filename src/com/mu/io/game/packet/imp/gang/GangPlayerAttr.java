package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class GangPlayerAttr extends WriteOnlyPacket {
   public GangPlayerAttr() {
      super(10637);
   }

   public static void writeMemberAttr(GangMember member, WriteOnlyPacket packet) throws Exception {
      if (member == null) {
         packet.writeBoolean(false);
      } else {
         Gang gang = member.getGang();
         packet.writeBoolean(true);
         packet.writeDouble((double)gang.getId());
         packet.writeUTF(gang.getName());
         packet.writeShort(gang.getFlagId());
         packet.writeByte(member.getPost());
      }

   }

   public static void pushAttr(Player player) {
      GangPlayerAttr ga = new GangPlayerAttr();

      try {
         writeMemberAttr(GangManager.getMember(player.getID()), ga);
         player.writePacket(ga);
         ga.destroy();
         ga = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
