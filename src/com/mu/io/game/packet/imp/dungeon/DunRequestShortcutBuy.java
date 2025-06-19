package com.mu.io.game.packet.imp.dungeon;

import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.io.game.packet.ReadAndWritePacket;

public class DunRequestShortcutBuy extends ReadAndWritePacket {
   public DunRequestShortcutBuy(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int tid = this.readByte();
      int key = this.readInt();
      DungeonTemplate template = DungeonTemplateFactory.getTemplate(tid);
      if (template != null) {
         template.writeShortcutBuyTicket(this.getPlayer(), key);
      }

   }
}
