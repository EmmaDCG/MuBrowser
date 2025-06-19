package com.mu.io.game.packet.imp.collection;

import com.mu.game.model.activity.imp.collection.Collection;
import com.mu.game.model.item.Item;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.Iterator;

public class CollectionInfo extends ReadAndWritePacket {
   public CollectionInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.writeUTF(Collection.getName());
      this.writeUTF(Collection.getUrl());
      ArrayList list = Collection.getRewardList();
      this.writeByte(list.size());
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         Item item = (Item)var3.next();
         GetItemStats.writeItem(item, this);
      }

      this.getPlayer().writePacket(this);
   }
}
