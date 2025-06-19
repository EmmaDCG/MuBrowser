package com.mu.io.game.packet.imp.activity;

import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ActivityBaseInfo extends ReadAndWritePacket {
   public ActivityBaseInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public ActivityBaseInfo() {
      super(10801, (byte[])null);
   }

   public static void writeBase(Player player) {
      ActivityBaseInfo ai = new ActivityBaseInfo();

      try {
         ConcurrentHashMap map = ActivityManager.getActivityMap();
         ai.writeByte(map.size());
         Iterator it = map.values().iterator();

         while(it.hasNext()) {
            Activity activity = (Activity)it.next();
            ai.writeByte(activity.getId());
            ai.writeUTF(activity.getName());
            ai.writeByte(activity.getActivityType());
            ai.writeShort(activity.getSort());
            ai.writeBoolean(activity.isOpen());
            ai.writeByte(activity.getDigitalRelationId());
         }

         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void process() throws Exception {
      ConcurrentHashMap map = ActivityManager.getActivityMap();
      this.writeByte(map.size());
      Iterator it = map.values().iterator();

      while(it.hasNext()) {
         Activity activity = (Activity)it.next();
         this.writeByte(activity.getId());
         this.writeUTF(activity.getName());
         this.writeByte(activity.getActivityType());
         this.writeShort(activity.getSort());
         this.writeBoolean(activity.isOpen());
         this.writeByte(activity.getDigitalRelationId());
      }

      this.getPlayer().writePacket(this);
   }
}
