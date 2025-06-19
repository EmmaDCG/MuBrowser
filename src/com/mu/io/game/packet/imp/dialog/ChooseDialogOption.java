package com.mu.io.game.packet.imp.dialog;

import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ChooseDialogOption extends ReadAndWritePacket {
   public ChooseDialogOption(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (player != null) {
         long id = (long)this.readDouble();
         Npc npc = player.getMap().getNpc(id);
         if (npc != null) {
            int optionId = this.readShort();
            npc.onDialogChooseOption(player, optionId);
         }
      }
   }
}
