package com.mu.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import jxl.Sheet;
import jxl.Workbook;

public class RndNames {
   private static final int NameNumber = 35000;
   private static ConcurrentHashMap lastSelectedNames = new ConcurrentHashMap(8, 0.75F, 2);
   private static ConcurrentLinkedQueue maleQueue = new ConcurrentLinkedQueue();
   private static ConcurrentLinkedQueue femalQueue = new ConcurrentLinkedQueue();
   private static ArrayList surnameList = new ArrayList();
   private static ArrayList boyNameList = new ArrayList();
   private static ArrayList girlNameList = new ArrayList();
   private static HashSet usedNameSet = new HashSet();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheetSurName = wb.getSheet(1);
      Sheet sheetBoyName = wb.getSheet(2);
      Sheet sheetGirlName = wb.getSheet(3);
      int surRows = sheetSurName.getRows();

      int boyRows;
      for(boyRows = 1; boyRows < surRows; ++boyRows) {
         addSurname(sheetSurName.getCell(0, boyRows).getContents().trim());
      }

      boyRows = sheetBoyName.getRows();

      int girlRows;
      for(girlRows = 1; girlRows < boyRows; ++girlRows) {
         addBoyName(sheetBoyName.getCell(0, girlRows).getContents().trim());
      }

      girlRows = sheetGirlName.getRows();

      for(int i = 1; i < girlRows; ++i) {
         addGirlName(sheetGirlName.getCell(0, i).getContents().trim());
      }

   }

   public static void addSurname(String sn) {
      surnameList.add(sn);
   }

   public static void addBoyName(String bn) {
      boyNameList.add(bn);
   }

   public static void addGirlName(String gn) {
      girlNameList.add(gn);
   }

   public static void addUsedName(String name) {
      usedNameSet.add(name);
   }

   public static String getRndName(int sex) {
      String f = (String)surnameList.get(Rnd.get(surnameList.size()));
      return sex == 0 ? f + (String)boyNameList.get(Rnd.get(boyNameList.size())) : f + (String)girlNameList.get(Rnd.get(girlNameList.size()));
   }

   public static void createNames() {
      int surnameSize = surnameList.size();
      int boyNameSize = boyNameList.size();
      int girlNameSize = girlNameList.size();

      int count;
      int tmpCount;
      String name;
      for(count = 35000; count > 0; --count) {
         tmpCount = 100;

         while(tmpCount > 0) {
            name = (String)surnameList.get(Rnd.get(surnameSize)) + (String)boyNameList.get(Rnd.get(boyNameSize));
            if (DFA.hasKeyWords(name)) {
               --tmpCount;
            } else {
               if (!usedNameSet.contains(name)) {
                  usedNameSet.add(name);
                  maleQueue.add(name);
                  break;
               }

               --tmpCount;
            }
         }
      }

      for(count = 35000; count > 0; --count) {
         tmpCount = 100;

         while(tmpCount > 0) {
            name = (String)surnameList.get(Rnd.get(surnameSize)) + (String)girlNameList.get(Rnd.get(girlNameSize));
            if (DFA.hasKeyWords(name)) {
               --tmpCount;
            } else {
               if (!usedNameSet.contains(name)) {
                  usedNameSet.add(name);
                  femalQueue.add(name);
                  break;
               }

               --tmpCount;
            }
         }
      }

      usedNameSet.clear();
   }

   public static String getName(String userName, int sex) {
      String name = "";
      String oldName = (String)lastSelectedNames.remove(userName);
      if (sex == 0) {
         if (!maleQueue.isEmpty()) {
            name = (String)maleQueue.poll();
            lastSelectedNames.put(userName, name);
         }
      } else if (!femalQueue.isEmpty()) {
         name = (String)femalQueue.poll();
         lastSelectedNames.put(userName, name);
      }

      if (oldName != null) {
         returnName(oldName, sex);
      }

      return name;
   }

   private static void returnName(String name, int sex) {
      if (sex == 0) {
         maleQueue.add(name);
      } else {
         femalQueue.add(name);
      }

   }

   public static void returnNameForLogout(String userName, int sex) {
      String name = (String)lastSelectedNames.remove(userName);
      if (name != null) {
         returnName(name, sex);
      }

   }

   public static void deleteName(String userName) {
      lastSelectedNames.remove(userName);
   }
}
