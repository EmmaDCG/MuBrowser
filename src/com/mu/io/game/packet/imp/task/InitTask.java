package com.mu.io.game.packet.imp.task;

import com.mu.io.game.packet.ReadAndWritePacket;

public class InitTask extends ReadAndWritePacket {
   public InitTask(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitTask() {
      super(40000, (byte[])null);
   }

   public void process() throws Exception {
      this.getPlayer().getTaskManager().initTask(this);
   }
}
