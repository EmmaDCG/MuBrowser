package com.mu.io.game.packet.imp.rewardhall.vitality;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class VitalityReward extends ReadAndWritePacket {
   public VitalityReward(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int id = this.readByte();
      boolean success = player.getVitalityManager().drawReward(id);
      this.writeBoolean(success);
      player.writePacket(this);
   }
}
