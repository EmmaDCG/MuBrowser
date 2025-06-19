package com.mu.game.model.unit.buff.model;

import com.mu.game.model.service.StringTools;
import com.mu.game.model.stats.StatEnum;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;

public class BuffDynamicData {
   private static HashMap dyDatas = new HashMap();
   private int modelID;
   private int level;
   private int rate;
   private int duration;
   private List props = null;
   private StatEnum strStat = null;

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         BuffDynamicData dyData = new BuffDynamicData();
         dyData.setModelID(Tools.getCellIntValue(sheet.getCell("A" + i)));
         dyData.setLevel(Tools.getCellIntValue(sheet.getCell("B" + i)));
         dyData.setRate(Tools.getCellIntValue(sheet.getCell("C" + i)));
         dyData.setDuration(Tools.getCellIntValue(sheet.getCell("D" + i)));
         List props = StringTools.analyzeBuffProps(Tools.getCellValue(sheet.getCell("E" + i)));
         dyData.setProps(props);
         String strStatString = sheet.getCell("F" + i).getContents();
         if (strStatString != null && strStatString.length() > 0) {
            StatEnum strStat = StatEnum.find(Integer.parseInt(strStatString));
            dyData.setStrStat(strStat);
         }

         HashMap levelMaps = (HashMap)dyDatas.get(dyData.getModelID());
         if (levelMaps == null) {
            levelMaps = new HashMap();
            dyDatas.put(dyData.getModelID(), levelMaps);
         }

         levelMaps.put(dyData.getLevel(), dyData);
      }

   }

   public static BuffDynamicData getDyData(int buffModelID, int level) {
      HashMap levelMaps = (HashMap)dyDatas.get(buffModelID);
      return levelMaps != null ? (BuffDynamicData)levelMaps.get(level) : null;
   }

   public StatEnum getStrStat() {
      return this.strStat;
   }

   public void setStrStat(StatEnum strStat) {
      this.strStat = strStat;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getRate() {
      return this.rate;
   }

   public void setRate(int rate) {
      this.rate = rate;
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public List getProps() {
      return this.props;
   }

   public void setProps(List props) {
      this.props = props;
   }
}
