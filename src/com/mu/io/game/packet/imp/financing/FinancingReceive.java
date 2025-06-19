package com.mu.io.game.packet.imp.financing;

import com.mu.io.game.packet.ReadAndWritePacket;

public class FinancingReceive extends ReadAndWritePacket {
   public FinancingReceive(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int id = this.readUnsignedByte();
      boolean result = this.getPlayer().getFinancingManager().receiveReward(id);
      this.writeBoolean(result);
      if (result) {
         this.writeByte(id);
      }

      this.getPlayer().writePacket(this);
   }
}
