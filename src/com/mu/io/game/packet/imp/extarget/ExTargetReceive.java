package com.mu.io.game.packet.imp.extarget;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ExTargetReceive extends ReadAndWritePacket {
   public ExTargetReceive(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public ExTargetReceive() {
      super(20504, (byte[])null);
   }

   public void process() throws Exception {
      int id = this.readByte();
      this.getPlayer().getExtargetManager().receive(id);
   }

   public static void writeReceiveResult(Player player, int id, boolean result, long itemId, int receiveResult) {
      ExTargetReceive er = new ExTargetReceive();

      try {
         er.writeBoolean(result);
         if (result) {
            er.writeByte(id);
            er.writeByte(receiveResult);
            er.writeDouble((double)itemId);
         }

         player.writePacket(er);
         er.destroy();
         er = null;
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }
}
