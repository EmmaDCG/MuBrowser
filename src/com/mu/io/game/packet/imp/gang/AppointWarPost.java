package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class AppointWarPost extends ReadAndWritePacket {
   public AppointWarPost(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      if (gang != null) {
         if (gang.isWinner()) {
            if (player.getID() != gang.getMasterId()) {
               SystemMessage.writeMessage(player, 9006);
            } else {
               long id = (long)this.readDouble();
               String name = this.readUTF();
               int wp = this.readByte();
               if (wp == 1) {
                  wp = 2;
               } else {
                  wp = 3;
               }

               boolean appoint = this.readBoolean();
               if (id == player.getID()) {
                  SystemMessage.writeMessage(player, 9015);
               } else {
                  GangMember member;
                  if (!appoint) {
                     member = gang.getMember(id);
                     if (member == null) {
                        SystemMessage.writeMessage(player, 9014);
                        return;
                     }

                     if (member.getWarPost() != 2 && member.getWarPost() != 3) {
                        SystemMessage.writeMessage(player, 9053);
                        return;
                     }

                     gang.doOperation(player, 16, member);
                  } else {
                     if (wp != 2 && wp != 3) {
                        SystemMessage.writeMessage(player, 9051);
                        return;
                     }

                     member = gang.getMemberByName(name);
                     if (member == null) {
                        SystemMessage.writeMessage(player, 9014);
                        return;
                     }

                     if (member.getWarPost() != 0) {
                        SystemMessage.writeMessage(player, 9052);
                        return;
                     }

                     gang.doOperation(player, 15, member, Integer.valueOf(wp));
                  }

               }
            }
         }
      }
   }
}
