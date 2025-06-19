package com.mu.io.game.packet.imp.collection;

import com.mu.game.model.activity.imp.collection.Collection;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class CollectionReceive extends ReadAndWritePacket {
   public CollectionReceive(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      boolean b = Collection.receive(player);
      this.writeBoolean(b);
      player.writePacket(this);
   }
}
