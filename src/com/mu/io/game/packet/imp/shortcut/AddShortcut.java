package com.mu.io.game.packet.imp.shortcut;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.shortcut.ShortcutEntry;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;

public class AddShortcut extends WriteOnlyPacket {
   public AddShortcut(HashMap entries) {
      super(10211);

      try {
         this.writeByte(entries.size());
         Iterator var3 = entries.values().iterator();

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

   public static void sendToClient(Player player, HashMap entries) {
      AddShortcut asc = new AddShortcut(entries);
      player.writePacket(asc);
      asc.destroy();
      asc = null;
   }

   public static void sendToClient(Player player, ShortcutEntry entry) {
      HashMap entries = new HashMap();
      entries.put(entry.getPosition(), entry);
      sendToClient(player, entries);
      entries.clear();
   }
}
