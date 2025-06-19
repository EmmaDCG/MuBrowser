package com.mu.executor.imp.log;

import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.schedule.log.ItemLogTask;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class ItemLogExecutor extends Executable {
   public ItemLogExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      ItemLogTask.addItemLog(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      int type = ((Integer)obj[1]).intValue();
      Item item = (Item)obj[2];
      int count = ((Integer)obj[3]).intValue();
      int source = ((Integer)obj[4]).intValue();
      packet.writeByte(type);
      packet.writeLong(item.getID());
      packet.writeInt(item.getModelID());
      packet.writeUTF(item.getName());
      packet.writeByte(item.getQuality());
      packet.writeInt(count);
      packet.writeByte(item.getStarLevel());
      packet.writeByte(item.getZhuijiaLevel());
      packet.writeByte(item.getSocket());
      packet.writeBoolean(item.isBind());
      packet.writeLong(item.getExpireTime());
      packet.writeInt(item.getDurability());
      packet.writeUTF(item.isEquipment() ? item.getOtherStr() : item.getBasisStr());
      packet.writeUTF(item.getStoneStr());
      packet.writeUTF(item.getRuneStr());
      packet.writeLong(player.getID());
      packet.writeUTF(player.getName());
      packet.writeByte(source);
   }

   public static void createLogPacket(Player player, Item item, int type, int operatonCount, int source) {
      if (item != null && item.getModel().getSort() != 3) {
         if (item.getModel().getSort() != 1 || item.getOtherStats().size() >= 1 || item.getStarLevel() >= 1 || item.getZhuijiaLevel() >= 1 || item.getSocket() >= 1 || item.getRunes().size() >= 1) {
            WriteOnlyPacket packet = Executor.ItemLog.toPacket(player, type, item, operatonCount, source);
            player.writePacket(packet);
            packet.destroy();
            packet = null;
         }
      }
   }
}
