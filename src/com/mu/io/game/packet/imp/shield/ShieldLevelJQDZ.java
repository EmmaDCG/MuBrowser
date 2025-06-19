package com.mu.io.game.packet.imp.shield;

import com.mu.game.model.mall.MallConfigManager;
import com.mu.game.model.mall.MallItemData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.mall.MallShowItem;

public class ShieldLevelJQDZ extends ReadAndWritePacket {
   public ShieldLevelJQDZ(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      MallItemData md = MallConfigManager.getData(player.getShieldManager().getLevel().getMallItemId());
      if (md != null) {
         MallShowItem.show(player, md.getItem().getID(), 1);
      }
   }
}
