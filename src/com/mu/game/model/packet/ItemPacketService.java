package com.mu.game.model.packet;

import com.mu.executor.Executor;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ItemPacketService {
   public static void noticeGatewayAddItem(Player player, Item item) {
      WriteOnlyPacket packet = Executor.AddItem.toPacket(player, item);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public static void noticeGatewayUpdateItem(Player player, Item item, int updateType) {
      WriteOnlyPacket packet = Executor.UpdateItem.toPacket(item, updateType);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public static void noticeGatewayDeleteItem(Player player, Item item, int source) {
      WriteOnlyPacket packet = Executor.DeleteItem.toPacket(player, item, source);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public static void noticeGatewayUpdateStorage(Player player, Storage storage) {
      WriteOnlyPacket packet = Executor.UpdateStoragePage.toPacket(player, storage);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public static void noticeGatewayUpdateItemLimits(Player player, int modelID, int count) {
      WriteOnlyPacket packet = Executor.UpdateItemLimit.toPacket(player, modelID, count);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
