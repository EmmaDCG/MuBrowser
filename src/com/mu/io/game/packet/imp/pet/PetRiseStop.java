package com.mu.io.game.packet.imp.pet;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class PetRiseStop extends ReadAndWritePacket {
   public PetRiseStop(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      player.getPetManager().stopRise();
      this.writeBoolean(true);
      player.writePacket(this);
   }
}
