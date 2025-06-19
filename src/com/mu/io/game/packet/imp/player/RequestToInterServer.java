package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class RequestToInterServer extends ReadAndWritePacket {
   public RequestToInterServer(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public RequestToInterServer() {
      super(111, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      player.toRemoteServer();
   }
}
