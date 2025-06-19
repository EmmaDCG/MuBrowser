package com.mu.io.game.packet.imp.player;

import com.mu.io.game.packet.ReadAndWritePacket;

public class Heartbeat extends ReadAndWritePacket {
   public Heartbeat() {
      super(10251, (byte[])null);
   }

   public Heartbeat(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.getPlayer().setReceiveHeartTime(System.currentTimeMillis());
   }
}
