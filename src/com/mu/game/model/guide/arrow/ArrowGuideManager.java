package com.mu.game.model.guide.arrow;

import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.guide.PushRoleArrow;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class ArrowGuideManager {
   public static final int Arrow_Welcom = 1;
   public static final int Arrow_NewEquip = 2;
   public static final int Arrow_NewSkill = 3;
   public static final int Arrow_AddPoint = 4;
   public static final int Arrow_SavePoint = 5;
   public static final int Arrow_Compose = 6;
   public static final int Arrow_AutoDungeon = 7;
   public static final int Arrow_TaskFly = 8;
   public static final int Arrow_TaskNext = 9;
   public static final int Arrow_CloseAddPoint = 10;
   public static final int Arrow_ComposeWineNotice = 11;
   public static final int Arrow_OneKeyPut = 12;
   public static final int Arrow_RingTask = 13;
   public static final int Arrow_EnterBloodCastle = 14;
   public static final int Arrow_BloodCastleReceive = 15;
   public static final int Arrow_EnterDevilSqure = 16;
   public static final int Arrow_DevilSqureReceive = 17;
   public static final int Arrow_EnterTrial = 18;
   public static final int Arrow_AutoTrial = 19;
   public static final int Arrow_EnterPersonalBoss = 20;
   public static final int Arrow_AutouPersonalBoss = 21;
   public static final int Arrow_QuitDungeon = 22;
   public static final int Arrow_Inspire = 23;
   public static final int Arrow_AcceptRingTask = 24;
   public static final int Arrow_CloseBlood = 25;
   private static HashMap infoMap = new HashMap();
   private static HashMap arrowGuideMap = new HashMap();
   private Player owner = null;
   private HashMap guideMap = new HashMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String content = Tools.getCellValue(sheet.getCell("C" + i));
         boolean isInDb = Tools.getCellIntValue(sheet.getCell("D" + i)) == 1;
         int times = Tools.getCellIntValue(sheet.getCell("E" + i));
         boolean saveMore = Tools.getCellIntValue(sheet.getCell("F" + i)) == 1;
         String other = Tools.getCellValue(sheet.getCell("G" + i));
         ArrowInfo info = new ArrowInfo(id);
         info.setContent(content);
         info.setInDb(isInDb);
         info.setTimes(times);
         info.setOther(other);
         if (info.isInDb()) {
            info.setSaveMore(saveMore);
         }

         infoMap.put(id, info);
      }

      createArrowGuide();
   }

   private static void createArrowGuide() {
      Iterator it = infoMap.values().iterator();

      while(it.hasNext()) {
         ArrowInfo info = (ArrowInfo)it.next();
         ArrowGuide ag = null;
         switch(info.getId()) {
         case 19:
         case 21:
            ag = new AutoOrQuitDunArrow(info.getId(), 7);
            break;
         case 20:
         default:
            ag = new CommonArrow(info.getId());
         }

         if (ag != null) {
            arrowGuideMap.put(((ArrowGuide)ag).getArrowId(), ag);
         }
      }

   }

   public static ArrowGuide getArrowGuide(int id) {
      return (ArrowGuide)arrowGuideMap.get(id);
   }

   public static ArrowInfo getArrowInfo(int id) {
      return (ArrowInfo)infoMap.get(id);
   }

   public static void pushArrow(Player player, int id, String content) {
      ArrowGuide ag = getArrowGuide(id);
      if (ag != null) {
         ag.pushArrow(player, content);
      }

   }

   public ArrowGuideManager(Player owner) {
      this.owner = owner;
   }

   public void putGuide(int id, int times) {
      this.guideMap.put(id, times);
   }

   public void addGuide(int id) {
      ArrowInfo info = getArrowInfo(id);
      if (info != null && info.isInDb()) {
         Integer in = (Integer)this.guideMap.get(id);
         int times = 1;
         if (in != null) {
            times = in.intValue() + 1;
         }

         this.guideMap.put(id, times);
         WriteOnlyPacket packet = Executor.SaveArrowGuide.toPacket(this.owner.getID(), id, times);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      }
   }

   public int getGuideTimes(int id) {
      Integer in = (Integer)this.guideMap.get(id);
      return in == null ? 0 : in.intValue();
   }

   public void pushArrow(int id, String text) {
      PushRoleArrow.pushArrow(this.getOwner(), id, text);
   }

   public boolean isFinishComposeWine() {
      return this.getOwner().getAchievementManager().isFinished(2);
   }

   public boolean isFinishMayaWeapon() {
      return this.getOwner().getAchievementManager().isFinished(1);
   }

   public void finishMayaWeapon() {
      this.getOwner().getAchievementManager().addSchedule(1);
   }

   public void finishComposeWine() {
      this.getOwner().getAchievementManager().addSchedule(2);
   }

   public boolean hasComposeGuide() {
      return this.getGuideTimes(6) < 0;
   }

   public Player getOwner() {
      return this.owner;
   }

   public boolean shouldPushRingTask() {
      ArrowInfo info = getArrowInfo(13);
      return info.getTimes() >= this.getGuideTimes(13);
   }

   public boolean isFinished(int arrowId) {
      ArrowInfo info = getArrowInfo(arrowId);
      if (info == null) {
         return true;
      } else {
         return info.getTimes() <= this.getGuideTimes(arrowId);
      }
   }

   public void destroy() {
      this.guideMap.clear();
      this.guideMap = null;
      this.owner = null;
   }
}
