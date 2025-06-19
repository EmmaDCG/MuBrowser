package com.mu.io.game.packet.imp.team;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class TeamApplyInviteList extends WriteOnlyPacket {
   public static final int Type_Apply = 1;
   public static final int Type_Invite = 2;

   public TeamApplyInviteList() {
      super(11011);
   }

   public static void addList(Player player, long id, String name, int header, int profession, int level, int type) {
      try {
         TeamApplyInviteList al = new TeamApplyInviteList();
         al.writeDouble((double)id);
         al.writeUTF(name);
         al.writeShort(header);
         al.writeByte(profession);
         al.writeShort(level);
         al.writeByte(type);
         player.writePacket(al);
         al.destroy();
         al = null;
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }
}
