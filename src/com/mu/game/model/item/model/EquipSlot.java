package com.mu.game.model.item.model;

import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;

public class EquipSlot {
   private static HashMap slots = new HashMap();
   public static final int WEAPON = 0;
   public static final int WEAPON_SECONDARY = 1;
   public static final int CLOTHES = 2;
   public static final int LEGGINGS = 3;
   public static final int HEAD = 4;
   public static final int Panda = 5;
   public static final int NECK = 6;
   public static final int WRIST = 7;
   public static final int RING_1 = 8;
   public static final int RING_2 = 9;
   public static final int FOOT = 10;
   public static final int Guardian = 11;
   public static final int WING = 12;
   public static final int RINDING = 13;
   public static final int JEWELRY = 14;
   public static final int NONE = 100;
   private static List equipCheckSet = new ArrayList();
   private int slotID;
   private String slotName;
   private int defaultIcon;

   public EquipSlot(int slotID, String slotName, int defaultIcon) {
      this.slotID = slotID;
      this.slotName = slotName;
      this.defaultIcon = defaultIcon;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int slotID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int icon = Tools.getCellIntValue(sheet.getCell("C" + i));
         EquipSlot slot = new EquipSlot(slotID, name, icon);
         slots.put(slotID, slot);
      }

      equipCheckSet.add(Integer.valueOf(0));
      equipCheckSet.add(Integer.valueOf(2));
      equipCheckSet.add(Integer.valueOf(4));
      equipCheckSet.add(Integer.valueOf(3));
      equipCheckSet.add(Integer.valueOf(7));
      equipCheckSet.add(Integer.valueOf(10));
   }

   public static List getEquipCheckSet() {
      return equipCheckSet;
   }

   public static EquipSlot getSlot(int slotID) {
      return (EquipSlot)slots.get(slotID);
   }

   public static HashMap getAllSlots() {
      return slots;
   }

   public int getSlotID() {
      return this.slotID;
   }

   public void setSlotID(int slotID) {
      this.slotID = slotID;
   }

   public String getSlotName() {
      return this.slotName;
   }

   public void setSlotName(String slotName) {
      this.slotName = slotName;
   }

   public int getDefaultIcon() {
      return this.defaultIcon;
   }

   public void setDefaultIcon(int defaultIcon) {
      this.defaultIcon = defaultIcon;
   }
}
