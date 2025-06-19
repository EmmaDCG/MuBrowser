package com.mu.io.game.packet.imp.pet;

import com.mu.game.model.pet.PlayerPetManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class PetAttributeRise extends ReadAndWritePacket {
   public PetAttributeRise(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int statId = this.readShort();
      int count = this.readByte();
      Player player = this.getPlayer();
      PlayerPetManager ppm = null;
      if (player != null && (ppm = player.getPetManager()) != null) {
         boolean result = ppm.riseAttribute(statId);
         this.writeBoolean(result);
         player.writePacket(this);
      }
   }
}
