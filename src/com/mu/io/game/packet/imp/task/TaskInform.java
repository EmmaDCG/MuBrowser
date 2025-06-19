package com.mu.io.game.packet.imp.task;

import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.TaskRewardItemData;
import com.mu.game.model.task.TaskState;
import com.mu.game.model.task.clazz.TaskClazzRC;
import com.mu.game.model.task.clazz.TaskClazzXS;
import com.mu.game.model.task.target.TaskTarget;
import com.mu.game.model.task.target.TaskTargetRate;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.utils.CommonRegPattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskInform extends WriteOnlyPacket {
   private static Logger logger = LoggerFactory.getLogger(TaskInform.class);

   public TaskInform(int code) {
      super(code);
   }

   public static void sendMsgTaskData(Player player, List taskList, int startIndex, int endIndex, TaskState.TaskClientState defaultState) {
      try {
         endIndex = Math.min(endIndex, taskList.size());
         if (endIndex - startIndex < 1) {
            return;
         }

         TaskInform inform = new TaskInform(40001);
         inform.writeByte(endIndex - startIndex);

         for(int i = startIndex; i < endIndex; ++i) {
            TaskData data = (TaskData)taskList.get(i);
            writePacket_BaseData(player, inform, data, (Task)null);
            writePacket_RateData(player, inform, (Task)null, data, defaultState);
            inform.writeBytes(data.getLinkData());
            writePacket_RewardData(inform, data.getRewardList(player.getProType()));
         }

         player.writePacket(inform);
         inform.destroy();
         inform = null;
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void sendMsgTaskData(Player player, Task... tasks) {
      try {
         if (tasks == null || tasks.length == 0) {
            return;
         }

         List list = new ArrayList();

         for(int i = 0; i < tasks.length; ++i) {
            if (tasks[i] != null) {
               list.add(tasks[i]);
            }
         }

         if (list.isEmpty()) {
            return;
         }

         TaskInform inform = new TaskInform(40001);
         inform.writeByte(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            Task task = (Task)var5.next();
            TaskData data = task.getData();
            writePacket_BaseData(player, inform, data, task);
            writePacket_RateData(player, inform, task, task.getData(), TaskState.TaskClientState.NEW);
            inform.writeBytes(data.getLinkData());
            writePacket_RewardData(inform, task.getRewardList());
         }

         player.writePacket(inform);
         inform.destroy();
         inform = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public static void writePacket_BaseData(Player player, WriteOnlyPacket packet, TaskData data, Task task) {
      try {
         packet.writeInt(data.getId());
         packet.writeUTF(data.getName());
         packet.writeByte(data.getClazz().getValue());
         packet.writeUTF(data.getClazzName());
         packet.writeShort(data.getIcon(player.getProType()));
         packet.writeByte(data.getQuality());
         packet.writeUTF(data.getDescription());
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void writePacket_RateData(Player player, WriteOnlyPacket packet, Task task, TaskData data, TaskState.TaskClientState defaultState) {
      try {
         TaskState.TaskClientState tcs = task == null ? defaultState : TaskState.getClientState(task);
         boolean useAccept = TaskState.TaskClientState.NEW.is(tcs);
         boolean useComplete = TaskState.TaskClientState.RUN_COMPLETED.is(tcs);
         TaskTarget[] targets = task == null ? data.getTargets() : task.getTargets();
         TaskTargetRate[] rates = task == null ? null : task.getRates();
         String traceStr = useAccept ? data.getAcceptTraceStr() : (useComplete ? data.getSubmitTraceStr() : data.getTargetTraceStr());
         Matcher m = CommonRegPattern.PATTERN_TASK_PROFESSION.matcher(traceStr);
         StringBuffer sb = new StringBuffer();

         while(m.find()) {
            if (Integer.parseInt(m.group(1)) == player.getProType()) {
               m.appendReplacement(sb, m.group(2));
            } else {
               m.appendReplacement(sb, "");
            }
         }

         m.appendTail(sb);
         m = CommonRegPattern.PATTERN_TASK_RATE.matcher(sb.toString());
         sb = new StringBuffer();

         while(m.find()) {
            int index = Integer.parseInt(m.group(1));
            int maxRate = index >= targets.length ? 0 : targets[index].getMaxRate();
            int curRate = tcs.is(TaskState.TaskClientState.OVER) ? maxRate : (index >= targets.length ? 0 : (task == null ? targets[index].getRealRate(player) : rates[index].getRealRate()));
            if (rates != null && index >= rates.length) {
               logger.error("TaskData[{}] config error, index[{}] rate not found", task.getId(), index);
            }

            String str = m.group(2);
            str = "c".equalsIgnoreCase(str) ? "" + curRate : ("m".equalsIgnoreCase(str) ? "" + maxRate : "(" + curRate + "/" + maxRate + ")");
            m.appendReplacement(sb, str);
         }

         m.appendTail(sb);
         traceStr = sb.toString();
         packet.writeByte(tcs.getValue());
         packet.writeByte(data.getRunLink(tcs));
         if (task != null) {
            task.setCurTraceStr(traceStr);
         }

         packet.writeUTF(traceStr);
      } catch (Exception var17) {
         var17.printStackTrace();
      }

   }

   public static void sendMsgTaskFly(Player player, int taskId) {
      try {
         TaskInform inform = new TaskInform(40012);
         inform.writeInt(taskId);
         player.writePacket(inform);
         inform.destroy();
         inform = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void writePacket_RewardData(WriteOnlyPacket packet, List rewardList) {
      try {
         if (rewardList == null) {
            packet.writeByte(0);
         } else {
            packet.writeByte(rewardList.size());

            for(int i = 0; i < rewardList.size(); ++i) {
               GetItemStats.writeItem(((TaskRewardItemData)rewardList.get(i)).getItem(), packet);
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendMsgTaskState(Task task) {
      try {
         Player player = task.getOwner();
         TaskInform ti = new TaskInform(40003);
         ti.writeInt(task.getId());
         writePacket_RateData(player, ti, task, task.getData(), TaskState.TaskClientState.NEW);
         player.writePacket(ti);
         ti.destroy();
         ti = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendMsgCurZZTask(Player player, int id) {
      try {
         TaskInform inform = new TaskInform(40023);
         inform.writeInt(id);
         player.writePacket(inform);
         inform.destroy();
         inform = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendMsgRCDialog(Player player, boolean dialogSwitch) {
      try {
         TaskInform inform = new TaskInform(40006);
         inform.writeBoolean(dialogSwitch);
         player.writePacket(inform);
         inform.destroy();
         inform = null;
         if (player.getArrowGuideManager().shouldPushRingTask()) {
            ArrowGuideManager.pushArrow(player, 13, (String)null);
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendMsgCurRCInform(PlayerTaskManager ptm) {
      int curRCStar = ptm.getCurRCStar();
      Task task = ptm.getCurRCTask();
      TaskClazzRC rc = TaskConfigManager.getRC(ptm.getOwner());
      if (task != null && rc != null) {
         try {
            TaskInform inform = new TaskInform(40005);
            inform.writeInt(task.getId());
            inform.writeUTF(task.getData().getDescription());
            List rewardList = task.getRewardList(rc.getRewardStarFactor(curRCStar));
            long exp = 0L;
            int money = 0;

            for(int i = 0; i < rewardList.size(); ++i) {
               TaskRewardItemData tri = (TaskRewardItemData)rewardList.get(i);
               if (tri.getModelID() == 2015) {
                  money = tri.getCount();
               }

               if (tri.getModelID() == 2018) {
                  exp = (long)tri.getCount();
               }
            }

            inform.writeDouble((double)exp);
            inform.writeInt(money);
            TaskRewardItemData.destroyList(rewardList);
            rewardList = null;
            inform.writeByte(curRCStar);
            inform.writeByte(ptm.getCurRCRemain());
            inform.writeBoolean(ptm.getCurRCRemain() <= 0 && task.is(TaskState.NEW));
            if (ptm.getCurRCRemain() <= 0 && task.is(TaskState.NEW)) {
               inform.writeInt(rc.getBuyCountExpend());
            }

            inform.writeInt(rc.getRefreshStarExpend());
            ptm.getOwner().writePacket(inform);
            inform.destroy();
            inform = null;
         } catch (Exception var11) {
            var11.printStackTrace();
         }

      } else {
         logger.error("not found task or TaskClazzRC");
      }
   }

   public static void sendMsgCurXSConfig(Player player, Map curXSTaskMap, ConcurrentHashMap curXSCountMap) {
      try {
         TaskInform inform = new TaskInform(40015);
         inform.writeByte(TaskConfigManager.getXSSize());
         Iterator it = TaskConfigManager.getXSIterator();

         while(it.hasNext()) {
            TaskClazzXS xs = (TaskClazzXS)it.next();
            Integer count = (Integer)curXSCountMap.get(xs);
            count = count == null ? 0 : count.intValue();
            inform.writeInt(xs.getId());
            inform.writeShort(xs.getSort());
            inform.writeByte(xs.getColor());
            inform.writeShort(xs.getLevel());
            int maxCount = xs.getOneDayCountLimit();
            inform.writeShort(Math.max(0, maxCount - count.intValue()));
            inform.writeUTF(xs.getName());
            inform.writeShort(xs.getIcon());
            inform.writeShort(xs.getMapId());
            inform.writeShort(xs.getPositionIcon());
            inform.writeUTF(xs.getDescription());
            GetItemStats.writeItem(xs.getOpenItem().getItem(), inform);
            inform.writeBoolean(curXSTaskMap.get(xs) != null);
            inform.writeByte(xs.getSize());
            writePacket_RewardData(inform, xs.getShowRewardList());
            inform.writeByte(xs.getHcPanelId1());
            inform.writeByte(xs.getHcPanelId2());
            inform.writeShort(xs.getHcPanelId3());
         }

         player.writePacket(inform);
         inform.destroy();
         inform = null;
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void sendMsgCurXSInform(Player player, TaskClazzXS xs, Task task) {
      try {
         TaskInform inform = new TaskInform(40017);
         inform.writeInt(xs.getId());
         if (task != null) {
            inform.writeByte(task.getClazzIndex() + 1);
            Iterator it = xs.getIterator();

            while(it.hasNext()) {
               TaskData data = (TaskData)it.next();
               inform.writeInt(data.getId());
               if (data == task.getData()) {
                  break;
               }
            }
         } else {
            inform.writeByte(0);
         }

         player.writePacket(inform);
         inform.destroy();
         inform = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void sendMsgCurGHInform(Player player, Collection taskList) {
      try {
         TaskInform inform = new TaskInform(40019);
         inform.writeByte(taskList.size());
         Iterator it = taskList.iterator();

         while(it.hasNext()) {
            Integer id = (Integer)it.next();
            inform.writeInt(id.intValue());
         }

         player.writePacket(inform);
         inform.destroy();
         inform = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendMsgTaskEffect(Player player, int taskId, boolean isChapter) {
      try {
         TaskInform inform = new TaskInform(40022);
         inform.writeInt(taskId);
         player.writePacket(inform);
         inform.destroy();
         inform = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendMsgTaskContinue(Player player, boolean isContinue) {
      try {
         TaskInform inform = new TaskInform(40004);
         inform.writeBoolean(isContinue);
         player.writePacket(inform);
         inform.destroy();
         inform = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendMsgClear(Player player, Task... tasks) {
      try {
         if (tasks == null || tasks.length == 0) {
            return;
         }

         List list = new ArrayList();

         for(int i = 0; i < tasks.length; ++i) {
            if (tasks[i] != null) {
               list.add(tasks[i].getData());
            }
         }

         sendMsgClear(player, (List)list);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendMsgClear(Player player, List list) {
      try {
         if (list == null || list.isEmpty()) {
            return;
         }

         TaskInform ti = new TaskInform(40002);
         ti.writeByte(list.size());
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            TaskData data = (TaskData)var4.next();
            ti.writeInt(data.getId());
         }

         player.writePacket(ti);
         ti.destroy();
         ti = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendMsgTaskTraceList(Player player, Task curZJTask, Task curRCTask, Task curZXTask, Task curTXTask, ConcurrentHashMap curXSTaskMap, Task curZZTask, ConcurrentHashMap currentTaskMap) {
      try {
         List list = new ArrayList();
         if (curZJTask != null && curZJTask.getClazzNext() != null) {
            list.add(curZJTask.getId());
         }

         if (curRCTask != null && !curRCTask.is(TaskState.OVER)) {
            list.add(curRCTask.getId());
         }

         Iterator it = curXSTaskMap.values().iterator();

         while(it.hasNext()) {
            list.add(((Task)it.next()).getId());
         }

         if (curZZTask != null) {
            list.add(curZZTask.getId());
         }

         if (curZXTask != null && curZXTask.is(TaskState.RUN)) {
            list.add(curZXTask.getId());
         }

         if (curTXTask != null && curTXTask.is(TaskState.RUN)) {
            list.add(curTXTask.getId());
         }

         TaskInform packet = new TaskInform(40026);
         packet.writeByte(list.size());
         Iterator var11 = list.iterator();

         while(var11.hasNext()) {
            Integer id = (Integer)var11.next();
            packet.writeInt(id.intValue());
         }

         list.clear();
         list = null;
         player.writePacket(packet);
         packet.destroy();
         it = null;
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }

   public static void sendMsgZXTip(Player player, Task curZJTask) {
      try {
         TaskInform packet = new TaskInform(40027);
         boolean visible = curZJTask != null && !curZJTask.is(TaskState.OVER);
         packet.writeBoolean(visible);
         if (visible) {
            packet.writeInt(curZJTask.getId());
            packet.writeBoolean(curZJTask.isComplete());
         }

         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendMsgZXDetail(Player player, Task curZJTask) {
      try {
         TaskInform packet = new TaskInform(40028);
         if (curZJTask == null || curZJTask.is(TaskState.OVER)) {
            return;
         }

         TaskData data = curZJTask.getData();
         packet.writeInt(data.getId());
         packet.writeShort(data.getIcon(player.getProType()));
         packet.writeUTF(data.getDescription());
         packet.writeUTF(curZJTask.getCurTraceStr());
         packet.writeBytes(data.getLinkData());
         GetItemStats.writeItem(((TaskRewardItemData)curZJTask.getRewardList().get(0)).getItem(), packet);
         packet.writeByte(curZJTask.is(TaskState.OVER) ? 2 : (curZJTask.isComplete() ? 1 : 0));
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
