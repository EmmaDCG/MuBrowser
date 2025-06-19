package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.GangManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;

public class GangRequestZml extends ReadAndWritePacket {
   public GangRequestZml(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Item zml = GangManager.getZmlItem();
      GetItemStats.writeItem(zml, this);
      this.writeUTF(GangManager.getZmlDes());
      player.writePacket(this);
   }
}
