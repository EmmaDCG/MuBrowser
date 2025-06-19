package com.mu.io.game.packet.imp.activity;

import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.imp.tx.bluevip.ActivityBlueVip;
import com.mu.io.game.packet.ReadAndWritePacket;

public class BlueOneKeyReceive extends ReadAndWritePacket {
   public BlueOneKeyReceive(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public BlueOneKeyReceive() {
      super(10805, (byte[])null);
   }

   public void process() throws Exception {
      Activity activity = ActivityManager.getActivity(10);
      if (activity != null) {
         ActivityBlueVip bv = (ActivityBlueVip)activity;
         bv.oneKeyReceive(this.getPlayer());
      }

   }
}
