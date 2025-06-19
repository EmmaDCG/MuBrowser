package com.mu.io.game.packet.imp.rewardhall.online;

import com.mu.io.game.packet.ReadAndWritePacket;

public class OnlineRewardReceiveWeek extends ReadAndWritePacket {
   public OnlineRewardReceiveWeek(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.getPlayer().getOnlineManager().receiveWeekReward();
   }
}
