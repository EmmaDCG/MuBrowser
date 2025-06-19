package com.mu.game.model.spiritOfWar.model;

import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class SpiritRankModel {
   private static HashMap modelMap = new HashMap();
   public static int maxRank = 1;
   private int rank;
   private int source;
   private int scale;
   private String name;

   public SpiritRankModel(int rank, int source, int scale) {
      this.rank = rank;
      this.source = source;
      this.scale = scale;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      int i;
      for(i = 2; i <= rows; ++i) {
         int rank = Tools.getCellIntValue(sheet.getCell("A" + i));
         int source = Tools.getCellIntValue(sheet.getCell("B" + i));
         int scale = Tools.getCellIntValue(sheet.getCell("C" + i));
         String name = Tools.getCellValue(sheet.getCell("D" + i));
         if (rank > maxRank) {
            maxRank = rank;
         }

         SpiritRankModel rankModel = new SpiritRankModel(rank, source, scale);
         rankModel.setName(name);
         modelMap.put(rank, rankModel);
      }

      for(i = 1; i <= maxRank; ++i) {
         if (getRankModel(i) == null) {
            throw new Exception("战魂-" + sheet.getName() + ",第" + i + "个等阶不存在 ");
         }
      }

   }

   public static SpiritRankModel getRankModel(int rank) {
      return (SpiritRankModel)modelMap.get(rank);
   }

   public int getRank() {
      return this.rank;
   }

   public void setRank(int rank) {
      this.rank = rank;
   }

   public int getSource() {
      return this.source;
   }

   public void setSource(int source) {
      this.source = source;
   }

   public int getScale() {
      return this.scale;
   }

   public void setScale(int scale) {
      this.scale = scale;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
