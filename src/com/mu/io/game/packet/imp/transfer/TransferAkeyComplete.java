package com.mu.io.game.packet.imp.transfer;

import com.mu.game.model.transfer.Transfer;
import com.mu.game.model.transfer.TransferConfigManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.TransferPopup;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;

public class TransferAkeyComplete extends ReadAndWritePacket {
   public TransferAkeyComplete(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Transfer transfer = TransferConfigManager.getWillTransfer(player.getProType(), player.getProLevel());
      if (transfer != null && player.getLevel() >= transfer.getLevel() && transfer.isAKeyComplete()) {
         ShowPopup.open(this.getPlayer(), new TransferPopup(this.getPlayer().createPopupID(), transfer));
      }

   }
}
