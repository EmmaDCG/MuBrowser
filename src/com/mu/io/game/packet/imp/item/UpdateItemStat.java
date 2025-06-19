package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;
import java.util.List;

public class UpdateItemStat extends WriteOnlyPacket {
   public UpdateItemStat(Item item) {
      super(20021);

      try {
         this.writeByte(item.getContainerType());
         this.writeShort(1);
         this.writeDouble((double)item.getID());
         GetItemStats.writeProps(item, this);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public UpdateItemStat(int containerType, List items) {
      super(20021);

      try {
         this.writeByte(containerType);
         this.writeShort(items.size());
         Iterator var4 = items.iterator();

         while(var4.hasNext()) {
            Item item = (Item)var4.next();
            this.writeDouble((double)item.getID());
            GetItemStats.writeProps(item, this);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendToClient(Player player, Item item) {
      UpdateItemStat uis = new UpdateItemStat(item);
      player.writePacket(uis);
      uis.destroy();
      uis = null;
   }

   public static void sendToClient(Player player, List items, int containerType) {
      UpdateItemStat uis = new UpdateItemStat(containerType, items);
      player.writePacket(uis);
      uis.destroy();
      uis = null;
   }
}
