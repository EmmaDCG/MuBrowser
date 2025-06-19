package com.mu.io.game.packet.imp.rewardhall.sign;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class SignReward extends ReadAndWritePacket {
   public SignReward(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int id = this.readByte();
      Player player = this.getPlayer();
      boolean success = player.getSignManager().drawSignReward(id);
      this.writeBoolean(success);
      player.writePacket(this);
   }
}
