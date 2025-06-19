package com.mu.io.game.packet.imp.player.offline;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.offline.OfflineManager;
import com.mu.io.game.packet.ReadAndWritePacket;

public class GetOfflineBuffInfo extends ReadAndWritePacket {
   public GetOfflineBuffInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      this.writeShort(OfflineManager.getBuffInfo(player.getVipShowLevel()).getAddition());
      this.writeUTF(player.getOffLineManager().getOfflineTimeStr());
      this.writeUTF(OfflineManager.getBuffDes());
      this.writeUTF(OfflineManager.getBuffTips());
      player.writePacket(this);
   }
}
