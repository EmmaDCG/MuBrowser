package com.mu.io.game.packet.imp.item;

import com.mu.game.model.shop.ShopConfigure;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.npc.OpenNpcShop;

public class OpenVipShop extends ReadAndWritePacket {
   public OpenVipShop(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      openVipShop(player);
   }

   public static void openVipShop(Player player) {
      OpenNpcShop.sendToClient(player, false, 10000, ShopConfigure.getShopTypeName(3));
   }
}
