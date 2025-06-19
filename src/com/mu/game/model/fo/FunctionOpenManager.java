package com.mu.game.model.fo;

import com.mu.game.model.unit.player.Player;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class FunctionOpenManager {
   public static final int Open_Level = 1;
   public static final int Open_Task = 2;
   public static final int Open_Vip = 3;
   public static final int Function_Compose = 1;
   public static final int Function_BloodCastle = 2;
   public static final int Function_Devil = 3;
   public static final int Function_DailyTask = 4;
   public static final int Function_OfferRewardTask = 6;
   public static final int Function_BrilliantTask = 7;
   public static final int Function_Boss = 8;
   public static final int Function_Temple = 9;
   public static final int Function_Trial = 10;
   public static final int Function_JobTransfer = 11;
   public static final int Function_Additional = 12;
   public static final int Function_Gem = 13;
   public static final int Function_Gang = 14;
   public static final int Function_Ranking = 15;
   public static final int Function_Pet = 16;
   public static final int Function_Rune = 17;
   public static final int Function_RuneTransfer = 18;
   public static final int Function_Shield = 19;
   public static final int Function_BigDevil = 20;
   public static final int Function_Offline = 21;
   public static final int Function_SevenDay = 22;
   public static final int Function_SpiritOfWar = 23;
   public static final int Function_Hallow = 24;
   public static final int Function_Top = 25;
   public static HashMap functionOpenMap = new HashMap();
   public static HashMap levelOpenMap = new HashMap();
   public static HashMap taskOpenMap = new HashMap();
   public static HashMap vipOpenMap = new HashMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int icon = Tools.getCellIntValue(sheet.getCell("C" + i));
         int openType = Tools.getCellIntValue(sheet.getCell("D" + i));
         int openValue = Tools.getCellIntValue(sheet.getCell("E" + i));
         String notOpenDes = Tools.getCellValue(sheet.getCell("F" + i));
         FunctionOpen fo = new FunctionOpen(id);
         fo.setIcon(icon);
         fo.setName(name);
         fo.setNotOpenDes(notOpenDes);
         fo.setType(openType);
         fo.setValue(openValue);
         functionOpenMap.put(id, fo);
         switch(openType) {
         case 1:
            levelOpenMap.put(openValue, fo);
            break;
         case 2:
            taskOpenMap.put(openValue, fo);
            break;
         case 3:
            vipOpenMap.put(openValue, fo);
         }
      }

   }

   public static ArrayList getPlayerFunctionOpen(Player player) {
      ArrayList list = new ArrayList();

      FunctionOpenStruct fs;
      for(Iterator it = functionOpenMap.values().iterator(); it.hasNext(); list.add(fs)) {
         FunctionOpen fo = (FunctionOpen)it.next();
         fs = new FunctionOpenStruct(fo.getId());
         boolean isOpen = fo.isOpen(player);
         fs.setIcon(fo.getIcon());
         fs.setOpen(isOpen);
         if (isOpen) {
            fs.setDes(fo.getName());
         } else {
            fs.setDes(fo.getNotOpenDes());
         }
      }

      return list;
   }

   public static boolean isOpen(Player player, int id) {
      FunctionOpen fo = (FunctionOpen)functionOpenMap.get(id);
      return fo != null && fo.isOpen(player);
   }

   public static FunctionOpen getFunctionOpen(int id) {
      return (FunctionOpen)functionOpenMap.get(id);
   }

   public static void checkLevel(Player player, int oldLevel, int newLevel) {
      for(int i = oldLevel + 1; i <= newLevel; ++i) {
         FunctionOpen fo = (FunctionOpen)levelOpenMap.get(i);
         if (fo != null) {
            fo.doOpen(player);
         }
      }

   }

   public static int getPetOpenLevel() {
      return ((FunctionOpen)functionOpenMap.get(Integer.valueOf(16))).getValue();
   }

   public static void checkTask(Player player, int taskId) {
      FunctionOpen fo = (FunctionOpen)taskOpenMap.get(taskId);
      if (fo != null) {
         fo.doOpen(player);
      }

   }
}
