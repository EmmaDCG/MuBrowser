package com.mu.io.game.packet.imp.storage;

import com.mu.game.model.item.container.DeportExpandData;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.ExpandDeportPopup;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class DeportExpand extends ReadAndWritePacket {
   public DeportExpand(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      DeportExpandData exData = DeportExpandData.getDeportExpandData(player.getDepot().getPage());
      if (exData == null) {
         SystemMessage.writeMessage(player, 2002);
      } else {
         ExpandDeportPopup pop = new ExpandDeportPopup(player.createPopupID(), 1, exData);
         ShowPopup.open(player, pop);
      }
   }
}
