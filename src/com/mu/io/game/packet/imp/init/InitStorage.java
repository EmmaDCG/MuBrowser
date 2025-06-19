package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.ItemDBManager;
import com.mu.game.model.item.container.imp.Backpack;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;
import java.util.List;

public class InitStorage extends ReadAndWritePacket {
   public InitStorage(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static WriteOnlyPacket initStorages(long roleID) {
      List datas = ItemDBManager.searchStorage(roleID);
      WriteOnlyPacket packet = new InitStorage(datas);
      datas.clear();
      return packet;
   }

   public InitStorage(List storageList) {
      super(59001, (byte[])null);

      try {
         this.writeByte(storageList.size());
         Iterator var3 = storageList.iterator();

         while(var3.hasNext()) {
            int[] entry = (int[])var3.next();
            this.writeByte((byte)entry[0]);
            this.writeShort((short)entry[1]);
            this.writeShort((short)entry[2]);
            this.writeInt(entry[3]);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int size = this.readByte();

      for(int i = 0; i < size; ++i) {
         int type = this.readByte();
         int page = this.readShort();
         int cooledCount = this.readShort();
         int cooledTime = this.readInt();
         Storage storage = player.getStorage(type);
         if (storage != null) {
            if (type == 1) {
               ((Backpack)storage).reset(page, cooledCount, cooledTime);
            } else {
               storage.reset(page);
            }
         }
      }

   }
}
