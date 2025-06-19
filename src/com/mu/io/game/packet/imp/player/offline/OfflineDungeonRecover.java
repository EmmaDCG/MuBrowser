package com.mu.io.game.packet.imp.player.offline;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.RecoverPopup;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;

public class OfflineDungeonRecover extends ReadAndWritePacket {
   public OfflineDungeonRecover(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int id = this.readByte();
      int times = this.readUnsignedByte();
      ShowPopup.open(player, new RecoverPopup(player.createPopupID(), id, times));
   }
}
