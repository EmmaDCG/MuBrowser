package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class FrameCheck extends ReadAndWritePacket {
   public FrameCheck(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public FrameCheck() {
      super(10202, (byte[])null);
   }

   public void process() throws Exception {
   }

   public static void writeHeartbeet(Player player) {
      try {
         FrameCheck hb = new FrameCheck();
         hb.writeDouble((double)System.currentTimeMillis());
         player.writePacket(hb);
         player.setHeartSendTime(System.currentTimeMillis());
         hb.destroy();
         hb = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
