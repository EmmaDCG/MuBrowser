package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.ApplyInfo;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class GetGangApply extends ReadAndWritePacket {
   public GetGangApply(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public GetGangApply() {
      super(10612, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      if (gang != null) {
         GangMember member = gang.getMember(player.getID());
         if (member != null && (member.getPost() == 2 || member.getPost() == 1)) {
            pushGangApply(gang, player, this);
         }
      }
   }

   public static void pushhGangApply(Gang gang, Player player) {
      GetGangApply ga = new GetGangApply();
      pushGangApply(gang, player, ga);
      ga.destroy();
      ga = null;
   }

   public static void pushGangApply(Gang gang, Player player, WriteOnlyPacket packet) {
      try {
         ArrayList list = gang.getApplyList();
         packet.writeByte(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            ApplyInfo info = (ApplyInfo)var5.next();
            packet.writeDouble((double)info.getRoleId());
            packet.writeUTF(info.getName());
            int[] icons = info.getVipIcons();
            packet.writeShort(icons[0]);
            packet.writeShort(icons[1]);
            packet.writeShort(info.getLevel());
            packet.writeByte(info.getProfession());
            packet.writeDouble((double)info.getApplyTime());
         }

         player.writePacket(packet);
         list.clear();
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
