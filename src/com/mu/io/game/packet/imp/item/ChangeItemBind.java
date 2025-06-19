package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ChangeItemBind extends WriteOnlyPacket {
   public ChangeItemBind() {
      super(20004);
   }

   private void setData(long objId, int containerType, boolean bind) {
      try {
         this.writeByte(containerType);
         this.writeDouble((double)objId);
         this.writeBoolean(bind);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void sendToClient(Player player, long objId, int containerType, boolean bind) {
      ChangeItemBind di = new ChangeItemBind();
      di.setData(objId, containerType, bind);
      player.writePacket(di);
      di.destroy();
      di = null;
   }

   public static void sendToClient(Player player, Item item) {
      sendToClient(player, item.getID(), item.getContainerType(), item.isBind());
   }
}
