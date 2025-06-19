package com.mu.game.model.activity.imp.collection;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.unit.player.Player;
import java.util.Calendar;

public class CollectionActivity extends Activity {
   public CollectionActivity() {
      super(127);
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

   public void open() {
   }

   public CollectionElement getCollectionElement() {
      return (CollectionElement)this.elementList.get(0);
   }

   public void close() {
   }

   public int getActivityType() {
      return 0;
   }

   public void writeDetail(Player player) {
   }

   public int getShellId() {
      return 127;
   }
}
