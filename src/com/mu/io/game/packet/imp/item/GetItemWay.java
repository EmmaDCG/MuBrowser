package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.GetItemWayManager;
import com.mu.io.game.packet.ReadAndWritePacket;

public class GetItemWay extends ReadAndWritePacket {
   public GetItemWay(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public GetItemWay() {
      super(20032, (byte[])null);
   }

   public void process() throws Exception {
      int id = this.readInt();
      GetItemWayManager.writeWay(this.getPlayer(), id);
   }
}
