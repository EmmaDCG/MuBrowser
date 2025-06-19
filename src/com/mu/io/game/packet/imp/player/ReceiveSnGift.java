package com.mu.io.game.packet.imp.player;

import com.mu.game.model.gift.GiftSnManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.utils.concurrent.ThreadCachedPoolManager;

public class ReceiveSnGift extends ReadAndWritePacket {
   public ReceiveSnGift(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      final Player player = this.getPlayer();
      final String sn = this.readUTF();
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            GiftSnManager.receiveSnGift(player, sn);
         }
      });
   }
}
