package com.mu.io.game.packet.imp.item;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;
import java.util.List;

public class DeleteItem extends WriteOnlyPacket {
   public DeleteItem(int code) {
      super(code);
   }

   public DeleteItem() {
      super(20002);
   }

   private void setData(long objId, int containerType) {
      try {
         this.writeByte(containerType);
         this.writeShort(1);
         this.writeDouble((double)objId);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void setData(List objIds, int containerType) {
      try {
         this.writeByte(containerType);
         this.writeShort(objIds.size());
         Iterator var4 = objIds.iterator();

         while(var4.hasNext()) {
            Long objId = (Long)var4.next();
            this.writeDouble((double)objId.longValue());
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendToClient(Player player, long objId, int containerType) {
      DeleteItem di = new DeleteItem();
      di.setData(objId, containerType);
      player.writePacket(di);
      di.destroy();
      di = null;
   }

   public static void sendToClient(Player player, List objIds, int containerType) {
      DeleteItem di = new DeleteItem();
      di.setData(objIds, containerType);
      player.writePacket(di);
      di.destroy();
      di = null;
   }
}
