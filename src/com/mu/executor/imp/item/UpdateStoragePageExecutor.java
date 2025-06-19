package com.mu.executor.imp.item;

import com.mu.db.manager.ItemDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.item.container.imp.Backpack;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class UpdateStoragePageExecutor extends Executable {
   public UpdateStoragePageExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      ItemDBManager.updateStorage(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      try {
         Player player = (Player)obj[0];
         Storage storage = (Storage)obj[1];
         long roleID = player.getID();
         int storageType = storage.getType();
         int page = storage.getPage();
         int cooledCount = 0;
         int cooledTime = 0;
         if (storage.getType() == 1) {
            Backpack backpack = (Backpack)storage;
            cooledCount = backpack.getCooledCount();
            cooledTime = backpack.getCurGridTime() + (int)(System.currentTimeMillis() - backpack.getLastCheckTime());
         }

         packet.writeLong(roleID);
         packet.writeByte(storageType);
         packet.writeShort(page);
         packet.writeShort(cooledCount);
         packet.writeInt(cooledTime);
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }
}
