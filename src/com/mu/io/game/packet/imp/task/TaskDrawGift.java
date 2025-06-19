package com.mu.io.game.packet.imp.task;

import com.mu.io.game.packet.ReadAndWritePacket;

public class TaskDrawGift extends ReadAndWritePacket {
   public TaskDrawGift(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int giftId = this.readByte();
   }
}
