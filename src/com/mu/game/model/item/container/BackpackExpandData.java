package com.mu.game.model.item.container;

import com.mu.db.log.IngotChangeType;
import com.mu.game.model.item.container.imp.Backpack;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class BackpackExpandData {
   private static HashMap dataMap = new HashMap();
   private int page;
   private int ingot;
   private int needTime;
   private long rewardExp;

   public BackpackExpandData(int page, int ingot, int needTime, long rewardExp) {
      this.page = page;
      this.ingot = ingot;
      this.needTime = needTime;
      this.rewardExp = rewardExp;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      int i;
      for(i = 2; i <= rows; ++i) {
         int page = Tools.getCellIntValue(sheet.getCell("A" + i));
         int ingot = Tools.getCellIntValue(sheet.getCell("B" + i));
         int needTime = Tools.getCellIntValue(sheet.getCell("C" + i));
         long rewardExp = Tools.getCellLongValue(sheet.getCell("D" + i));
         String des = "第 " + i + " 行 - ";
         if (ingot < 1) {
            throw new Exception(sheet.getName() + des + " 砖石数量不正确");
         }

         if (needTime < 1) {
            throw new Exception(sheet.getName() + des + " 所需时间不正确");
         }

         if (rewardExp < 1L) {
            throw new Exception(sheet.getName() + des + " 经验奖励不正确");
         }

         BackpackExpandData bed = new BackpackExpandData(page, ingot, needTime, rewardExp);
         dataMap.put(page, bed);
      }

      for(i = 50; i <= 98; ++i) {
         if (!dataMap.containsKey(i)) {
            throw new Exception(sheet.getName() + ",数据不完整，缺少第" + i + " 格数据");
         }
      }

   }

   public static BackpackExpandData getExpandData(int page) {
      return (BackpackExpandData)dataMap.get(page);
   }

   public static BackpackExpandData getNextExpandData(Player player) {
      int page = player.getBackpack().getLimit();
      return getExpandData(page);
   }

   public static int canOpen(Player player, int targetPage) {
      BackpackExpandData data = null;
      int grid = player.getBackpack().getLimit();
      if (grid >= targetPage) {
         return 2002;
      } else if (targetPage > 98) {
         return 2001;
      } else {
         data = getExpandData(targetPage);
         return data == null ? 2002 : 1;
      }
   }

   public static long[] getRewardAndNeed(Player player, int targetPage) {
      Backpack backpack = player.getBackpack();
      int curWaitGrid = backpack.getCurWaitOpenGrid();
      long rewardExp = 0L;
      int needIngot = 0;
      long remainTime = -1L;

      for(int i = backpack.getLimit() + 1; i <= targetPage; ++i) {
         BackpackExpandData data = getExpandData(i);
         rewardExp += data.getRewardExp();
         if (i >= curWaitGrid) {
            needIngot += data.getIngot();
         }

         if (curWaitGrid == targetPage) {
            remainTime = (long)backpack.getRemainCoolTime();
         }
      }

      int type = 0;
      if (targetPage > curWaitGrid) {
         type = 1;
      } else if (targetPage < curWaitGrid) {
         type = 2;
      }

      return new long[]{rewardExp, (long)needIngot, remainTime, (long)type};
   }

   public static int addPage(Player player, int targetPage) {
      int result = canOpen(player, targetPage);
      if (result != 1) {
         return result;
      } else {
         long[] results = getRewardAndNeed(player, targetPage);
         long rewardExp = results[0];
         int needIngot = (int)results[1];
         if (needIngot > player.getIngot()) {
            return 1015;
         } else {
            if (needIngot != 0) {
               result = PlayerManager.reduceIngot(player, needIngot, IngotChangeType.OpenBackpack, String.valueOf(targetPage));
            }

            if (result == 1) {
               PlayerManager.addExp(player, rewardExp, -1L);
            }

            return result;
         }
      }
   }

   public int getNeedTime() {
      return this.needTime;
   }

   public void setNeedTime(int needTime) {
      this.needTime = needTime;
   }

   public long getRewardExp() {
      return this.rewardExp;
   }

   public void setRewardExp(long rewardExp) {
      this.rewardExp = rewardExp;
   }

   public int getPage() {
      return this.page;
   }

   public void setPage(int page) {
      this.page = page;
   }

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }
}
