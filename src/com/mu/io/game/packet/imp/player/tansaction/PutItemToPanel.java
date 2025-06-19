package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;

public class PutItemToPanel extends WriteOnlyPacket {
   public PutItemToPanel() {
      super(13009);
   }

   public static void putItemToPanel(Item item, Player owner, Player target, int index) {
      try {
         PutItemToPanel pt = new PutItemToPanel();
         GetItemStats.writeItem(item, pt);
         pt.writeByte(index);
         pt.writeByte(1);
         owner.writePacket(pt);
         pt.destroy();
         pt = null;
         pt = new PutItemToPanel();
         GetItemStats.writeItem(item, pt);
         pt.writeByte(index);
         pt.writeByte(2);
         target.writePacket(pt);
         pt.destroy();
         pt = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
