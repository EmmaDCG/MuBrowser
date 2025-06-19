package com.mu.io.game.packet.imp.chat;

import com.mu.config.Global;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.BottomMessage;
import com.mu.utils.DFA;

public class ReceivePrivateMessage extends ReadAndWritePacket {
   public static final int Type_Persinal = 1;
   public static final int Type_Gang = 2;

   public ReceivePrivateMessage(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (Global.isJY(player.getID())) {
         BottomMessage.pushMessage(player, 17004);
      } else {
         int type = this.readByte();
         if (type == 1) {
            long rid = (long)this.readDouble();
            if (rid == player.getID()) {
               return;
            }

            String msg = this.readUTF().trim();
            int size = this.readUnsignedShort();
            byte[] bytes = new byte[size];
            this.readBytes(bytes);
            if (!msg.trim().equals("")) {
               ChatProcess.processPersinal(player, rid, DFA.getDFAStr(msg), bytes);
            }
         } else {
            String msg = this.readUTF().trim();
            int size = this.readUnsignedShort();
            byte[] bytes = new byte[size];
            this.readBytes(bytes);
            ChatProcess.processGang(player, DFA.getDFAStr(msg), bytes);
         }

      }
   }
}
