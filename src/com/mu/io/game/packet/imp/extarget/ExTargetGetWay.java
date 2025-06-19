package com.mu.io.game.packet.imp.extarget;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.extarget.ExTargetElement;
import com.mu.game.model.unit.player.extarget.ExtargetManager;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ExTargetGetWay extends ReadAndWritePacket {
   public ExTargetGetWay(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public ExTargetGetWay() {
      super(20503, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int bid = this.readByte();
      int index = this.readByte();
      ExTargetElement element = ExtargetManager.getElement(player.getProType(), bid, index);
      if (element != null) {
         player.writePacket(element.getPacket());
      }

   }
}
