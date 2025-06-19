package com.mu.game.model.unit.player.pkMode;

import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.List;
import jxl.Sheet;

public enum PkEnum {
   Mode_Peace(1, "和平", true),
   Mode_Force(2, "强制", true),
   Mode_Massacre(3, "全体", true);

   private static List list = new ArrayList();
   private int modeID;
   private String name;
   private boolean manulSwitch;
   private int icon;
   private String des;

   private PkEnum(int modelID, String name, boolean manulSwiitch) {
      this.manulSwitch = manulSwiitch;
      this.modeID = modelID;
      this.name = name;
   }

   public static PkEnum find(int modeID) {
      PkEnum[] var4;
      int var3 = (var4 = values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         PkEnum pe = var4[var2];
         if (pe.getModeID() == modeID) {
            return pe;
         }
      }

      return Mode_Peace;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      int modeID;
      for(int i = 2; i <= rows; ++i) {
         modeID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int icon = Tools.getCellIntValue(sheet.getCell("C" + i));
         String des = Tools.getCellValue(sheet.getCell("D" + i));
         PkEnum pe = find(modeID);
         pe.setName(name);
         pe.setIcon(icon);
         pe.setDes(des);
      }

      PkEnum[] var10;
      int var9 = (var10 = values()).length;

      for(modeID = 0; modeID < var9; ++modeID) {
         PkEnum pk = var10[modeID];
         list.add(pk);
      }

   }

   public static List getAllPkEnums() {
      return list;
   }

   public int getModeID() {
      return this.modeID;
   }

   public void setModeID(int modeID) {
      this.modeID = modeID;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isManulSwitch() {
      return this.manulSwitch;
   }

   public void setManulSwitch(boolean manulSwitch) {
      this.manulSwitch = manulSwitch;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }
}
