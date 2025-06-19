package com.mu.game.model.shield;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ShieldConfigManager {
   private static final List levelList = new ArrayList();
   private static final List rankList = new ArrayList();
   public static final int EXCHANGE_RATE = 10000;
   private static int PerRankLevelSize = 0;
   private static ShieldRank FirstRank = null;

   public static void initConfigs(InputStream in) throws BiffException, IOException {
      Workbook wb = null;

      try {
         wb = Workbook.getWorkbook(in);
         Sheet[] sheets = wb.getSheets();
         initShieldLevelConfig(sheets[1]);
         initShieldRankConfig(sheets[2]);
      } finally {
         if (wb != null) {
            wb.close();
         }

      }

   }

   private static void initShieldLevelConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         ShieldLevel level = new ShieldLevel();
         level.setLevel(Integer.parseInt(A));
         level.setShieldValue(Integer.parseInt(B));
         level.setExpend(Integer.parseInt(C));
         level.setShieldRecover(Integer.parseInt(D));
         level.setMallItemId(Integer.parseInt(E));
         level.setZDL(Integer.parseInt(F));
         levelList.add(level);
      }

   }

   private static void initShieldRankConfig(Sheet sheet) {
      ShieldRank lastRank = null;
      int r = -1;

      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         String G = sheet.getCell("G" + i).getContents().trim();
         String H = sheet.getCell("H" + i).getContents().trim();
         String I = sheet.getCell("I" + i).getContents().trim();
         String J = sheet.getCell("J" + i).getContents().trim();
         String K = sheet.getCell("K" + i).getContents().trim();
         String L = sheet.getCell("L" + i).getContents().trim();
         String M = sheet.getCell("M" + i).getContents().trim();
         ShieldRank rank = new ShieldRank();
         rank.setRank(Integer.parseInt(A));
         rank.setStar(Integer.parseInt(B));
         rank.setLimit(Integer.parseInt(C));
         rank.setExpendStr(D);
         rank.addProperty(E);
         rank.addProperty(F);
         rank.addProperty(G);
         rank.addProperty(H);
         rank.addProperty(I);
         rank.addProperty(J);
         rank.addProperty(K);
         rank.setEffect(Integer.parseInt(L));
         rank.setZDL(Integer.parseInt(M));
         if (lastRank != null) {
            lastRank.setNext(rank);
         }

         lastRank = rank;
         rankList.add(rank);
         if (r == -1 || rank.getRank() == r) {
            r = rank.getRank();
            ++PerRankLevelSize;
         }

         if (FirstRank == null) {
            FirstRank = rank;
         }
      }

   }

   public static ShieldLevel getLevel(int level) {
      int index = Math.max(0, Math.min(levelList.size() - 1, level - 1));
      return (ShieldLevel)levelList.get(index);
   }

   public static ShieldRank getRank(int rank, int star) {
      int index = Math.max(0, Math.min(rankList.size() - 1, (rank - FirstRank.getRank()) * PerRankLevelSize + star - FirstRank.getStar()));
      return (ShieldRank)rankList.get(index);
   }
}
