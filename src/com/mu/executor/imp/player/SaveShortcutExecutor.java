package com.mu.executor.imp.player;

import com.mu.db.manager.ShortcutDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.shortcut.Shortcut;
import com.mu.game.model.unit.player.shortcut.ShortcutEntry;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.util.HashMap;
import java.util.Iterator;

public class SaveShortcutExecutor extends Executable {
   public SaveShortcutExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      ShortcutDBManager.saveShortcut(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      Shortcut shortcut = player.getShortcut();
      packet.writeLong(player.getID());
      HashMap entries = shortcut.getEntries();
      int delSize = 10 - entries.size();
      packet.writeByte(delSize);

      for(int i = 0; i < 10; ++i) {
         if (!entries.containsKey(i)) {
            packet.writeByte(i);
         }
      }

      packet.writeByte(entries.size());
      Iterator var8 = entries.values().iterator();

      while(var8.hasNext()) {
         ShortcutEntry entry = (ShortcutEntry)var8.next();
         packet.writeByte(entry.getPosition());
         packet.writeByte(entry.getType());
         packet.writeInt(entry.getModelID());
      }

   }
}
