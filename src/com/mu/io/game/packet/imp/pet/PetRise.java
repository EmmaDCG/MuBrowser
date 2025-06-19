package com.mu.io.game.packet.imp.pet;

import com.mu.game.model.pet.PlayerPetManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class PetRise extends ReadAndWritePacket {
   public PetRise(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      boolean autoBuy = this.readBoolean();
      boolean autoRise = this.readBoolean();
      Player player = this.getPlayer();
      PlayerPetManager ppm = player.getPetManager();
      if (ppm != null) {
         ppm.rise(autoBuy, autoRise);
      }
   }
}
