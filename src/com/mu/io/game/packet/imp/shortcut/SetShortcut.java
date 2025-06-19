package com.mu.io.game.packet.imp.shortcut;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.shortcut.Shortcut;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class SetShortcut extends ReadAndWritePacket {
   public SetShortcut(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int position = this.readByte();
      int type = this.readByte();
      int modelID = this.readInt();
      Shortcut shortcut = player.getShortcut();
      int result = shortcut.addSkillEntry(position, type, modelID);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }
}
