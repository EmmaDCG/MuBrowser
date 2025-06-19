package com.mu.io.game.packet.imp.vip;

import com.mu.game.model.mall.MallConfigManager;
import com.mu.game.model.mall.MallItemData;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.vip.VIPConfigManager;
import com.mu.game.model.vip.VIPData;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.mall.MallShowItem;

public class VIPOpen extends ReadAndWritePacket {
   public VIPOpen(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int vipId = this.readByte();
      VIPData vd = VIPConfigManager.getVIP(vipId);
      MallItemData md = MallConfigManager.getData(vd.getItemId());
      Player player = this.getPlayer();
      MallShowItem.show(player, md.getItem().getID(), 1);
   }
}
