package com.mu.game.model.rewardhall;

import com.mu.config.Global;
import com.mu.game.model.item.Item;
import com.mu.game.model.rewardhall.online.OnlineRewardDayData;
import com.mu.game.model.rewardhall.online.OnlineRewardWeekData;
import com.mu.game.model.rewardhall.sign.SignRewardData;
import com.mu.game.model.rewardhall.vitality.VitalityRewardData;
import com.mu.game.model.rewardhall.vitality.VitalityTaskData;
import com.mu.utils.CommonRegPattern;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RewardHallConfigManager {
   private static Logger logger = LoggerFactory.getLogger(RewardHallConfigManager.class);
   private static final HashMap signRewardMap = new LinkedHashMap();
   private static final HashMap vitalityTaskMap = new LinkedHashMap();
   private static final HashMap vitalityRewardMap = new LinkedHashMap();
   private static final HashMap onlineRewardDayMap = new LinkedHashMap();
   private static final List onlineRewardWeekList = new ArrayList();
   private static int MAX_VITALITY = 150;
   private static final int SIGN_ROUND_DAYS = 30;

   public static void initConfigs(InputStream in) throws BiffException, IOException {
      Workbook wb = null;

      try {
         wb = Workbook.getWorkbook(in);
         Sheet[] sheets = wb.getSheets();
         initSignRewardConfig(sheets[1], sheets[2]);
         initVitalityTaskConfig(sheets[3]);
         initVitalityRewardConfig(sheets[4]);
         initOnlineRewardDayConfig(sheets[5]);
         initOnlineRewardWeekConfig(sheets[6]);
      } finally {
         if (wb != null) {
            wb.close();
         }

      }

   }

   public static void parseRewardItemStr(Collection container, String parseStr) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(parseStr);

      while(m.find()) {
         int itemId = Integer.parseInt(m.group());
         m.find();
         int count = Integer.parseInt(m.group());
         m.find();
         boolean bind = Integer.parseInt(m.group()) != 0;
         m.find();
         int statRuleID = Integer.parseInt(m.group());
         m.find();
         long expireTime = Long.parseLong(m.group());
         RewardItemData rid = new RewardItemData(itemId, count);
         rid.setBind(bind);
         rid.setStatRuleID(statRuleID);
         rid.setExpireTime(expireTime);
         rid.buildItem();
         Item item = rid.getItem();
         if (item == null) {
            (new Exception()).printStackTrace();
         } else {
            container.add(rid);
         }
      }

   }

   private static void initSignRewardConfig(Sheet sheet1, Sheet sheet2) {
      int i;
      String A;
      String C;
      String D;
      SignRewardData data;
      for(i = 2; i <= sheet1.getRows(); ++i) {
         A = sheet1.getCell("A" + i).getContents().trim();
         C = sheet1.getCell("B" + i).getContents().trim();
         D = sheet1.getCell("C" + i).getContents().trim();
         data = new SignRewardData();
         data.setId(Integer.parseInt(A));
         data.setName(C);
         data.setCount(Integer.parseInt(D));
         signRewardMap.put(data.getId(), data);
      }

      for(i = 2; i <= sheet2.getRows(); ++i) {
         A = sheet2.getCell("A" + i).getContents().trim();
         C = sheet2.getCell("C" + i).getContents().trim();
         D = sheet2.getCell("D" + i).getContents().trim();
         data = (SignRewardData)signRewardMap.get(Integer.parseInt(A));
         if (data == null) {
            logger.error("sign rewards config config error, can not find SignRewardData by id {}", A);
         } else {
            List list1 = new ArrayList();
            List list2 = new ArrayList();
            parseRewardItemStr(list1, C);
            parseRewardItemStr(list2, D);
            data.addRoundReward(list1, list2);
         }
      }

   }

   private static void initVitalityTaskConfig(Sheet sheet) {
      int sumVitality = 0;

      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         String G = sheet.getCell("G" + i).getContents().trim();
         VitalityTaskData vtd = new VitalityTaskData();
         vtd.setId(Integer.parseInt(A));
         vtd.setName(B);
         vtd.setVitality(Integer.parseInt(C));
         vtd.setTargetStr(D);
         vtd.setHasEnter(Integer.parseInt(E) == 1);
         vtd.setEnterFunction(Integer.parseInt(F));
         vtd.setEnterStr(G);
         sumVitality += vtd.getVitality();
         vitalityTaskMap.put(vtd.getId(), vtd);
      }

      MAX_VITALITY = Math.max(MAX_VITALITY, sumVitality);
   }

   private static void initVitalityRewardConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         VitalityRewardData vrd = new VitalityRewardData();
         vrd.setId(Integer.parseInt(A));
         vrd.setVitality(Integer.parseInt(B));
         parseRewardItemStr(vrd.getRewardList(), C);
         if (vrd.getRewardList().size() > 4) {
            logger.error("活跃度奖励不能大于4个");
         }

         vitalityRewardMap.put(vrd.getId(), vrd);
      }

   }

   private static void initOnlineRewardDayConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         OnlineRewardDayData dd = new OnlineRewardDayData();
         dd.setId(Integer.parseInt(A));
         dd.setSeconds(Integer.parseInt(B));
         dd.setIDU(Tools.parseItemDataUnit(C));
         onlineRewardDayMap.put(dd.getId(), dd);
      }

   }

   private static void initOnlineRewardWeekConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         OnlineRewardWeekData wd = new OnlineRewardWeekData();
         wd.setId(Integer.parseInt(A));
         wd.setHourIngot(Integer.parseInt(B));
         wd.setMaxHour(Integer.parseInt(C));
         wd.setStr1(D);
         wd.setStr2(E);
         wd.setStr3(F);
         onlineRewardWeekList.add(wd);
      }

   }

   public static int getSignRewardSize() {
      return signRewardMap.size();
   }

   public static Iterator getSignRewardIterator() {
      return signRewardMap.values().iterator();
   }

   public static SignRewardData getSignRewardData(int id) {
      return (SignRewardData)signRewardMap.get(id);
   }

   public static int getOnlineRewardSize() {
      return onlineRewardDayMap.size();
   }

   public static Iterator getOnlineRewardIterator() {
      return onlineRewardDayMap.values().iterator();
   }

   public static OnlineRewardDayData getOnlineRewardData(int id) {
      return (OnlineRewardDayData)onlineRewardDayMap.get(id);
   }

   public static OnlineRewardWeekData getOnlineRewardWeek() {
      return getOnlineRewardWeek(getOnlineRewardWeekIndex());
   }

   public static OnlineRewardWeekData getOnlineRewardWeek(int index) {
      int week = Math.max(0, Math.min(onlineRewardWeekList.size() - 1, index));
      return (OnlineRewardWeekData)onlineRewardWeekList.get(week);
   }

   public static int getOnlineRewardWeekIndex() {
      Date date = Global.getOpenServerTiem();
      int day = (int)((System.currentTimeMillis() - Time.getTodayBegin(date.getTime())) / 86400000L);
      return day / 7;
   }

   public static int getMaxVitality() {
      return MAX_VITALITY;
   }

   public static int getVitalityTaskSize() {
      return vitalityTaskMap.size();
   }

   public static Iterator getVitalityTaskIterator() {
      return vitalityTaskMap.values().iterator();
   }

   public static int getVitalityRewardSize() {
      return vitalityRewardMap.size();
   }

   public static Iterator getVitalityRewardIterator() {
      return vitalityRewardMap.values().iterator();
   }

   public static VitalityRewardData getVitalityRewardData(int id) {
      return (VitalityRewardData)vitalityRewardMap.get(id);
   }

   public static VitalityTaskData getVitalityTaskData(int id) {
      return (VitalityTaskData)vitalityTaskMap.get(id);
   }

   public static int getSignRoundDays() {
      return 30;
   }
}
