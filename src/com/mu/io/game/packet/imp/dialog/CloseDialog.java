package com.mu.io.game.packet.imp.dialog;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.io.IOException;

public class CloseDialog extends ReadAndWritePacket {
   public CloseDialog(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public CloseDialog() {
      super(10403, (byte[])null);
   }

   public void process() throws Exception {
   }

   public static void close(Player player) {
      try {
         CloseDialog cd = new CloseDialog();
         cd.writeBoolean(true);
         player.writePacket(cd);
         cd.destroy();
         cd = null;
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }
}
