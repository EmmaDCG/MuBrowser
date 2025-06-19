package com.mu.game.model.luckyTurnTable;

import com.mu.config.Global;
import com.mu.db.log.IngotChangeType;
import com.mu.executor.Executor;
import com.mu.game.model.luckyTurnTable.weight.TurnTableElement;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.task.specified.SpecifiedTimeManager;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.luckyTurnTabel.LuckyTurnTabelRecord;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import jxl.Sheet;
import jxl.Workbook;

public class LuckyTurnTableManager {
   private static SortedMap tableMap = new TreeMap();
   private static HashMap ruleMap = new HashMap();
   private static ConcurrentLinkedQueue recordList = new ConcurrentLinkedQueue();
   public static String ruleDes = "";
   private static int MaxCount = 1;
   private static Date endTime;
   private static Date openTime;
   private static int limitVipLevel = 3;
   private static int vipAddCount = 2;
   private static final int PeriodMoney = 100;
   private static final int PeriodCount = 2;
   private static TimerTask closeTask = null;

   public static void init(InputStream in) throws Exception {
      closeTask = new TimerTask() {
         public void run() {
            LuckyTurnTableManager.doClose();
         }
      };
      Workbook wb = Workbook.getWorkbook(in);
      initTable(wb.getSheet(1));
      initRule(wb.getSheet(2));
   }

   public static int getMaxCount() {
      return MaxCount;
   }

