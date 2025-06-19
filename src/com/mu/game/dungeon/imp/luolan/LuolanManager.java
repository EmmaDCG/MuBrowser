package com.mu.game.dungeon.imp.luolan;

import com.mu.config.BroadcastManager;
import com.mu.config.Global;
import com.mu.db.manager.GangDBManager;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.specified.SpecifiedTimeManager;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.dungeon.DunTimingPanel;
import com.mu.io.game.packet.imp.player.ChangePlayerGangName;
import com.mu.utils.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimerTask;

public class LuolanManager {
   private Luolan luolan = null;
   private LuolanTemplate template = null;
   private Date openDate = null;
   private Date comfirmDate = null;
   private OpenTask openTask = null;
   private TimerTask comfirmRankTask = null;

   public LuolanManager(LuolanTemplate template) {
      this.template = template;
   }

   public void init() {
      if (this.openTask != null) {
         this.openTask.cancel();
         this.openTask = null;
      }

      if (this.comfirmRankTask != null) {
         this.comfirmRankTask.cancel();
         this.comfirmRankTask = null;
      }

      SpecifiedTimeManager.purge();
      this.initOpenDate();
      this.openTask = new OpenTask();
      this.comfirmRankTask = new TimerTask() {
         public void run() {
            GangManager.confirmRank();
         }
      };
      SpecifiedTimeManager.schedule(this.openTask, this.openDate);
      SpecifiedTimeManager.schedule(this.comfirmRankTask, this.comfirmDate);
   }

   public Date getOpenDate() {
      return this.openDate;
   }

   public long getOpenDay() {
      return this.openDate == null ? -1L : Time.getDayLong(this.openDate);
   }

   public void initOpenDate() {
      Date openServerDate = Global.getOpenServerTiem();
      Calendar cal = Calendar.getInstance();
      cal.setTime(openServerDate);
      cal.set(11, 20);
      cal.set(12, 0);
      cal.set(13, 0);
      cal.add(6, 2);
      this.openDate = cal.getTime();
      Date nowDate = Calendar.getInstance().getTime();
      if (nowDate.after(this.openDate)) {
         cal.set(7, 6);
         this.openDate = cal.getTime();
         if (nowDate.after(this.openDate)) {
            do {
               cal.add(6, 7);
               this.openDate = cal.getTime();
            } while(!nowDate.before(this.openDate));
         }
      }

      Calendar comfirmCal = Calendar.getInstance();
      comfirmCal.setTime(this.openDate);
      comfirmCal.set(11, 0);
      comfirmCal.set(12, 0);
      comfirmCal.set(13, 1);
      this.comfirmDate = comfirmCal.getTime();
   }

   public void createLuolan(boolean isDebug) {
      if (this.luolan != null) {
         this.luolan.destroy();
      }

      if (isDebug) {
         this.openDate = Calendar.getInstance().getTime();
      }

      this.clearVictoryGang();
      this.luolan = new Luolan(DungeonManager.getID(), this.template);
      this.luolan.initMap();
      DungeonManager.addDungeon(this.luolan);
      UpdateMenu.allPlayerUpdate(22);
      BroadcastManager.broadcastSystemAndZmd(this.template.getBroadcastWhenOpen());
      DunTimingPanel ap = this.template.getTimingPanel();
      HashMap gangs = this.luolan.getJoinGang();
      Iterator it = gangs.keySet().iterator();

      while(true) {
         Gang gang;
         do {
            if (!it.hasNext()) {
               ap.destroy();
               ap = null;
               return;
            }

            gang = GangManager.getGang(((Long)it.next()).longValue());
         } while(gang == null);

         Iterator itMembers = gang.getMemberMap().values().iterator();

         while(itMembers.hasNext()) {
            GangMember member = (GangMember)itMembers.next();
            Player player = member.getPlayer();
            if (player != null) {
               player.writePacket(ap);
            }
         }
      }
   }

   public void clearVictoryGang() {
      long victorGang = GangManager.getWarVictorGang();
      if (victorGang > 0L) {
         Gang gang = GangManager.getGang(victorGang);
         if (gang == null) {
            return;
         }

         GangManager.clearVictoryGang();
         Iterator it = gang.getMemberMap().values().iterator();

         while(it.hasNext()) {
            GangMember member = (GangMember)it.next();
            gang.clearWarTitleAndBuff(member);
            Player player = member.getPlayer();
            if (player != null) {
               ChangePlayerGangName.change(player);
            }
         }

         GangDBManager.clearWarPost(victorGang);
      }

   }

   public LuolanTemplate getTemplate() {
      return this.template;
   }

   public Luolan getLuoLan() {
      return this.luolan;
   }

   public void clearLuolan() {
      this.luolan = null;
   }

   public void resetLuolan() {
      this.openDate = Calendar.getInstance().getTime();
      GangManager.confirmRank();
      this.createLuolan(true);
   }

   public boolean isOnTheMarch() {
      if (this.luolan == null) {
         return false;
      } else {
         return !this.luolan.isComplete();
      }
   }
}
