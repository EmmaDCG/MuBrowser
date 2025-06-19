package com.mu.game.model.financing;

import com.mu.game.model.rewardhall.RewardHallConfigManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinancingConfigManager {
   private static Logger logger = LoggerFactory.getLogger(FinancingConfigManager.class);
   private static final HashMap itemMap = new LinkedHashMap();
   private static final HashMap rewardMap = new LinkedHashMap();

   public static void initConfigs(InputStream in) throws BiffException, IOException {
      Workbook wb = null;

      try {
         wb = Workbook.getWorkbook(in);
         Sheet[] sheets = wb.getSheets();
         initItemDataConfig(sheets[1]);
         initRewardDataConfig(sheets[2]);
      } finally {
         if (wb != null) {
            wb.close();
         }

      }

   }

   private static void initItemDataConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         FinancingItemData fid = new FinancingItemData();
         fid.setId(Integer.parseInt(A));
         fid.setName(B);
         fid.setImage1(Integer.parseInt(C));
         fid.setImage2(Integer.parseInt(D));
         fid.setPrice(Integer.parseInt(E));
         fid.setConditionType(Integer.parseInt(F));
         itemMap.put(fid.getId(), fid);
      }

   }

   private static void initRewardDataConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         FinancingItemData fid = getItemData(Integer.parseInt(A));
         if (fid == null) {
            logger.error("FinancingItemRewardData config error, not found FinancingItemData by id {} ", Integer.parseInt(A));
            break;
         }

         FinancingItemRewardData fird = new FinancingItemRewardData();
         fird.setItemData(fid);
         fird.setId(Integer.parseInt(B));
         fird.setName(C);
         fird.setImage(Integer.parseInt(D));
         RewardHallConfigManager.parseRewardItemStr(fird.getRewardList(), E);
         if (fird.getRewardList().size() > 4) {
            logger.error("投资奖励不能大于4个");
         }

         fird.setConditionValue(Integer.parseInt(F));
         fid.addReward(fird);
         rewardMap.put(fird.getId(), fird);
      }

   }

   public static FinancingItemData getItemData(int id) {
      return (FinancingItemData)itemMap.get(id);
   }

   public static FinancingItemRewardData getRewardData(int id) {
      return (FinancingItemRewardData)rewardMap.get(id);
   }

   public static int getItemSize() {
      return itemMap.size();
   }

   public static Iterator getItemIterator() {
      return itemMap.values().iterator();
   }
}
