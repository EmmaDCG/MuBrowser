package com.mu.game.model.unit.player;

import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class CreateProfessionInfo {
   private int ProfessionType = 0;
   private String name;
   private boolean isOpne = true;
   private int nameSrc1 = -1;
   private int nameSrc2 = -1;
   private int header = -1;
   private int picture = -1;
   private String des;
   private ArrayList tjbList = new ArrayList();
   private static ArrayList infoList = new ArrayList();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      initBase(wb.getSheet(1));
      initChangeJob(wb.getSheet(2));
   }

   private static void initBase(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         CreateProfessionInfo info = new CreateProfessionInfo();
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String n = Tools.getCellValue(sheet.getCell("B" + i));
         boolean open = Tools.getCellIntValue(sheet.getCell("C" + i)) == 1;
         int w1 = Tools.getCellIntValue(sheet.getCell("D" + i));
         int w2 = Tools.getCellIntValue(sheet.getCell("E" + i));
         int h = Tools.getCellIntValue(sheet.getCell("F" + i));
         int img = Tools.getCellIntValue(sheet.getCell("G" + i));
         String d = Tools.getCellValue(sheet.getCell("H" + i));
         info.setHeader(h);
         info.setName(n);
         info.setNameSrc1(w1);
         info.setNameSrc2(w2);
         info.setOpne(open);
         info.setPicture(img);
         info.setProfessionType(id);
         info.setDes(d);
         infoList.add(info);
      }

   }

   private static void initChangeJob(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int type = Tools.getCellIntValue(sheet.getCell("A" + i));
         String n = Tools.getCellValue(sheet.getCell("B" + i));
         int icon = Tools.getCellIntValue(sheet.getCell("C" + i));
         String d = Tools.getCellValue(sheet.getCell("D" + i));
         TransferJobInfo ti = new TransferJobInfo();
         ti.setDes(d);
         ti.setIcon(icon);
         ti.setName(n);
         CreateProfessionInfo info = getInfo(type);
         if (info != null) {
            info.addTransferJob(ti);
         }
      }

   }

   private static CreateProfessionInfo getInfo(int type) {
      Iterator var2 = infoList.iterator();

      while(var2.hasNext()) {
         CreateProfessionInfo info = (CreateProfessionInfo)var2.next();
         if (info.getProfessionType() == type) {
            return info;
         }
      }

      return null;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public int getProfessionType() {
      return this.ProfessionType;
   }

   public void setProfessionType(int professionType) {
      this.ProfessionType = professionType;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isOpne() {
      return this.isOpne;
   }

   public void setOpne(boolean isOpne) {
      this.isOpne = isOpne;
   }

   public int getNameSrc1() {
      return this.nameSrc1;
   }

   public void setNameSrc1(int nameSrc1) {
      this.nameSrc1 = nameSrc1;
   }

   public int getNameSrc2() {
      return this.nameSrc2;
   }

   public void setNameSrc2(int nameSrc2) {
      this.nameSrc2 = nameSrc2;
   }

   public int getHeader() {
      return this.header;
   }

   public void setHeader(int header) {
      this.header = header;
   }

   public int getPicture() {
      return this.picture;
   }

   public void setPicture(int picture) {
      this.picture = picture;
   }

   public ArrayList getTjbList() {
      return this.tjbList;
   }

   public void addTransferJob(TransferJobInfo ti) {
      this.tjbList.add(ti);
   }

   public static ArrayList getInfoList() {
      return infoList;
   }
}
