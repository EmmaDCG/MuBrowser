package com.mu.game.model.tanxian;

import com.mu.game.model.map.LineMap;
import com.mu.game.model.map.MapConfig;
import com.mu.utils.Tools;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class TanXianConfigManager {
   private static final Map dataMap = new LinkedHashMap();
   private static final List levelList = new ArrayList();
   private static HashMap clueInfoMap = new HashMap();
   private static HashMap mapClue = new HashMap();
   public static int TANXIAN_EXPEND_ITEM = 0;
   public static int TANXIAN_EXPEND_INGOT = 0;
   public static String TANXIAN_COUNT_TIP = "";
   public static int TANXIAN_BUY_COUNT = 0;

   public static void initConfigs(InputStream in) throws BiffException, IOException {
      Workbook wb = null;

      try {
         wb = Workbook.getWorkbook(in);
         Sheet[] sheets = wb.getSheets();
         initTanXianDataConfig(sheets[1]);
         initTanXianLevelConfig(sheets[2]);
         initStaticDataConfig(sheets[3]);
         initClueInfo(sheets[4]);
      } finally {
         if (wb != null) {
            wb.close();
         }

      }

   }

   private static void initStaticDataConfig(Sheet sheet) {
      TANXIAN_EXPEND_ITEM = Integer.parseInt(sheet.getCell("B1").getContents().trim());
      TANXIAN_EXPEND_INGOT = Integer.parseInt(sheet.getCell("B2").getContents().trim());
      TANXIAN_COUNT_TIP = sheet.getCell("B3").getContents().trim();
      TANXIAN_BUY_COUNT = Integer.parseInt(sheet.getCell("B4").getContents().trim());
   }

   private static void initClueInfo(Sheet sheet) {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int mapId = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int templateId = Tools.getCellIntValue(sheet.getCell("C" + i));
         int x = Tools.getCellIntValue(sheet.getCell("E" + i));
         int y = Tools.getCellIntValue(sheet.getCell("F" + i));
         int taskId = Tools.getCellIntValue(sheet.getCell("I" + i));
         String reStr = Tools.getCellValue(sheet.getCell("J" + i));
         int tanxianLevel = Tools.getCellIntValue(sheet.getCell("K" + i));
         int dunRate = Tools.getCellIntValue(sheet.getCell("L" + i));
         ClueInfo ci = new ClueInfo();
         ci.setDunRate(dunRate);
         ci.setMapId(mapId);
         ci.setName(name);
         ci.setTanxianLevel(tanxianLevel);
         ci.setTaskId(taskId);
         ci.setTemplateId(templateId);
         ci.setX(x);
         ci.setY(y);
         ci.setUnitList(Tools.parseItemDataUnitList(reStr));

         try {
            ci.setItemList(Tools.parseItemList(reStr));
         } catch (Exception var14) {
            var14.printStackTrace();
         }

         ArrayList cList = (ArrayList)clueInfoMap.get(mapId);
         if (cList == null) {
            cList = new ArrayList();
            clueInfoMap.put(mapId, cList);
         }

         cList.add(ci);
      }

   }

   private static void initTanXianDataConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         String G = sheet.getCell("G" + i).getContents().trim();
         String H = sheet.getCell("H" + i).getContents().trim();
         TanXianData data = new TanXianData(Integer.parseInt(A));
         data.setName(B);
         data.setIcon(Integer.parseInt(C));
         data.setOpenLevel(Integer.parseInt(D));
         data.setDescription(E);
         data.setItemListStr(F);
         data.setExp(Integer.parseInt(G));
         data.setTaskStr(H);
         dataMap.put(data.getId(), data);
      }

   }

   private static void initTanXianLevelConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String B = sheet.getCell("B" + i).getContents().trim();
         levelList.add(Integer.parseInt(B));
      }

   }

   public static void initClues() {
      Iterator it = clueInfoMap.entrySet().iterator();

      while(true) {
         ArrayList list;
         LineMap lm;
         do {
            if (!it.hasNext()) {
               return;
            }

            Entry entry = (Entry)it.next();
            int mapId = ((Integer)entry.getKey()).intValue();
            list = (ArrayList)entry.getValue();
            lm = MapConfig.getLineMap(mapId);
         } while(lm == null);

         com.mu.game.model.map.Map[] maps = lm.getAllMaps();

         for(int i = 0; i < maps.length; ++i) {
            com.mu.game.model.map.Map map = maps[i];
            Iterator var9 = list.iterator();

            while(var9.hasNext()) {
               ClueInfo info = (ClueInfo)var9.next();
               Clue clue = new Clue(info, map);
               map.addMaterial(clue);
               if (!mapClue.containsKey(info.getTaskId())) {
                  mapClue.put(info.getTaskId(), info);
               }
            }
         }
      }
   }

   public static ClueInfo getClueInfoByTask(int taskId) {
      return (ClueInfo)mapClue.get(taskId);
   }

   public static int getDataSize() {
      return dataMap.size();
   }

   public static Iterator getTanXianDataIterator() {
      return dataMap.values().iterator();
   }

   public static TanXianData getData(int id) {
      return (TanXianData)dataMap.get(id);
   }

   public static int getMaxLevel() {
      return levelList.size();
   }

   public static int getLevelExp(int level) {
      return level > 0 && level <= getMaxLevel() ? ((Integer)levelList.get(level - 1)).intValue() : Integer.MAX_VALUE;
   }
}
