package com.mu.io.game.packet.imp.player;

import com.mu.io.game.packet.ReadAndWritePacket;

public class PlayerModulePacket extends ReadAndWritePacket {
   public PlayerModulePacket(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public PlayerModulePacket() {
      super(110, (byte[])null);
   }

   public void process() throws Exception {
   }
}