   public static void initTable(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int tableID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int source = Tools.getCellIntValue(sheet.getCell("B" + i));
         String name = Tools.getCellValue(sheet.getCell("C" + i));
         float multiple = Tools.getCellFloatValue(sheet.getCell("D" + i));
         if (multiple < 0.0F) {
            throw new Exception(sheet.getName() + "-倍数不正确");
         }

         TurnTable table = new TurnTable(tableID, source, name, multiple);
         tableMap.put(tableID, table);
      }

   }

   public static void initRule(Sheet sheet) throws Exception {
      int time = Tools.getCellIntValue(sheet.getCell("B1"));
      if (time < 1) {
         throw new Exception(sheet.getName() + " 过期时间不正确");
      } else {
         initTime(time);
         ruleDes = Tools.getCellValue(sheet.getCell("B2"));
         limitVipLevel = Tools.getCellIntValue(sheet.getCell("B3"));
         vipAddCount = Tools.getCellIntValue(sheet.getCell("D3"));
         int rows = sheet.getRows();

         int i;
         for(i = 5; i <= rows; ++i) {
            int count = Tools.getCellIntValue(sheet.getCell("A" + i));
            int needIngot = Tools.getCellIntValue(sheet.getCell("B" + i));
            String weightStr = Tools.getCellValue(sheet.getCell("C" + i));
            TurnTableElement element = new TurnTableElement();
            element.parse(weightStr, "转盘规则 - 第" + i + "行");
            TurnTableRule rule = new TurnTableRule(count, needIngot, element);
            ruleMap.put(rule.getCount(), rule);
            if (count > MaxCount) {
               MaxCount = count;
            }
         }

         for(i = 1; i <= MaxCount; ++i) {
            if (getRule(i) == null) {
               throw new Exception(sheet.getName() + "-第" + i + "次数据不存在");
            }
         }

      }
   }

   private static void doClose() {
      UpdateMenu.allPlayerUpdate(35);
   }

   private static void initTime(int durationDay) {
      Calendar oc = Calendar.getInstance();
      oc.setTime(Global.getOpenServerTiem());
      openTime = oc.getTime();
      Calendar ec = Calendar.getInstance();
      ec.setTime(Global.getOpenServerTiem());
      ec.add(11, durationDay);
      endTime = ec.getTime();
      if (endTime.after(Calendar.getInstance().getTime())) {
         SpecifiedTimeManager.schedule(closeTask, endTime);
      }

   }

   public static boolean hasLuckyTable(int tableID) {
      return tableMap.containsKey(tableID);
   }

   public static TurnTable getTurnTable(int tableID) {
      return (TurnTable)tableMap.get(tableID);
   }

   public static SortedMap getTableMap() {
      return tableMap;
   }

   public static TurnTableRule getRule(int count) {
      return (TurnTableRule)ruleMap.get(count);
   }

   public static int getNeedIngotByShow(int count) {
      return ruleMap.containsKey(count) ? getRule(count).getNeedIngot() : getRule(MaxCount).getNeedIngot();
   }

   public static List getAllRecordList() {
      List list = new ArrayList();
      list.addAll(recordList);
      return list;
   }

   public static boolean isOpen() {
      if (endTime != null && openTime != null) {
         Date curDate = Calendar.getInstance().getTime();
         return curDate.after(openTime) && curDate.before(endTime);
      } else {
         return false;
      }
   }

   public static boolean isEnd() {
      return false;
   }

   public static int getRemainTime() {
      if (!isOpen()) {
         return 0;
      } else {
         Date curDate = Calendar.getInstance().getTime();
         long remainTime = endTime.getTime() - curDate.getTime();
         remainTime /= 1000L;
         return (int)remainTime;
      }
   }

   public static int getRemainCount(Player player) {
      if (player.getTurnTableCount() >= MaxCount) {
         return 0;
      } else {
         int count = player.getUser().getPay(openTime.getTime(), System.currentTimeMillis());
         if (count < 100) {
            return 0;
         } else {
            count = count / 100 * 2;
            count = Math.min(count, MaxCount - vipAddCount);
            if (!player.getVIPManager().isTimeOut() && player.getVIPManager().getLevel().getLevel() >= limitVipLevel) {
               count += vipAddCount;
            }

            count = Math.max(0, count - player.getTurnTableCount());
            return count;
         }
      }
   }

   public static int canTurn(Player player) {
      if (!isOpen()) {
         return 23409;
      } else if (getRemainCount(player) < 1) {
         if (player.getTurnTableCount() >= MaxCount) {
            return 23410;
         } else if (player.getTurnTableCount() < MaxCount - vipAddCount) {
            return 23408;
         } else {
            return !player.getVIPManager().isTimeOut() && player.getVIPManager().getLevel().getLevel() >= limitVipLevel ? 23408 : 23411;
         }
      } else {
         return 1;
      }
   }

   public static int[] doLuckyTurn(Player player) {
      int[] results = new int[]{1, 0, 0};
      int result = canTurn(player);
      if (result != 1) {
         results[0] = result;
         return results;
      } else {
         int count = player.getTurnTableCount();
         ++count;
         TurnTableRule rule = getRule(count);
         if (player.getIngot() < rule.getNeedIngot()) {
            results[0] = 23412;
            return results;
         } else {
            int tableID = rule.getRndTableID();
            TurnTable table = getTurnTable(tableID);
            float multiple = table.getMultiple();
            int successIngot = (int)(multiple * (float)rule.getNeedIngot());
            PlayerManager.reduceIngot(player, rule.getNeedIngot(), IngotChangeType.LuckyTurnTable, String.valueOf(count));
            PlayerManager.addIngot(player, successIngot, IngotChangeType.LuckyTurnTable.getType());
            player.setTurnTableCount(count);
            savePlayerLuckyCount(player);
            addRecord(player, multiple, successIngot);
            results[1] = tableID;
            results[2] = successIngot;
            UpdateMenu.update(player, 35);
            return results;
         }
      }
   }

   private static void savePlayerLuckyCount(Player player) {
      WriteOnlyPacket packet = Executor.SaveLuckyTurn.toPacket(player);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   private static void addRecord(Player player, float multiple, int successIngot) {
      TurnTableRecord record = new TurnTableRecord(player.getID(), player.getName(), (int)(multiple * 1000.0F), successIngot);
      if (recordList.size() >= 30) {
         recordList.poll();
      }

      recordList.add(record);
      LuckyTurnTabelRecord.sendToOther(player, record);
   }
}
