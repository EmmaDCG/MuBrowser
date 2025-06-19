package com.mu.executor.imp.log;

import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.schedule.log.ItemLogTask;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ItemForgingExecutor extends Executable {
   public ItemForgingExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      ItemLogTask.addItemForgingLog(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      int type = ((Integer)obj[1]).intValue();
      Item item = (Item)obj[2];
      int preLevel = ((Integer)obj[3]).intValue();
      int status = ((Integer)obj[4]).intValue();
      int reduceIngot = ((Integer)obj[5]).intValue();
      int reduceMoney = ((Integer)obj[6]).intValue();
      packet.writeByte(type);
      packet.writeLong(item.getID());
      packet.writeInt(item.getModelID());
      packet.writeUTF(item.getName());
      packet.writeByte(item.getQuality());
      packet.writeByte(preLevel);
      int newLevel = item.getStarLevel();
      switch(type) {
      case 1:
         newLevel = item.getZhuijiaLevel();
         break;
      default:
         newLevel = item.getStarLevel();
      }

      packet.writeByte(newLevel);
      packet.writeUTF(item.getOtherStr());
      packet.writeUTF(item.getStoneStr());
      packet.writeUTF(item.getRuneStr());
      packet.writeByte(status);
      packet.writeInt(reduceIngot);
      packet.writeInt(reduceMoney);
      packet.writeLong(player.getID());
      packet.writeUTF(player.getName());
   }

   public static void logFoging(Player player, Item item, int preLevel, int type, boolean status, int reduceType, int reduceMoney) {
      if (item != null) {
         WriteOnlyPacket packet = Executor.ItemForgingLog.toPacket(player, type, item, preLevel, status ? 1 : 0, reduceType, reduceMoney);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      }
   }
}
