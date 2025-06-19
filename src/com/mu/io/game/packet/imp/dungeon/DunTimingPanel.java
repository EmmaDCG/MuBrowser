package com.mu.io.game.packet.imp.dungeon;

import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.io.game.packet.ReadAndWritePacket;

public class DunTimingPanel extends ReadAndWritePacket {
   public DunTimingPanel() {
      super(12021, (byte[])null);
   }

   public DunTimingPanel(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int id = this.readUnsignedByte();
      switch(id) {
      case 1:
         DungeonTemplateFactory.getTemplate(5).clickTimePanel(this.getPlayer());
         break;
      case 2:
         DungeonTemplateFactory.getTemplate(6).clickTimePanel(this.getPlayer());
         break;
      case 3:
         DungeonTemplateFactory.getTemplate(9).clickTimePanel(this.getPlayer());
         break;
      case 4:
         DungeonTemplateFactory.getTemplate(10).clickTimePanel(this.getPlayer());
      }

   }
}
