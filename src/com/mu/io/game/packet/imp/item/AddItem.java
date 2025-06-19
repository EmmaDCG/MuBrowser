package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;
import java.util.List;

public class AddItem extends WriteOnlyPacket {
   public AddItem(int code) {
      super(code);
   }

   public AddItem() {
      super(20001);
   }

   public void setData(Item item) {
      try {
         this.writeByte(item.getContainerType());
         this.writeShort(1);
         GetItemStats.writeItem(item, this);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void setData(List items, int containerType) {
      try {
         this.writeByte(containerType);
         this.writeShort(items.size());
         Iterator var4 = items.iterator();

         while(var4.hasNext()) {
            Item item = (Item)var4.next();
            GetItemStats.writeItem(item, this);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendToClient(Player player, Item item) {
      AddItem ai = new AddItem();
      ai.setData(item);
      player.writePacket(ai);
      ai.destroy();
      ai = null;
   }

   public static void sendToClient(Player player, List items, int containerType) {
      AddItem ai = new AddItem();
      ai.setData(items, containerType);
      player.writePacket(ai);
      ai.destroy();
      ai = null;
   }
}
