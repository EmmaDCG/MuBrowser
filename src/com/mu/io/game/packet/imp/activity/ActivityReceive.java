package com.mu.io.game.packet.imp.activity;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.activity.ActivityManager;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ActivityReceive extends ReadAndWritePacket {
   public ActivityReceive(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public ActivityReceive() {
      super(10803, (byte[])null);
   }

   public void process() throws Exception {
      int id = this.readUnsignedShort();
      ActivityElement element = ActivityManager.getActivityElement(id);
      if (element != null) {
         element.doReceive(this.getPlayer());
      }
   }
}
