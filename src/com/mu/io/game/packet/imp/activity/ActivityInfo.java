package com.mu.io.game.packet.imp.activity;

import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityManager;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ActivityInfo extends ReadAndWritePacket {
   public ActivityInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public ActivityInfo() {
      super(10802, (byte[])null);
   }

   public void process() throws Exception {
      int id = this.readByte();
      Activity activity = ActivityManager.getActivity(id);
      if (activity != null) {
         activity.writeDetail(this.getPlayer());
      }

   }
}
