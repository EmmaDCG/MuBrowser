package com.mu.game.model.packet;

import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class RolePacketService {
   public static void noticeGatewayWhenPotentialChange(Player player) {
      WriteOnlyPacket packet = Executor.SavePotentialChange.toPacket(player);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public static void noticeGatewaySaveFirstKill(Player player, int templateId) {
      WriteOnlyPacket packet = Executor.FirstKill.toPacket(player, templateId);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public static void noticeGatewaySaveLevelChange(Player player) {
      WriteOnlyPacket packet = Executor.SaveLevelChange.toPacket(player);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public static void noticeGatewayDeleteBuff(Player player, int buffModelID) {
      WriteOnlyPacket packet = Executor.DeleteBuff.toPacket(player, buffModelID);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public static void noticeGatewaySaveHangset(Player player) {
      WriteOnlyPacket packet = Executor.SaveHangset.toPacket(player);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public static void noticeGatewayTransfer(Player player) {
      WriteOnlyPacket packet = Executor.SaveTransfer.toPacket(player);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
