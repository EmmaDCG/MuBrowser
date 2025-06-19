package com.mu.game.dungeon.imp.redfort;

import com.mu.game.CenterManager;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.specified.SpecifiedTimeManager;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.dungeon.DunTimingPanel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class RedFortManager {
   private RedFort redFort = null;
   private ArrayList startTimeList = new ArrayList();
   private ArrayList taskList = new ArrayList();

   public RedFort getRedFort() {
      return this.redFort;
   }

   public void createRedFort() {
      if (this.redFort != null) {
         try {
            this.redFort.destroy();
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      RedFortTemplate template = (RedFortTemplate)DungeonTemplateFactory.getTemplate(5);
      this.redFort = new RedFort(DungeonManager.getID(), template);
      this.redFort.init();
      DungeonManager.addDungeon(this.redFort);
      DunTimingPanel ap = template.getTimingPanel();
      Iterator it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         if (player.getLevel() >= template.getLevelLimit()) {
            UpdateMenu.updateDungeonMenu(player, 5);
            player.writePacket(ap);
         }
      }

      ap.destroy();
      ap = null;
   }

   public void clearRedFort() {
      this.redFort = null;
   }

   public void addStartTime(int[] time) {
      this.startTimeList.add(time);
   }

   public Date getDate(int hour, int minute, int second) {
      Calendar tc = Calendar.getInstance();
      tc.set(11, hour);
      tc.set(12, minute);
      tc.set(13, second);
      return tc.getTime();
   }

   public void start() {
      if (this.redFort != null) {
         this.redFort.destroy();
         this.redFort = null;
      }

      Iterator var2 = this.taskList.iterator();

      while(var2.hasNext()) {
         RedFortTask task = (RedFortTask)var2.next();
         task.cancel();
      }

      this.taskList.clear();
      SpecifiedTimeManager.purge();

      for(int i = 0; i < this.startTimeList.size(); ++i) {
         int[] startTime = (int[])this.startTimeList.get(i);
         Date startDate = this.getDate(startTime[0], startTime[1], startTime[2]);
         Calendar startCalendar = Calendar.getInstance();
         startCalendar.setTime(startDate);
         RedFortTask rt = new RedFortTask(startTime[0], startTime[1], startTime[2]);

         try {
            rt.start();
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

   }
}
