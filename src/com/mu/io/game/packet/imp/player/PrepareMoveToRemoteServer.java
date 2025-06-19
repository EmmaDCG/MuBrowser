package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class PrepareMoveToRemoteServer extends ReadAndWritePacket {
   public PrepareMoveToRemoteServer(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public PrepareMoveToRemoteServer() {
      super(109, (byte[])null);
   }

   public static PrepareMoveToRemoteServer prepareMoveToRemoteServer(int initType) {
      PrepareMoveToRemoteServer pr = new PrepareMoveToRemoteServer();

      try {
         pr.writeByte(initType);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return pr;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int initType = this.readByte();
      if (initType == 2) {
         player.setDestroyType(4);
      } else {
         player.setDestroyType(5);
      }

      player.setShouldDestroy(true);
   }
}
