package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;

public class AddGangMember extends WriteOnlyPacket {
   public AddGangMember() {
      super(10601);
   }

   public AddGangMember(GangMember member) {
      super(10601);

      try {
         this.writeByte(1);
         writeMemberInfo(this, member);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void addOneMember(Player player, GangMember member) {
      try {
         AddGangMember am = new AddGangMember(member);
         player.writePacket(am);
         am.destroy();
         am = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void pushAllGangMembers(Player player) {
      try {
         Gang gang = player.getGang();
         if (gang == null) {
            return;
         }

         AddGangMember am = new AddGangMember();
         am.writeByte(gang.getMemberSize());
         Iterator it = gang.getMemberMap().values().iterator();

         while(it.hasNext()) {
            writeMemberInfo(am, (GangMember)it.next());
         }

         player.writePacket(am);
         am.destroy();
         am = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void writeMemberInfo(WriteOnlyPacket packet, GangMember member) {
      try {
         int[] icons = member.getBlueIcons();
         packet.writeDouble((double)member.getId());
         packet.writeUTF(member.getName());
         packet.writeShort(member.getLevel());
         packet.writeShort(icons[0]);
         packet.writeShort(icons[1]);
         packet.writeByte(member.getProfession());
         packet.writeByte(member.getPost());
         packet.writeUTF(GangManager.getPostName(member.getPost()));
         packet.writeBoolean(member.isOnline());
         if (!member.isOnline()) {
            packet.writeDouble((double)member.getOfflineTime());
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
