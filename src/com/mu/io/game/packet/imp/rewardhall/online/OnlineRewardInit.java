package com.mu.io.game.packet.imp.rewardhall.online;

import com.mu.io.game.packet.ReadAndWritePacket;

public class OnlineRewardInit extends ReadAndWritePacket {
   public OnlineRewardInit(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      this.getPlayer().getOnlineManager().init(this);
   }
}
