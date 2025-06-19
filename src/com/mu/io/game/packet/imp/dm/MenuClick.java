package com.mu.io.game.packet.imp.dm;

import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.ui.dm.DynamicMenuManager;
import com.mu.io.game.packet.ReadAndWritePacket;

public class MenuClick extends ReadAndWritePacket {
   public MenuClick(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int id = this.readUnsignedByte();
      DynamicMenu menu = DynamicMenuManager.getMenu(id);
      if (menu != null) {
         menu.onClick(this.getPlayer());
      }

   }
}
