package com.mu.io.game.packet.imp.pet;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class InitPet extends ReadAndWritePacket {
   public InitPet(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitPet() {
      super(43001, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      player.getPetManager().init(this);
   }
}
