package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.QuitGangPop;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;

public class QuitGang extends ReadAndWritePacket {
   public QuitGang(long rid) {
      super(10618, (byte[])null);

      try {
         this.writeDouble((double)rid);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public QuitGang(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      if (gang != null) {
         boolean isDisbind = gang.getMasterId() == player.getID();
         QuitGangPop pop = new QuitGangPop(player.createPopupID(), isDisbind);
         ShowPopup.open(player, pop);
      }

   }
}
