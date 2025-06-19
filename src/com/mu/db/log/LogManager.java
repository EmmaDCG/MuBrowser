package com.mu.db.log;

import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class LogManager {
   public static void addIngotLog(Player player, int ingot, int addType) {
      WriteOnlyPacket packet = Executor.AddIngotLog.toPacket(player, ingot, addType);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public static void reduceIngotLog(Player player, int ingot, int reduceType, String reduceDetail) {
      WriteOnlyPacket packet = Executor.ReduceIngotLog.toPacket(player, ingot, reduceType, reduceDetail);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public static void addBindIngotLog(Player player, int bindIngot, int addType) {
      WriteOnlyPacket packet = Executor.AddBindIngotLog.toPacket(player, bindIngot, addType);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public static void reduceBindIngotLog(Player player, int ingot, int reduceType, String reduceDetail) {
      WriteOnlyPacket packet = Executor.ReduceBindIngotLog.toPacket(player, ingot, reduceType, reduceDetail);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
