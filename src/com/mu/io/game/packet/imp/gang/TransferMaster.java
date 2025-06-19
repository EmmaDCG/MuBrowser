package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.TranseferMasterPopup;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;

public class TransferMaster extends ReadAndWritePacket {
   public TransferMaster(long rid, String name) {
      super(10615, (byte[])null);

      try {
         this.writeDouble((double)rid);
         this.writeUTF(name);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public TransferMaster(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      long rid = (long)this.readDouble();
      if (gang != null) {
         ShowPopup.open(player, new TranseferMasterPopup(player.createPopupID(), rid));
      }
   }
}
