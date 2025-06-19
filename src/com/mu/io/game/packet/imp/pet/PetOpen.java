package com.mu.io.game.packet.imp.pet;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class PetOpen extends ReadAndWritePacket {
   public PetOpen(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }

   public static void sendPetOpen(Player player) {
      try {
         PetOpen packet = new PetOpen(43006, (byte[])null);
         packet.writeBoolean(true);
         player.writePacket(packet);
         packet.destroy();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
