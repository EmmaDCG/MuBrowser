package com.mu.game.model.hallow.model;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;

public class PartModel {
   private static HashMap partMap = new HashMap();
   private int rank;
   private int level;
   private int itemModelID;
   private int count;
   private List modifies = null;
   private String showStat = "";
   private int domineering;
   private String modifyString = "";
   private Item item = null;

   public PartModel(int rank, int level, int itemModelID, int count, int domineering) {
      this.rank = rank;
      this.level = level;
      this.itemModelID = itemModelID;
      this.count = count;
      this.domineering = domineering;
   }

   public static void initPart(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int rank = Tools.getCellIntValue(sheet.getCell("A" + i));
         int level = Tools.getCellIntValue(sheet.getCell("B" + i));
         int itemModelId = Tools.getCellIntValue(sheet.getCell("C" + i));
         int count = Tools.getCellIntValue(sheet.getCell("D" + i));
         String statStr = Tools.getCellValue(sheet.getCell("E" + i));
         String showStat = Tools.getCellValue(sheet.getCell("F" + i));
         int domineering = Tools.getCellIntValue(sheet.getCell("G" + i));
         Item item = ItemTools.createItem(itemModelId, count, 2);
         if (!ItemModel.hasItemModel(itemModelId) || count < 1 || item == null) {
            throw new Exception("圣器 - " + sheet.getName() + "道具不存在或者数量不对，第" + i);
         }

         List modifies = StringTools.analyzeArrayAttris(statStr, ",");
         if (modifies.size() < 1) {
            throw new Exception("圣器-" + sheet.getName() + "属性值不正确，第" + i);
         }

         if (domineering < 1) {
            throw new Exception(sheet.getName() + ",战斗力不正确，第" + i);
         }

         String statString = getStatString(modifies);
         modifies.add(new FinalModify(StatEnum.DOMINEERING, domineering, StatModifyPriority.ADD));
         PartModel model = new PartModel(rank, level, itemModelId, count, domineering);
         model.setItem(item);
         model.setShowStat(showStat);
         model.setModifies(modifies);
         model.setModifyString(statString);
         int key = createKey(rank, level);
         partMap.put(key, model);
      }

   }

   public static String getStatString(List fmList) {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < fmList.size(); ++i) {
         FinalModify fm = (FinalModify)fmList.get(i);
         sb.append(fm.getStat().getName());
         sb.append("#t");
         sb.append("#F{e=7}");
         sb.append("+");
         sb.append(fm.getShowValue() + fm.getSuffix());
         sb.append("#F");
         if (i < fmList.size() - 1) {
            sb.append("#b");
         }
      }

      return sb.toString();
   }

   private static int createKey(int rank, int level) {
      return rank * 1000 + level;
   }

   public static PartModel getPartModel(int rank, int level) {
      return (PartModel)partMap.get(createKey(rank, level));
   }

   public String getModifyString() {
      return this.modifyString;
   }

   public void setModifyString(String modifyString) {
      this.modifyString = modifyString;
   }

   public int getRank() {
      return this.rank;
   }

   public void setRank(int rank) {
      this.rank = rank;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getItemModelID() {
      return this.itemModelID;
   }

   public String getShowStat() {
      return this.showStat;
   }

   public void setShowStat(String showStat) {
      this.showStat = showStat;
   }

   public void setItemModelID(int itemModelID) {
      this.itemModelID = itemModelID;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public List getModifies() {
      return this.modifies;
   }

   public void setModifies(List modifies) {
      this.modifies = modifies;
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }

   public Item getItem() {
      return this.item;
   }

   public void setItem(Item item) {
      this.item = item;
   }
}
