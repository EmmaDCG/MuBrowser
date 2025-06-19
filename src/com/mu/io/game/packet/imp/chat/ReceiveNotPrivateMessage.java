package com.mu.io.game.packet.imp.chat;

import com.mu.config.Global;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.BottomMessage;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.DFA;

public class ReceiveNotPrivateMessage extends ReadAndWritePacket {
   private static long interval = 10000L;

   public ReceiveNotPrivateMessage(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long now = System.currentTimeMillis();
      if (now - player.getLastChatTime() < interval) {
         SystemMessage.writeMessage(player, 17014);
      } else {
         player.setLastChatTime(now);
         if (Global.isJY(player.getID())) {
            BottomMessage.pushMessage(player, 17004);
         } else {
            byte type = this.readByte();
            switch(type) {
            case 0:
               if (player.getLevel() < 30) {
                  SystemMessage.writeMessage(player, 17005);
                  return;
               }
               break;
            case 1:
               if (player.getLevel() < 50) {
                  SystemMessage.writeMessage(player, 17006);
                  return;
               }
            case 2:
            case 3:
            case 4:
            case 5:
            default:
               break;
            case 6:
               if (player.getLevel() < 80) {
                  SystemMessage.writeMessage(player, 17007);
                  return;
               }
            }

            String msg = this.readUTF().trim();
            int size = this.readUnsignedShort();
            byte[] bytes = new byte[size];
            this.readBytes(bytes);
            if (!msg.equals("")) {
               ChatProcess.process(type, this.getPlayer(), DFA.getDFAStr(msg), bytes);
            }

            bytes = null;
         }
      }
   }
}
