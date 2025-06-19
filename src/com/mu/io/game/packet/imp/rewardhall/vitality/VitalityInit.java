package com.mu.io.game.packet.imp.rewardhall.vitality;

import com.mu.io.game.packet.ReadAndWritePacket;

public class VitalityInit extends ReadAndWritePacket {
   public VitalityInit(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.getPlayer().getVitalityManager().init(this);
   }
}
