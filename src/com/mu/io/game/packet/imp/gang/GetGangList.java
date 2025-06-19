package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.GangManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class GetGangList extends ReadAndWritePacket {
   public GetGangList() {
      super(10606, (byte[])null);
   }

   public GetGangList(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int page = this.readUnsignedShort();
      String key = this.readUTF();
      GangManager.pushGangList(player, page, key);
   }
}
