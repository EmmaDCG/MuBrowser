package com.mu.io.game.packet.imp.magicItem;

import com.mu.game.model.item.box.magic.MagicManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.List;

public class OperateMagicItem extends ReadAndWritePacket {
   public OperateMagicItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int count = this.readByte();
      List indexList = MagicManager.operateMagic(player, count);
      boolean success = true;
      if (indexList == null) {
         success = false;
      }

      this.writeBoolean(success);
      if (success) {
         this.writeByte(indexList.size());
         Iterator var6 = indexList.iterator();

         while(var6.hasNext()) {
            Integer index = (Integer)var6.next();
            this.writeByte(index.intValue());
         }
      }

      player.writePacket(this);
   }
}
