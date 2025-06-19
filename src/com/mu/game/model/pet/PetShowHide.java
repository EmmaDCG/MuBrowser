package com.mu.game.model.pet;

import com.mu.io.game.packet.ReadAndWritePacket;

public class PetShowHide extends ReadAndWritePacket {
   public PetShowHide(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.getPlayer().getPetManager().control();
   }
}
