package com.mu.io.game.packet.imp.storage;

import com.mu.game.model.item.container.BackpackExpandData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class RequestBackpackExpand extends ReadAndWritePacket {
   public RequestBackpackExpand(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int gridIndex = this.readByte();
      gridIndex = gridIndex + 1;
      int result = BackpackExpandData.canOpen(player, gridIndex);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      } else {
         long[] rewardAndNeed = BackpackExpandData.getRewardAndNeed(player, gridIndex);
         int type = (int)rewardAndNeed[3];
         this.writeByte(type);
         this.writeDouble((double)rewardAndNeed[0]);
         this.writeInt((int)rewardAndNeed[2]);
         player.writePacket(this);
      }
   }
}
