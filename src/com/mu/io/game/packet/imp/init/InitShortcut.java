package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.ShortcutDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.shortcut.Shortcut;
import com.mu.game.model.unit.player.shortcut.ShortcutEntry;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;
import java.util.List;

public class InitShortcut extends ReadAndWritePacket {
   public InitShortcut(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static WriteOnlyPacket getInitShortcuts(long roleID) {
      List entries = ShortcutDBManager.searchShortcut(roleID);
      WriteOnlyPacket packet = new InitShortcut(entries);
      entries.clear();
      return packet;
   }

   public InitShortcut(List entries) {
      super(59003, (byte[])null);

      try {
         this.writeByte(entries.size());
         Iterator var3 = entries.iterator();

         while(var3.hasNext()) {
            ShortcutEntry entry = (ShortcutEntry)var3.next();
            this.writeByte(entry.getPosition());
            this.writeByte(entry.getType());
            this.writeInt(entry.getModelID());
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Shortcut shortcut = player.getShortcut();
      int size = this.readByte();

      for(int i = 0; i < size; ++i) {
         int position = this.readByte();
         int type = this.readByte();
         int modelID = this.readInt();
         shortcut.loadEntry(new ShortcutEntry(type, modelID, position));
      }

   }
}
