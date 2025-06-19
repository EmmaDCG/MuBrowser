package com.mu.io.game.packet.imp.material;

import com.mu.game.model.unit.material.Material;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class CollectionMaterial extends ReadAndWritePacket {
   public CollectionMaterial(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      long id = (long)this.readDouble();
      Player player = this.getPlayer();
      Material mt = player.getMap().getMaterial(id);
      if (mt == null) {
         SystemMessage.writeMessage(player, 10001);
         this.writeBoolean(false);
         player.writePacket(this);
      } else {
         player.stopRidingStatus();
         int result = mt.startGather(player);
         if (result != 1) {
            SystemMessage.writeMessage(player, result);
            this.writeBoolean(false);
         } else {
            player.startCountDown(mt);
            this.writeBoolean(true);
         }

         player.writePacket(this);
      }
   }
}
