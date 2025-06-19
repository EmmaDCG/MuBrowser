package com.mu.game.model.unit.monster.goldenboss;

import com.mu.config.Global;
import com.mu.game.model.drop.model.MonsterDrop;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.game.task.specified.SpecifiedTimeManager;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import jxl.Sheet;
import jxl.Workbook;

public class GoldenBossManager {
   public static final int GoldenBoss_Small = 1;
   public static final int GoldenBoss_Big = 2;
   private static ArrayList startTimeList = new ArrayList();
   private static ArrayList taskList = new ArrayList();
   private static HashMap mapInfoMap = new HashMap();
   private static int duration = 1800;
   private static int timeCost = 0;
   private static boolean isBegin = false;
   private static String startBroadCastContent;
   private static ScheduledFuture future = null;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      initGenoral(wb.getSheet(1));
      initMapInfo(wb.getSheet(2));
      initBossData(wb.getSheet(3));
   }

   private static void initBossData(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int mapId = Tools.getCellIntValue(sheet.getCell("A" + i));
         GoldenBossMapInfo mi = getGoldenBossMapInfo(mapId);
         if (mi != null) {
            GoldenBossData data = new GoldenBossData();
            data.setMapId(mapId);
            data.setTemplateId(Tools.getCellIntValue(sheet.getCell("B" + i)));
            data.setAi(Tools.getCellIntValue(sheet.getCell("C" + i)));
            data.setMinLevel(Tools.getCellIntValue(sheet.getCell("D" + i)));
            data.setMaxLevel(data.getMinLevel());
            data.setStar(Tools.getCellIntValue(sheet.getCell("E" + i)));
            data.setNum(Tools.getCellIntValue(sheet.getCell("F" + i)));
            String pointStr = Tools.getCellValue(sheet.getCell("G" + i));
            String[] tmpPoints = pointStr.split(";");
            if (tmpPoints.length < data.getNum()) {
               throw new Exception("坐标点数量小于怪物数量");
            }

            for(int j = 0; j < tmpPoints.length; ++j) {
               String[] ps = tmpPoints[j].split(",");
               data.addPoint(new int[]{Integer.parseInt(ps[0]), Integer.parseInt(ps[1])});
            }

            data.setSpeed(Tools.getCellFloatValue(sheet.getCell("H" + i)) * 100.0F);
            data.setMoveRadius(Tools.getCellIntValue(sheet.getCell("I" + i)));
            data.setGiveUpDist(Tools.getCellIntValue(sheet.getCell("J" + i)));
            data.setSerachDist(Tools.getCellIntValue(sheet.getCell("K" + i)));
            data.setMaxMoveDist(Tools.getCellIntValue(sheet.getCell("L" + i)));
            data.setAttackDist(Tools.getCellIntValue(sheet.getCell("M" + i)));
            data.setMinAttackDist(Tools.getCellIntValue(sheet.getCell("N" + i)));
            String skillIDStr = Tools.getCellValue(sheet.getCell("O" + i));
            List skillList = StringTools.analyzeIntegerList(skillIDStr, ",");
            if (skillList == null || skillList.size() < 1) {
               (new Exception("怪物技能没有配置")).printStackTrace();
            }

            Iterator var11 = skillList.iterator();

            while(var11.hasNext()) {
               Integer skillID = (Integer)var11.next();
               if (!SkillModel.hasModel(skillID.intValue())) {
                  (new Exception("怪物 分布 - 技能id不存在")).printStackTrace();
               }
            }

            data.setSkillList(skillList);
            data.setGoldenBossType(Tools.getCellIntValue(sheet.getCell("P" + i)));
            if (data.getGoldenBossType() == 1) {
               mi.setSmallBossData(data);
            } else {
               mi.setBigBossData(data);
            }

            int loopSize = Tools.getCellIntValue(sheet.getCell("Q" + i));
            String dropStr = Tools.getCellValue(sheet.getCell("R" + i));
            MonsterDrop md = new MonsterDrop(loopSize, dropStr, "黄金部队boss");
            data.addDrops(md);
            int bigLastLoopSize = Tools.getCellIntValue(sheet.getCell("S" + i));
            String bigLastDropStr = Tools.getCellValue(sheet.getCell("T" + i));
            MonsterDrop lastAttackMd = new MonsterDrop(bigLastLoopSize, bigLastDropStr, "黄金部队最后一击");
            data.addDrops(lastAttackMd);
         }
      }

   }

   public static GoldenBossMapInfo getGoldenBossMapInfo(int mapId) {
      return (GoldenBossMapInfo)mapInfoMap.get(mapId);
   }

   private static void initMapInfo(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int mapId = Tools.getCellIntValue(sheet.getCell("A" + i));
         String des = Tools.getCellValue(sheet.getCell("B" + i));
         String replaceDes = Tools.getCellValue(sheet.getCell("C" + i));
         int bossIcon = Tools.getCellIntValue(sheet.getCell("D" + i));
         ArrayList dropList = Tools.parseItemList(Tools.getCellValue(sheet.getCell("E" + i)));
         MapData data = MapConfig.getMapData(mapId);
         int reqLevel = data.getReqLevel();
         GoldenBossMapInfo mi = new GoldenBossMapInfo(mapId);
         mi.setBossIcon(bossIcon);
         mi.setDes(des);
         mi.setDropList(dropList);
         mi.setReplaceStr(replaceDes);
         mi.setReqLevel(reqLevel);
         mapInfoMap.put(mapId, mi);
      }

   }

   private static void initGenoral(Sheet sheet) throws Exception {
      duration = Tools.getCellIntValue(sheet.getCell("B2"));
      startBroadCastContent = Tools.getCellValue(sheet.getCell("C2"));
      String timeStr = Tools.getCellValue(sheet.getCell("A2"));
      String[] tmp = timeStr.split(";");

      for(int i = 0; i < tmp.length; ++i) {
         Date date = Time.getDate(tmp[i].trim(), "HH:mm:ss");
         int[] time = new int[3];
         Calendar cd = Calendar.getInstance();
         cd.setTime(date);
         time[0] = cd.get(11);
         time[1] = cd.get(12);
         time[2] = cd.get(13);
         startTimeList.add(time);
      }

   }

   private static void end() {
      endFuture();
      timeCost = 0;
      isBegin = false;
   }

   private static void endFuture() {
      if (future != null) {
         future.cancel(true);
         future = null;
      }

   }

   public static String getStartBroadCastContent() {
      return startBroadCastContent;
   }

   public static void start() {
      if (!Global.isInterServiceServer()) {
         Iterator var1 = taskList.iterator();

         while(var1.hasNext()) {
            GoldenBossTask task = (GoldenBossTask)var1.next();
            task.cancel();
         }

         taskList.clear();
         SpecifiedTimeManager.purge();

         for(int i = 0; i < startTimeList.size(); ++i) {
            int[] startTime = (int[])startTimeList.get(i);
            Date startDate = getDate(startTime[0], startTime[1], startTime[2]);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            GoldenBossTask rt = new GoldenBossTask(startTime[0], startTime[1], startTime[2]);

            try {
               rt.start();
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }

      }
   }

   public static void createBoss() {
      endFuture();
      timeCost = 0;
      isBegin = true;
      future = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(new Runnable() {
         public void run() {
            GoldenBossManager.checkTime();
         }
      }, 1000L, 1000L);
   }

   public static boolean isBegin() {
      return isBegin;
   }

   private static void checkTime() {
      ++timeCost;
      if (timeCost >= duration) {
         end();
      }

   }

   private static Date getDate(int hour, int minute, int second) {
      Calendar tc = Calendar.getInstance();
      tc.set(11, hour);
      tc.set(12, minute);
      tc.set(13, second);
      return tc.getTime();
   }
}
