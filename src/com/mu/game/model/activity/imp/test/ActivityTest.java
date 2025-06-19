package com.mu.game.model.activity.imp.test;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class ActivityTest extends Activity {
   private int buffId;
   private long buffDuration;
   private int startHour;
   private int startMinute;

   public ActivityTest() {
      super(5);
   }

   public void init(Object obj) throws Exception {
      Calendar oc = Calendar.getInstance();
      oc.setTime(Global.getOpenServerTiem());
      this.setOpenDate(oc.getTime());
      Calendar cc = Calendar.getInstance();
      cc.setTime(Global.getOpenServerTiem());
      cc.add(11, 200000);
      this.setCloseDate(cc.getTime());
   }

   public boolean isOpen() {
      return false;
   }

   public int getStartHour() {
      return this.startHour;
   }

   public void setStartHour(int startHour) {
      this.startHour = startHour;
   }

   public int getStartMinute() {
      return this.startMinute;
   }

   public void setStartMinute(int startMinute) {
      this.startMinute = startMinute;
   }

   public int getBuffId() {
      return this.buffId;
   }

   public void setBuffId(int buffId) {
      this.buffId = buffId;
   }

   public long getBuffDuration() {
      return this.buffDuration;
   }

   public void setBuffDuration(long buffDuration) {
      this.buffDuration = buffDuration;
   }

   public int getActivityType() {
      return 3;
   }

   public void writeDetail(Player player) {
      ActivityTestElement ae = (ActivityTestElement)this.getElementList().get(0);
      ActivityInfo ai = new ActivityInfo();

      try {
         ai.writeByte(this.getId());
         ai.writeShort(ae.getId());
         ArrayList list = ae.getRewardList();
         ai.writeByte(list.size());
         Iterator var6 = list.iterator();

         while(var6.hasNext()) {
            Item item = (Item)var6.next();
            GetItemStats.writeItem(item, ai);
         }

         ai.writeByte(ae.getReceiveStatus(player));
         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public int getShellId() {
      return 3;
   }
}
