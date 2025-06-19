package com.mu.io.game.packet.imp.npc;

import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ChatWithNpc extends ReadAndWritePacket {
   public ChatWithNpc(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long id = (long)this.readDouble();
      Npc npc = player.getMap().getNpc(id);
      if (npc != null) {
         npc.onDialogRequest(player);
      }

   }
}
