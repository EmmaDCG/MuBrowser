package com.mu.game.model.equip.external;

import com.mu.game.model.equip.star.StarForgingData;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.service.StringTools;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import jxl.Sheet;

public class ArmorEffectData {
   private static HashMap effectDatas = new HashMap();
   private static HashMap itemToEffectDatas = new HashMap();
   private static HashMap armorSlotMap = new HashMap();
   private int ID;
   private String name;
   private HashSet modelIDs;
   private HashMap effectAtoms = new HashMap();

   public ArmorEffectData(int ID, String name, HashSet modelIDs) {
      this.ID = ID;
      this.name = name;
      this.modelIDs = modelIDs;
   }

   public static void initData(Sheet sheet) throws Exception {
      armorSlotMap.put(Integer.valueOf(2), Integer.valueOf(1));
      armorSlotMap.put(Integer.valueOf(10), Integer.valueOf(1));
      armorSlotMap.put(Integer.valueOf(7), Integer.valueOf(1));
      armorSlotMap.put(Integer.valueOf(4), Integer.valueOf(1));
      armorSlotMap.put(Integer.valueOf(3), Integer.valueOf(1));
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int ID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         String modelIDStr = Tools.getCellValue(sheet.getCell("C" + i));
         HashSet modelIDs = StringTools.analyzeIntegerHashset(modelIDStr, ",");
         if (modelIDs.size() < 1) {
            throw new Exception(sheet.getName() + "-装备数据没有填写，第" + i + "行");
         }

         Iterator var8 = modelIDs.iterator();

         while(var8.hasNext()) {
            Integer modelID = (Integer)var8.next();
            if (ItemModel.getModel(modelID.intValue()) == null) {
               throw new Exception(sheet.getName() + "-道具不存在，第" + i + "行");
            }

            itemToEffectDatas.put(modelID, ID);
         }

         ArmorEffectData data = new ArmorEffectData(ID, name, modelIDs);
         effectDatas.put(ID, data);
      }

   }

   public static void initAtomEffect(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int ID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int starLevel = Tools.getCellIntValue(sheet.getCell("B" + i));
         int chestID = Tools.getCellIntValue(sheet.getCell("C" + i));
         int footID = Tools.getCellIntValue(sheet.getCell("D" + i));
         ArmorEffectData data = getEffectData(ID);
         if (data == null) {
            throw new Exception(sheet.getName() + "- 没有填写相应的特效组合ID");
         }

         ArmorEffectAtom atom = new ArmorEffectAtom(starLevel, chestID, footID);
         data.getEffectAtoms().put(starLevel, atom);
      }

      Iterator var10 = effectDatas.values().iterator();

      while(var10.hasNext()) {
         ArmorEffectData effectData = (ArmorEffectData)var10.next();
         if (effectData.getEffectAtoms().size() < 1) {
            throw new Exception(sheet.getName() + "-" + effectData.getName() + "没有填写特效数据");
         }
      }

   }

   public static boolean isArmorSet(int itemType) {
      switch(itemType) {
      case 13:
      case 15:
      case 16:
      case 17:
      case 18:
      case 21:
         return true;
      case 14:
      case 19:
      case 20:
      default:
         return false;
      }
   }

   public static boolean isArmorSetBySlot(int slot) {
      switch(slot) {
      case 2:
      case 3:
      case 4:
      case 7:
      case 10:
         return true;
      case 5:
      case 6:
      case 8:
      case 9:
      default:
         return false;
      }
   }

   public static ArmorEffectAtom getEffectAtom(Item targetItem, Map itemMap) {
      if (!isArmorSet(targetItem.getItemType())) {
         return null;
      } else {
         int minLevel = StarForgingData.getCommonMaxStraLevel();
         Iterator var4 = armorSlotMap.keySet().iterator();

         while(var4.hasNext()) {
            Integer slotKey = (Integer)var4.next();
            Item item = (Item)itemMap.get(slotKey);
            if (item == null) {
               return null;
            }

            if (minLevel > item.getStarLevel()) {
               minLevel = item.getStarLevel();
            }
         }

         if (minLevel < 1) {
            return null;
         } else {
            ArmorEffectData data = getEffectDataByItemModelID(targetItem.getModelID());
            return data == null ? null : (ArmorEffectAtom)data.getEffectAtoms().get(minLevel);
         }
      }
   }

   public static ArmorEffectData getEffectDataByItemModelID(int itemModelID) {
      if (!itemToEffectDatas.containsKey(itemModelID)) {
         return null;
      } else {
         int dataID = ((Integer)itemToEffectDatas.get(itemModelID)).intValue();
         return (ArmorEffectData)effectDatas.get(dataID);
      }
   }

   public static boolean inEffectData(int itemModelID) {
      return itemToEffectDatas.containsKey(itemModelID);
   }

   public static ArmorEffectAtom getEffectAtom(int itemModelID, int starLevel) {
      if (!itemToEffectDatas.containsKey(itemModelID)) {
         return null;
      } else {
         ArmorEffectData data = getEffectData(((Integer)itemToEffectDatas.get(itemModelID)).intValue());
         return (ArmorEffectAtom)data.getEffectAtoms().get(starLevel);
      }
   }

   private static ArmorEffectData getEffectData(int dataID) {
      return (ArmorEffectData)effectDatas.get(dataID);
   }

   public int getID() {
      return this.ID;
   }

   public void setID(int iD) {
      this.ID = iD;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public HashSet getModelIDs() {
      return this.modelIDs;
   }

   public void setModelIDs(HashSet modelIDs) {
      this.modelIDs = modelIDs;
   }

   public HashMap getEffectAtoms() {
      return this.effectAtoms;
   }

   public void setEffectAtoms(HashMap effectAtoms) {
      this.effectAtoms = effectAtoms;
   }
}
