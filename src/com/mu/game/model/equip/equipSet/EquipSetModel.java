package com.mu.game.model.equip.equipSet;

import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.service.StringTools;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EquipSetModel {
   private static Logger logger = LoggerFactory.getLogger(EquipSetModel.class);
   private static HashMap setMaps = new HashMap();
   private int setID;
   private String name;
   private String des;
   private HashMap setStats;
   private HashSet equipIDs;
   private int maxIndex = 0;
   private int maxSize = 0;
   private HashMap domiMap = new HashMap();

   public EquipSetModel(int setID) {
      this.setID = setID;
      this.setStats = new HashMap();
   }

   public static void init(Workbook wb) throws Exception {
      Sheet modelSheet = wb.getSheet(3);
      initSetModel(modelSheet);
      Sheet statSheet = wb.getSheet(4);
      initStat(statSheet);
      Iterator var4 = setMaps.values().iterator();

      while(var4.hasNext()) {
         EquipSetModel model = (EquipSetModel)var4.next();
         if (model.getEquipIDs().size() < 1) {
            logger.error("套装道具数量不正确 套装ID = {}", model.getSetID());
         } else if (model.getSetStats().size() < 1) {
            logger.error("套装属性数量不正确 套装ID = {}", model.getSetID());
         }
      }

   }

   public static void initSetModel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int setID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = sheet.getCell("B" + i).getContents();
         String des = sheet.getCell("C" + i).getContents();
         String equipIDStr = sheet.getCell("D" + i).getContents();
         int maxSize = Tools.getCellIntValue(sheet.getCell("E" + i));
         HashSet equipIDs = StringTools.analyzeIntegerHashset(equipIDStr, ",");
         EquipSetModel model = new EquipSetModel(setID);
         model.setName(name);
         model.setDes(des);
         model.setMaxSize(maxSize);
         Iterator var11 = equipIDs.iterator();

         while(var11.hasNext()) {
            Integer modelID = (Integer)var11.next();
            ItemModel itemmodel = ItemModel.getModel(modelID.intValue());
            if (itemmodel == null) {
               throw new Exception("装备套装，物品模板不存在 ，setID = " + setID + "，物品模板ID = " + modelID);
            }

            itemmodel.setSets(setID);
         }

         model.setEquipIDs(equipIDs);
         if (equipIDs.size() > maxSize) {
            throw new Exception("道具-装备--" + sheet.getName() + " : 最大数量填写错误");
         }

         addSetModel(model);
      }

   }

   public static void initStat(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int setID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int count = Tools.getCellIntValue(sheet.getCell("B" + i));
         String statStr = sheet.getCell("C" + i).getContents();
         int domi = Tools.getCellIntValue(sheet.getCell("D" + i));
         if (domi < 0) {
            throw new Exception(sheet.getName() + ",第" + i + "行战斗力数据无效");
         }

         EquipSetModel model = getModel(setID);
         if (model != null) {
            ArrayList statList = StringTools.analyzeArrayAttris(statStr, ",");
            model.addSetStat(count, statList);
            model.getDomiMap().put(count, domi);
         } else {
            logger.error("套装模板不存在 模板ID = {}", setID);
         }
      }

   }

   public void addSetStat(int count, ArrayList statList) {
      this.setStats.put(count, statList);
      if (this.getMaxIndex() < count) {
         this.setMaxIndex(count);
      }

   }

   public int getDomiByCount(int count) {
      return this.domiMap.containsKey(count) ? ((Integer)this.domiMap.get(count)).intValue() : 0;
   }

   public static void addSetModel(EquipSetModel model) {
      setMaps.put(model.getSetID(), model);
   }

   public static EquipSetModel getModel(int setID) {
      return (EquipSetModel)setMaps.get(setID);
   }

   public static Iterator getModelIterator() {
      return setMaps.values().iterator();
   }

   public static int getSize() {
      return setMaps.size();
   }

   public static HashMap getModelMap() {
      return setMaps;
   }

   public static boolean hasSet(int setID) {
      return setMaps.containsKey(setID);
   }

   public HashMap getDomiMap() {
      return this.domiMap;
   }

   public void setDomiMap(HashMap domiMap) {
      this.domiMap = domiMap;
   }

   public int getEquipSize() {
      return this.getEquipIDs().size();
   }

   public int getSetID() {
      return this.setID;
   }

   public void setSetID(int setID) {
      this.setID = setID;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public HashMap getSetStats() {
      return this.setStats;
   }

   public void setSetStats(HashMap setStats) {
      this.setStats = setStats;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public HashSet getEquipIDs() {
      return this.equipIDs;
   }

   public void setEquipIDs(HashSet equipIDs) {
      this.equipIDs = equipIDs;
   }

   public int getMaxSize() {
      return this.maxSize;
   }

   public void setMaxSize(int maxSize) {
      this.maxSize = maxSize;
   }

   public int getMaxIndex() {
      return this.maxIndex;
   }

   public void setMaxIndex(int maxIndex) {
      this.maxIndex = maxIndex;
   }
}
