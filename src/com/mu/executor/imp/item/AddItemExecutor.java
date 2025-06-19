package com.mu.executor.imp.item;

import com.mu.config.Global;
import com.mu.db.manager.GameDBManager;
import com.mu.db.manager.ItemDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.concurrent.ThreadCachedPoolManager;

public class AddItemExecutor extends Executable {
   public AddItemExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      ItemDBManager.insertItem(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      try {
         Player player = (Player)obj[0];
         Item item = (Item)obj[1];
         packet.writeLong(player.getID());
         packet.writeLong(item.getID());
         packet.writeInt(item.getModelID());
         packet.writeByte(item.getQuality());
         packet.writeInt(item.getCount());
         packet.writeShort(item.getSlot());
         packet.writeByte(item.getContainerType());
         packet.writeByte(item.getStarLevel());
         packet.writeByte(item.getSocket());
         packet.writeBoolean(item.isBind());
         packet.writeInt(item.getMoney());
         packet.writeByte(item.getMoneyType());
         packet.writeInt(item.getStarUpTimes());
         packet.writeByte(item.getOnceMaxStarLevel());
         packet.writeLong(item.getExpireTime());
         packet.writeShort(item.getDurability());
         packet.writeUTF(item.getBasisStr());
         packet.writeUTF(item.getOtherStr());
         packet.writeUTF(item.getStoneStr());
         packet.writeUTF(item.getRuneStr());
         packet.writeByte(item.getZhuijiaLevel());
         final long itemId = item.getID();
         if (Global.isInterServiceServer()) {
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  GameDBManager.insertInterMaxId(1, itemId);
               }
            });
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
