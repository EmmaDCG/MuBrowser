package com.mu.game.model.task;

import com.mu.game.model.task.clazz.TaskClazz;
import com.mu.game.model.task.clazz.TaskClazzRC;
import com.mu.game.model.task.clazz.TaskClazzXS;
import com.mu.game.model.task.clazz.TaskClazzZJ;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.CommonRegPattern;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.jdom.IllegalDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskConfigManager {
   private static Logger logger = LoggerFactory.getLogger(TaskConfigManager.class);
   private static final Map dataMap = new HashMap();
   private static final Map dialogMap = new HashMap();
   private static final Map positionMap = new HashMap();
   private static final Map zjMap = new LinkedHashMap();
   private static final List rcList = new ArrayList();
   private static final Map xsMap = new HashMap();
   private static final List zjTaskList = new ArrayList();
   private static TaskData zjTaskHeader = null;
   private static final Set ghTaskList = new LinkedHashSet();
   private static final List zxTaskList = new ArrayList();
   private static TaskData zxTaskHeader = null;
   public static final int MaxRCStar = 10;
   public static final int MaxRCBuy = 2;
   private static final int maxRCLoop = 10;
   public static final int TASK_RC_SUBMIT_MULTIPLE = 5;
   public static int TASK_TRACE_ZJ_INDEX = 4;
   public static final int TASK_TRACE_PT = 1;
   public static final int TASK_TRACE_ZJ = 2;
   public static final int[] NPC_TASK_HEADER = new int[3];
   public static final int CLEAR_HOUR = 0;
   public static final int AKeyCompleteAllRCTaskExpendIngot = 90;
   private static final HashMap cjTaskMap = new HashMap();

   public static void initConfigs(InputStream in) throws BiffException, IOException {
      Workbook wb = null;

      try {
         wb = Workbook.getWorkbook(in);
         Sheet[] sheets = wb.getSheets();
         initTaskDialogConfig(sheets[2]);
         initTaskPositionConfig(sheets[3]);
         initTaskZhangJieConfig(sheets[4]);
         initTaskXuanShangConfig(sheets[6]);
         initTaskDataConfig(sheets[1]);
         initTaskRiChangConfig(sheets[5]);
         initStaticDataConfig(sheets[7]);
         TaskData lastData = null;
         List continueDisabledList = new ArrayList();
         Iterator it = zjMap.values().iterator();

         label106:
         while(it.hasNext()) {
            TaskClazzZJ zj = (TaskClazzZJ)it.next();
            Iterator itt = zj.getIterator();

            while(true) {
               while(true) {
                  if (!itt.hasNext()) {
                     continue label106;
                  }

                  TaskData data = (TaskData)itt.next();
                  data.setClazzIndex(zjTaskList.size());
                  if (data.isDisabled()) {
                     itt.remove();
                     continueDisabledList.add(data);
                  } else {
                     if (lastData == null) {
                        zjTaskHeader = data;
                     } else {
                        lastData.setClazzNext(data);
                     }

                     Iterator ittt = continueDisabledList.iterator();

                     while(ittt.hasNext()) {
                        ((TaskData)ittt.next()).setClazzNext(data);
                     }

                     continueDisabledList.clear();
                     zjTaskList.add(data);
                     lastData = data;
                  }
               }
            }
         }
      } finally {
         if (wb != null) {
            wb.close();
         }

      }

   }

   private static void initStaticDataConfig(Sheet sheet) {
      for(int i = 0; i < NPC_TASK_HEADER.length; ++i) {
         NPC_TASK_HEADER[i] = Integer.parseInt(sheet.getCell("B" + (2 + i)).getContents().trim());
      }

      TASK_TRACE_ZJ_INDEX = Integer.parseInt(sheet.getCell("B" + (2 + NPC_TASK_HEADER.length)).getContents().trim());
   }

   private static void initTaskDataConfig(Sheet sheet) {
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
         String N = sheet.getCell("N" + i).getContents().trim();
         String O = sheet.getCell("O" + i).getContents().trim();
         String P = sheet.getCell("P" + i).getContents().trim();
         String Q = sheet.getCell("Q" + i).getContents().trim();
         String R = sheet.getCell("R" + i).getContents().trim();
         String S = sheet.getCell("S" + i).getContents().trim();
         String T = sheet.getCell("T" + i).getContents().trim();
         String U = sheet.getCell("U" + i).getContents().trim();
         String V = sheet.getCell("V" + i).getContents().trim();
         String W = sheet.getCell("W" + i).getContents().trim();
         String X = sheet.getCell("X" + i).getContents().trim();
         TaskData data = new TaskData(Integer.parseInt(A), B);
         data.setIcon(C);
         data.setDisabled("1".equals(D));
         data.setClazz(TaskClazz.valueOf(Integer.parseInt(E)));
         data.setClazzName(F);
         data.setClazzId(Integer.parseInt(G));
         data.setAutoAccept("1".equals(H));
         data.setAutoSubmit("1".equals(I));
         data.setLevel(Integer.parseInt(J));
         data.setBeforeTask(Integer.parseInt(K));
         data.setAcceptNpc(Long.parseLong(L));
         data.setSubmitNpc(Long.parseLong(N));
         data.setAcceptDialog((TaskDialog)dialogMap.get(Integer.parseInt(M)));
         data.setSubmitDialog((TaskDialog)dialogMap.get(Integer.parseInt(O)));
         data.setDescription(P);
         data.setTargetStr(Q);
         data.setLinkStr(R);
         data.setAcceptTraceStr(S);
         data.setTargetTraceStr(T);
         data.setSubmitTraceStr(U);
         data.setRewardStr(V);
         data.setQuality(Integer.parseInt(W));
         data.setRunLink(X);
         if (data.getClazz() != null) {
            dataMap.put(data.getId(), data);
            if (data.is(TaskClazz.ZJ)) {
               TaskClazzZJ zj = (TaskClazzZJ)zjMap.get(data.getClazzId());
               if (zj == null) {
                  throw new IllegalDataException("task config err");
               }

               zj.addTask(data);
            } else if (data.is(TaskClazz.XS)) {
               TaskClazzXS xs = (TaskClazzXS)xsMap.get(data.getClazzId());
               if (xs == null) {
                  throw new IllegalDataException("task config err");
               }

               xs.addTask(data);
            } else if (data.is(TaskClazz.GH) && !data.isDisabled()) {
               ghTaskList.add(data);
            } else if (data.is(TaskClazz.ZX) && !data.isDisabled()) {
               data.setClazzIndex(zxTaskList.size());
               zxTaskList.add(data);
               if (zxTaskHeader == null) {
                  zxTaskHeader = data;
               } else {
                  ((TaskData)zxTaskList.get(zxTaskList.size() - 2)).setClazzNext(data);
               }
            }
         } else {
            logger.error("task config error, not found task[{}] clazz '{}'", data.getId(), E);
         }
      }

   }

   private static void initTaskDialogConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         TaskDialog dialog = new TaskDialog(Integer.parseInt(A), B, C, Integer.parseInt(D), E);
         dialogMap.put(dialog.getId(), dialog);
      }

   }

   private static void initTaskPositionConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();

         try {
            TaskPosition position = new TaskPosition(Integer.parseInt(A), Integer.parseInt(B), Integer.parseInt(C), Integer.parseInt(D));
            positionMap.put(position.getId(), position);
         } catch (Exception var7) {
            logger.error("TaskPosition[{}] config err, " + var7.toString(), A);
            var7.printStackTrace();
         }
      }

   }

   private static void initTaskZhangJieConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         TaskClazzZJ zj = new TaskClazzZJ(Integer.parseInt(A), B);
         zj.setIndex(i - 2);
         zjMap.put(zj.getId(), zj);
      }

   }

   private static void initTaskRiChangConfig(Sheet sheet) {
      for(int i = 2; i <= sheet.getRows(); ++i) {
         String A = sheet.getCell("A" + i).getContents().trim();
         String B = sheet.getCell("B" + i).getContents().trim();
         String C = sheet.getCell("C" + i).getContents().trim();
         String D = sheet.getCell("D" + i).getContents().trim();
         String E = sheet.getCell("E" + i).getContents().trim();
         String F = sheet.getCell("F" + i).getContents().trim();
         String G = sheet.getCell("G" + i).getContents().trim();
         String H = sheet.getCell("H" + i).getContents().trim();
         TaskClazzRC rc = new TaskClazzRC(Integer.parseInt(A));
         rc.setRewardStarFactorStr(B);
         rc.setRefreshStarExpend(Integer.parseInt(C));
         rc.setTaskSetStr(D);
         rc.setBaseRewardStr(E);
         rc.setSubmitFiveMultipleExpend(Integer.parseInt(F));
         rc.setBuyCountExpend(Integer.parseInt(G));
         rc.setRefreshStarStr(H);
         rcList.add(rc);
      }

   }

   private static void initTaskXuanShangConfig(Sheet sheet) {
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
         TaskClazzXS xs = new TaskClazzXS(Integer.parseInt(A));
         xs.setSort(Integer.parseInt(B));
         xs.setLevel(Integer.parseInt(C));
         xs.setColor(Integer.parseInt(D));
         xs.setName(E);
         xs.setIcon(Integer.parseInt(F));
         xs.setPositionId(Integer.parseInt(G));
         xs.setPositionIcon(Integer.parseInt(H));
         xs.setDescription(I);
         xs.setOpenItem(J);
         xs.setShowRewardList(K);
         xs.setOneDayCountLimit(Integer.parseInt(L));
         xs.setHCPanelStr(M);
         xsMap.put(xs.getId(), xs);
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
         int attributeGroupId = Integer.parseInt(m.group());
         m.find();
         long expireTime = Long.parseLong(m.group());
         TaskRewardItemData tr = TaskRewardItemData.newInstance(itemId, count, bind, attributeGroupId, expireTime);
         container.add(tr);
      }

   }

   public static TaskData getData(int id) {
      return (TaskData)dataMap.get(id);
   }

   public static TaskDialog getDialog(int id) {
      return (TaskDialog)dialogMap.get(id);
   }

   public static TaskPosition getPosition(int id) {
      return (TaskPosition)positionMap.get(id);
   }

   public static TaskData getZJTaskHeader() {
      return zjTaskHeader;
   }

   public static TaskClazzZJ getZJ(int zjId) {
      return (TaskClazzZJ)zjMap.get(zjId);
   }

   public static int getZJSize() {
      return zjMap.size();
   }

   public static Iterator getZJIterator() {
      return zjMap.values().iterator();
   }

   public static TaskClazzRC getRC(Player player) {
      return player == null ? null : getRC(player.getLevel());
   }

   private static TaskClazzRC getRC(int level) {
      return (TaskClazzRC)rcList.get(Math.max(0, Math.min(rcList.size(), level) - 1));
   }

   public static int getXSSize() {
      return xsMap.size();
   }

   public static Iterator getXSIterator() {
      return xsMap.values().iterator();
   }

   public static TaskClazzXS getXS(int id) {
      return (TaskClazzXS)xsMap.get(id);
   }

   public static List getZJTaskList() {
      return zjTaskList;
   }

   public static int getMaxRCLoop() {
      return 10;
   }

   public static Iterator getGHIterator() {
      return ghTaskList.iterator();
   }

   public static Iterator getZXIterator() {
      return zxTaskList.iterator();
   }

   public static Integer getCJMappingTask(int cjId) {
      return (Integer)cjTaskMap.get(cjId);
   }

   public static void addCJMappingTask(int cjId, int taskId) {
      cjTaskMap.put(cjId, taskId);
   }

   public static TaskData getZxTaskHeader() {
      return zxTaskHeader;
   }
}
