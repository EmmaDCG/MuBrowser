package com.mu.game.model.equip.rune;

import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.service.StringTools;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;

public class RuneSetModel {
   private static HashMap models = new HashMap();
   private static List modelList = new ArrayList();
   private int modelID;
   private String name = "";
   private int effectCount;
   private List stats = null;

   public RuneSetModel(int modelID, int effectCount) {
      this.modelID = modelID;
      this.effectCount = effectCount;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int modelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int effectCount = Tools.getCellIntValue(sheet.getCell("B" + i));
         String statStr = Tools.getCellValue(sheet.getCell("C" + i));
         List statList = StringTools.analyzeArrayAttris(statStr, ",");
         if (statList.size() < 1) {
            throw new Exception("符文增加属性长度  < 1 ,modelID = " + modelID);
         }

         if (ItemModel.getModel(modelID) == null) {
            throw new Exception(sheet.getName() + "-符文道具ID 不存在");
         }

         RuneSetModel model = new RuneSetModel(modelID, effectCount);
         model.setStats(statList);
         model.setName(ItemModel.getModel(modelID).getName());
         addModel(model);
      }

   }

   public static void addModel(RuneSetModel model) {
      models.put(model.getModelID(), model);
      modelList.add(model.getModelID());
   }

   public static RuneSetModel getModel(int modelID) {
      return (RuneSetModel)models.get(modelID);
   }

   public static boolean hasModel(int modelID) {
      return models.containsKey(modelID);
   }

   public static List getModelList() {
      return modelList;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getEffectCount() {
      return this.effectCount;
   }

   public void setEffectCount(int effectCount) {
      this.effectCount = effectCount;
   }

   public List getStats() {
      return this.stats;
   }

   public void setStats(List stats) {
      this.stats = stats;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
