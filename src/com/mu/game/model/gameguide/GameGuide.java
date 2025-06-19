package com.mu.game.model.gameguide;

import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class GameGuide implements Comparable {
   private static ArrayList guideList = new ArrayList();
   private static HashMap guideMap = new HashMap();
   private int id;
   private int sort = 0;
   private String name;
   private ArrayList children = new ArrayList();
   private int background = -1;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      initGuide(wb.getSheet(1));
      initChild(wb.getSheet(2));
   }

   private static void initGuide(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         GameGuide guide = new GameGuide(Tools.getCellIntValue(sheet.getCell("A" + i)));
         guide.setName(Tools.getCellValue(sheet.getCell("B" + i)));
         guide.setBackground(Tools.getCellIntValue(sheet.getCell("C" + i)));
         guide.setSort(guide.getId());
         guideMap.put(guide.getId(), guide);
         guideList.add(guide);
      }

      Collections.sort(guideList);
   }

   public static ArrayList getGuideList() {
      return guideList;
   }

   private static void initChild(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int gid = Tools.getCellIntValue(sheet.getCell("A" + i));
         GameGuide guide = (GameGuide)guideMap.get(gid);
         if (guide != null) {
            guide.addChild(new String[]{Tools.getCellValue(sheet.getCell("B" + i)), Tools.getCellValue(sheet.getCell("C" + i))});
         }
      }

   }

   public GameGuide(int id) {
      this.id = id;
   }

   public int getSort() {
      return this.sort;
   }

   public void setSort(int sort) {
      this.sort = sort;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getId() {
      return this.id;
   }

   public void addChild(String[] child) {
      this.children.add(child);
   }

   public ArrayList getChildren() {
      return this.children;
   }

   public int getBackground() {
      return this.background;
   }

   public void setBackground(int background) {
      this.background = background;
   }

   public int compareTo(GameGuide o) {
      return this.sort - o.sort;
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
