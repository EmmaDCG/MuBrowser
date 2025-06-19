package com.mu.executor.imp.item;

import com.mu.db.manager.ItemDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.item.Item;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class DeleteItemExecutor extends Executable {
   public DeleteItemExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      ItemDBManager.deleteItem(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      try {
         Item item = (Item)obj[1];
         packet.writeLong(item.getID());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
