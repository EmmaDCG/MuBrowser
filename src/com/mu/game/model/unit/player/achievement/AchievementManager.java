package com.mu.game.model.unit.player.achievement;

import com.mu.executor.Executor;
import com.mu.executor.imp.player.SaveAchievementExecutor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import jxl.Sheet;
import jxl.Workbook;

public class AchievementManager {
   public static final int Ac_Maya = 1;
   public static final int Ac_Wine1 = 2;
   private static HashMap achievementMap = new HashMap();
   private Player owner;
   private HashMap achMap = new HashMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         int num = Tools.getCellIntValue(sheet.getCell("C" + i));
         boolean sao = Tools.getCellIntValue(sheet.getCell("D" + i)) == 1;
         Achievement ac = new Achievement();
         ac.setId(id);
         ac.setNeedNumber(num);
         ac.setSaveAtOnce(sao);
         achievementMap.put(id, ac);
      }

   }

   public static Achievement getAchievement(int id) {
      return (Achievement)achievementMap.get(id);
   }

   public AchievementManager(Player owner) {
      this.owner = owner;
   }

   public Player getOwner() {
      return this.owner;
   }

   public void addAchievement(int id, int num) {
      this.achMap.put(id, num);
   }

   public void addSchedule(int id) {
      Achievement ac = getAchievement(id);
      if (ac != null) {
         int value = 1;
         Integer in = (Integer)this.achMap.get(id);
         if (in != null) {
            if (ac.isFinished(in.intValue())) {
               return;
            }

            value = in.intValue() + 1;
         }

         this.achMap.put(id, value);
         ac.isFinished(value);
         if (ac.isSaveAtOnce()) {
            SaveAchievementExecutor.saveOneAchievement(this.getOwner(), id, value);
         }

      }
   }

   public boolean isFinished(int id) {
      Integer in = (Integer)this.achMap.get(id);
      if (in == null) {
         return false;
      } else {
         Achievement ac = getAchievement(id);
         return ac == null ? false : ac.isFinished(in.intValue());
      }
   }

   public WriteOnlyPacket createOfflinePacket() {
      ArrayList list = new ArrayList();
      Iterator it = this.achMap.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         int id = ((Integer)entry.getKey()).intValue();
         int value = ((Integer)entry.getValue()).intValue();
         Achievement ac = (Achievement)achievementMap.get(id);
         if (ac != null && !ac.isSaveAtOnce()) {
            list.add(new int[]{id, value});
         }
      }

      if (list.size() == 0) {
         return null;
      } else {
         WriteOnlyPacket packet = Executor.SaveAchievement.toPacket(this.getOwner().getID(), list);
         list.clear();
         return packet;
      }
   }

   public void destroy() {
      if (this.achMap != null) {
         this.achMap.clear();
         this.achMap = null;
      }

      this.owner = null;
   }
}
