package com.mu.io.game.packet.imp.rewardhall.online;

import com.mu.io.game.packet.ReadAndWritePacket;

public class OnlineRewardReceiveDay extends ReadAndWritePacket {
   public OnlineRewardReceiveDay(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.getPlayer().getOnlineManager().receiveDayReward();
   }
}
