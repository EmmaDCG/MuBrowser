package com.mu.io.game.packet.imp.chat;

import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class RequestSiLiao extends ReadAndWritePacket {
   public RequestSiLiao(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      long rid = (long)this.readDouble();
      Player target = CenterManager.getPlayerByRoleID(rid);
      if (target == null) {
         SystemMessage.writeMessage(this.getPlayer(), 1021);
      } else if (rid == this.getPlayer().getID()) {
         SystemMessage.writeMessage(this.getPlayer(), 17015);
      } else {
         this.writeDouble((double)target.getID());
         this.writeUTF(target.getName());
         this.writeShort(target.getHeader());
         this.getPlayer().writePacket(this);
      }

   }
}
