package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class DeleteItemByServer extends WriteOnlyPacket {
   public DeleteItemByServer() {
      super(13011);
   }

   public static void deletItem(Player owner, Player target, int index, long itemId) {
      try {
         DeleteItemByServer ds = new DeleteItemByServer();
         ds.writeByte(index);
         ds.writeByte(1);
         ds.writeDouble((double)itemId);
         owner.writePacket(ds);
         ds.destroy();
         ds = null;
         ds = new DeleteItemByServer();
         ds.writeByte(index);
         ds.writeByte(2);
         ds.writeDouble((double)itemId);
         target.writePacket(ds);
         ds.destroy();
         ds = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void deletItem(Player owner, int index, long itemId) {
      try {
         DeleteItemByServer ds = new DeleteItemByServer();
         ds.writeByte(index);
         ds.writeByte(1);
         ds.writeDouble((double)itemId);
         owner.writePacket(ds);
         ds.destroy();
         ds = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
