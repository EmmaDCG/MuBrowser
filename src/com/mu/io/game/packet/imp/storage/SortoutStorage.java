package com.mu.io.game.packet.imp.storage;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.List;

public class SortoutStorage extends ReadAndWritePacket {
   public SortoutStorage(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int containerType = this.readByte();
      player.getItemManager().sortoutStorage(containerType);
   }

   public SortoutStorage(int containerType, List itemList) {
      super(20005, (byte[])null);

      try {
         this.writeByte(containerType);
         this.writeShort(itemList.size());
         Iterator var4 = itemList.iterator();

         while(var4.hasNext()) {
            Item item = (Item)var4.next();
            this.writeDouble((double)item.getID());
            this.writeShort(item.getSlot());
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void itemMoveResult(int containerType, List itemList, Player player) {
      if (itemList != null && itemList.size() != 0) {
         SortoutStorage mr = new SortoutStorage(containerType, itemList);
         player.writePacket(mr);
         mr.destroy();
         mr = null;
      }
   }
}
