package com.mu.game.model.task;

import com.mu.db.log.IngotChangeType;
import com.mu.executor.Executor;
import com.mu.game.model.dialog.DialogOptionSee;
import com.mu.game.model.dialog.options.DialogOptionTask;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.guide.TaskActionManager;
import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.rewardhall.vitality.VitalityTaskType;
import com.mu.game.model.task.clazz.TaskClazz;
import com.mu.game.model.task.clazz.TaskClazzRC;
import com.mu.game.model.task.clazz.TaskClazzXS;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.task.target.TaskTarget;
import com.mu.game.model.task.target.TaskTargetRate;
import com.mu.game.model.task.target.impl.TaskTargetCount;
import com.mu.game.model.task.target.impl.TaskTargetMoreSpecify;
import com.mu.game.model.task.target.impl.TaskTargetSpecify;
import com.mu.game.model.task.target.impl.TaskTargetValue;
import com.mu.game.model.transfer.Transfer;
import com.mu.game.model.transfer.TransferConfigManager;
import com.mu.game.model.transfer.TransferStep;
import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.npc.UpdateNpcHeadIcon;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.io.game.packet.imp.task.InitTask;
import com.mu.io.game.packet.imp.task.OpenXSUseItem;
import com.mu.io.game.packet.imp.task.RefreshRCStar;
import com.mu.io.game.packet.imp.task.SubmitRCTask;
import com.mu.io.game.packet.imp.task.TaskInform;
import com.mu.io.game.packet.imp.task.TaskPushNext;
import com.mu.utils.Rnd;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerTaskManager {
   private static Logger logger = LoggerFactory.getLogger(PlayerTaskManager.class);
   private Player owner;
   private Task curZJTask;
   private Task curRCTask;
   private Task curZXTask;
   private Task curTXTask;
   private ConcurrentHashMap curXSTaskMap;
   private ConcurrentHashMap curXSCountMap;
   private Task curZZTask;
   private ConcurrentHashMap currentTaskMap = Tools.newConcurrentHashMap4();
   private ConcurrentHashMap historyTaskMap = Tools.newConcurrentHashMap4();
   private CopyOnWriteArraySet canAcceptGHSet = new CopyOnWriteArraySet();
   private List ghTasklist = new ArrayList();
   private int curRCStar;
   private int curRCBuy;
   private int curRCRemain = 0;
   private HashMap check_count = new HashMap();
   private HashMap check_value = new HashMap();
   private HashMap check_specify = new HashMap();
   private HashMap check_specifyMore = new HashMap();
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$task$clazz$TaskClazz;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$task$target$TargetType;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$dialog$DialogOptionSee;

   public PlayerTaskManager(Player owner, long logoutTime) {
      this.owner = owner;
      this.curXSTaskMap = Tools.newConcurrentHashMap4();
      this.curXSCountMap = Tools.newConcurrentHashMap4();
      this.curRCRemain = TaskConfigManager.getMaxRCLoop();
      this.initChecker();
   }

   public void initTask(InitTask packet) {
      ArrayList taskList = new ArrayList();

      Task task;
      try {
         boolean has = packet.readBoolean();
         if (has) {
            this.curRCStar = packet.readInt();
            this.curRCRemain = packet.readInt();
            this.curRCBuy = packet.readInt();
         }

         int xsSize = packet.readByte();

         for(int i = 0; i < xsSize; ++i) {
            int xsId = packet.readInt();
            int count = packet.readInt();
            TaskClazzXS xs = TaskConfigManager.getXS(xsId);
            if (xs != null) {
               this.curXSCountMap.put(xs, count);
            }
         }

         while(packet.remaining() > 0) {
            task = Task.createForDB(packet.getPlayer(), packet);
            if (task != null) {
               taskList.add(task);
            }
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      this.curRCRemain = Math.max(0, this.curRCRemain);
      Transfer transfer = TransferConfigManager.getWillTransfer(this.owner.getProType(), this.owner.getProLevel());

      try {
         Iterator it = taskList.iterator();

         while(it.hasNext()) {
            task = (Task)it.next();
            boolean valid = true;
            switch($SWITCH_TABLE$com$mu$game$model$task$clazz$TaskClazz()[task.getClazz().ordinal()]) {
            case 1:
               this.curZJTask = task;
               break;
            case 2:
               valid = FunctionOpenManager.isOpen(this.owner, 4);
               if (valid) {
                  this.curRCTask = task;
               }
               break;
            case 3:
               valid = FunctionOpenManager.isOpen(this.owner, 6);
               if (task.is(TaskState.RUN) && valid) {
                  this.curXSTaskMap.put((TaskClazzXS)task.getClazzData(), task);
               }
               break;
            case 4:
               valid = FunctionOpenManager.isOpen(this.owner, 7);
               break;
            case 5:
               if (transfer != null && this.owner.getLevel() >= transfer.getLevel() && transfer.getTaskList().contains(task.getData())) {
                  this.curZZTask = task;
               }
               break;
            case 6:
               this.curZXTask = task;
               break;
            case 7:
               this.curTXTask = task;
            }

            if (valid) {
               this.historyTaskMap.put(task.getId(), task);
               if (task.is(TaskState.RUN)) {
                  this.currentTaskMap.put(task.getId(), task);
               }
            }
         }

         if (this.owner.isNeedZeroClear()) {
            this.curRCBuy = 0;
            this.curRCRemain = TaskConfigManager.getMaxRCLoop();
            if (!this.curXSCountMap.isEmpty()) {
               this.curXSCountMap.clear();
               dbClearXSCount(this.owner);
            }
         }

         if (this.owner.getInitType() == 3 || this.owner.getInitType() == 2) {
            this.onEventLogin(true);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   private void initChecker() {
      int i;
      for(i = 0; i < TargetType.CountType.values().length; ++i) {
         this.check_count.put(TargetType.CountType.values()[i], new CopyOnWriteArraySet());
      }

      for(i = 0; i < TargetType.ValueType.values().length; ++i) {
         this.check_value.put(TargetType.ValueType.values()[i], new CopyOnWriteArraySet());
      }

      for(i = 0; i < TargetType.SpecifyType.values().length; ++i) {
         this.check_specify.put(TargetType.SpecifyType.values()[i], new CopyOnWriteArraySet());
      }

      for(i = 0; i < TargetType.MoreSpecifyType.values().length; ++i) {
         this.check_specifyMore.put(TargetType.MoreSpecifyType.values()[i], new CopyOnWriteArraySet());
      }

   }

   private void registerChecker(Task task) {
      if (task != null && task.is(TaskState.RUN)) {
         try {
            TaskTarget[] targets = task.getTargets();
            TaskTargetRate[] rates = task.getRates();

            for(int i = 0; i < targets.length; ++i) {
               switch($SWITCH_TABLE$com$mu$game$model$task$target$TargetType()[targets[i].getType().ordinal()]) {
               case 1:
                  TaskTargetCount tc = (TaskTargetCount)targets[i];
                  ((CopyOnWriteArraySet)this.check_count.get(tc.getCountType())).add(rates[i]);
                  break;
               case 2:
                  TaskTargetSpecify ts = (TaskTargetSpecify)targets[i];
                  ((CopyOnWriteArraySet)this.check_specify.get(ts.getSpecifyType())).add(rates[i]);
                  break;
               case 3:
                  TaskTargetValue tv = (TaskTargetValue)targets[i];
                  ((CopyOnWriteArraySet)this.check_value.get(tv.getValueType())).add(rates[i]);
               case 4:
               default:
                  break;
               case 5:
                  TaskTargetMoreSpecify tms = (TaskTargetMoreSpecify)targets[i];
                  ((CopyOnWriteArraySet)this.check_specifyMore.get(tms.getSpecifyType())).add(rates[i]);
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }

      }
   }

   private void unregisterChecker(Task task) {
      if (task != null && !task.is(TaskState.RUN)) {
         try {
            TaskTarget[] targets = task.getTargets();
            TaskTargetRate[] rates = task.getRates();

            for(int i = 0; i < targets.length; ++i) {
               switch($SWITCH_TABLE$com$mu$game$model$task$target$TargetType()[targets[i].getType().ordinal()]) {
               case 1:
                  TaskTargetCount tc = (TaskTargetCount)targets[i];
                  ((CopyOnWriteArraySet)this.check_count.get(tc.getCountType())).remove(rates[i]);
                  break;
               case 2:
                  TaskTargetSpecify ts = (TaskTargetSpecify)targets[i];
                  ((CopyOnWriteArraySet)this.check_specify.get(ts.getSpecifyType())).remove(rates[i]);
                  break;
               case 3:
                  TaskTargetValue tv = (TaskTargetValue)targets[i];
                  ((CopyOnWriteArraySet)this.check_value.get(tv.getValueType())).remove(rates[i]);
               case 4:
               default:
                  break;
               case 5:
                  TaskTargetMoreSpecify tms = (TaskTargetMoreSpecify)targets[i];
                  ((CopyOnWriteArraySet)this.check_specifyMore.get(tms.getSpecifyType())).remove(rates[i]);
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }

      }
   }

   public Player getOwner() {
      return this.owner;
   }

   public ConcurrentHashMap getCurrentTaskMap() {
      return this.currentTaskMap;
   }

   public void onEventLogin(boolean skipServer) {
      boolean informChange = false;
      Iterator it = this.currentTaskMap.values().iterator();

      while(true) {
         while(it.hasNext()) {
            Task task = (Task)it.next();
            if (task.is(TaskClazz.ZJ) && this.curZJTask != task || task.is(TaskClazz.RC) && this.curRCTask != task || task.is(TaskClazz.ZZ) && this.curZZTask != task) {
               it.remove();
               this.historyTaskMap.remove(task.getId());
               dbDelete(task);
            } else {
               this.registerChecker(task);
            }
         }

         if (this.curZJTask != null && this.curZJTask.is(TaskState.RUN)) {
            if (!skipServer) {
               boolean include = this.curZJTask.getClazzNext() != null;
               if (include) {
                  TaskInform.sendMsgTaskData(this.owner, this.curZJTask);
               }
            }
         } else {
            this.accept(TaskConfigManager.getZJTaskHeader().getId(), true);
         }

         TaskInform.sendMsgTaskContinue(this.getOwner(), this.curZJTask.getClazzIndex() <= TaskConfigManager.TASK_TRACE_ZJ_INDEX);
         if (FunctionOpenManager.isOpen(this.owner, 4)) {
            if (this.curRCTask == null || this.curRCBuy < 2 && !this.curRCTask.is(TaskState.RUN)) {
               this.randomRCTask();
            } else if (!skipServer) {
               TaskInform.sendMsgTaskData(this.owner, this.curRCTask);
               TaskInform.sendMsgCurRCInform(this);
            }
         }

         TaskInform.sendMsgCurXSConfig(this.owner, this.curXSTaskMap, this.curXSCountMap);
         if (!skipServer) {
            it = this.curXSTaskMap.entrySet().iterator();

            while(it.hasNext()) {
               Entry entry = (Entry)it.next();
               TaskClazzXS xs = (TaskClazzXS)entry.getKey();
               Task task = (Task)entry.getValue();
               TaskInform.sendMsgTaskData(this.owner, xs.getTaskList(), 0, task.getClazzIndex(), TaskState.TaskClientState.OVER);
               TaskInform.sendMsgTaskData(this.owner, task);
               TaskInform.sendMsgCurXSInform(this.owner, xs, task);
            }
         }

         Iterator var11 = this.historyTaskMap.values().iterator();

         while(var11.hasNext()) {
            Task task = (Task)var11.next();
            if (task.is(TaskClazz.GH)) {
               if (!skipServer) {
                  TaskInform.sendMsgTaskData(this.owner, task);
               }

               this.ghTasklist.add(task.getId());
            }
         }

         if (!skipServer) {
            List list = this.getNewCanAcceptGHTaskList();
            TaskInform.sendMsgTaskData(this.owner, list, 0, list.size(), TaskState.TaskClientState.NEW);
            TaskInform.sendMsgCurGHInform(this.owner, this.ghTasklist);
         }

         if (!skipServer) {
            this.checkCanAcceptZXTask();
            TaskInform.sendMsgTaskData(this.owner, this.curZXTask);
            TaskInform.sendMsgZXTip(this.owner, this.curZXTask);
         }

         TaskInform.sendMsgTaskData(this.owner, this.curTXTask);
         Transfer transfer = TransferConfigManager.getWillTransfer(this.owner.getProType(), this.owner.getProLevel());
         if (transfer != null) {
            if (this.curZZTask != null) {
               if (!skipServer) {
                  TaskInform.sendMsgTaskData(this.owner, transfer.getTaskList(), 0, this.curZZTask.getClazzIndex(), TaskState.TaskClientState.OVER);
                  TaskInform.sendMsgTaskData(this.owner, this.curZZTask);
                  TaskInform.sendMsgTaskData(this.owner, transfer.getTaskList(), this.curZZTask.getClazzIndex() + 1, transfer.getTaskList().size(), TaskState.TaskClientState.NEW);
                  TaskInform.sendMsgCurZZTask(this.owner, this.curZZTask.getId());
               }

               if (this.curZZTask.isComplete() && this.curZZTask.isAutoSubmit()) {
                  this.submit(this.curZZTask.getId(), true);
               }
            } else {
               if (!skipServer) {
                  TaskInform.sendMsgTaskData(this.owner, transfer.getTaskList(), 0, transfer.getTaskList().size(), TaskState.TaskClientState.NEW);
                  TaskInform.sendMsgCurZZTask(this.owner, 0);
               }

               if (this.owner.getLevel() >= transfer.getLevel()) {
                  this.accept(transfer.getTaskHead().getId(), true);
               }
            }
         }

         TaskInform.sendMsgTaskTraceList(this.owner, this.curZJTask, this.curRCTask, this.curZXTask, this.curTXTask, this.curXSTaskMap, this.curZZTask, this.currentTaskMap);
         if (informChange) {
            dbReplaceInform(this);
         }

         return;
      }
   }

   public void onEventSkipDay() {
      try {
         this.curRCBuy = 0;
         this.curRCRemain = TaskConfigManager.getMaxRCLoop();
         this.randomRCTask();
         if (!this.curXSCountMap.isEmpty()) {
            Iterator it = this.curXSCountMap.keySet().iterator();

            while(true) {
               if (!it.hasNext()) {
                  this.curXSCountMap.clear();
                  dbClearXSCount(this.owner);
                  break;
               }

               TaskClazzXS xs = (TaskClazzXS)it.next();
               int maxCount = xs.getOneDayCountLimit();
               OpenXSUseItem.open(this.getOwner(), xs.getId(), this.curXSTaskMap.get(xs) != null, maxCount);
            }
         }

         dbReplaceInform(this);
         TaskInform.sendMsgTaskTraceList(this.owner, this.curZJTask, this.curRCTask, this.curZXTask, this.curTXTask, this.curXSTaskMap, this.curZZTask, this.currentTaskMap);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public boolean onEventDialogSee(Npc npc, DialogOptionTask dot) {
      TaskData data = dot.getData();
      Task task = (Task)this.currentTaskMap.get(data.getId());
      switch($SWITCH_TABLE$com$mu$game$model$dialog$DialogOptionSee()[dot.getSee().ordinal()]) {
      case 3:
         return this.canAccept(data);
      case 4:
         return task != null && (task.is(TaskClazz.RC) && task.is(TaskState.RUN) && !task.isComplete() || !task.getRates()[dot.getTarget().getIndex()].ok());
      case 5:
         if (task != null && task.isComplete()) {
            return true;
         }

         return false;
      default:
         return false;
      }
   }

   public void onEventDialogEnd(int taskId, long npcId) {
      Npc npc = this.getOwner().getMap().getNpc(npcId);
      TaskData data = TaskConfigManager.getData(taskId);
      if (npc != null) {
         if (data != null) {
            DialogOptionTask dot = npc.getCanSeeTaskOption(data, this.getOwner());
            if (dot != null) {
               switch($SWITCH_TABLE$com$mu$game$model$dialog$DialogOptionSee()[dot.getSee().ordinal()]) {
               case 3:
                  this.accept(taskId, false);
                  break;
               case 4:
                  Task task = (Task)this.currentTaskMap.get(taskId);
                  if (task != null) {
                     dot.getTarget().checkRate(task, npcId);
                  }
                  break;
               case 5:
                  this.submit(taskId, false);
               }

            }
         }
      }
   }

   public void onEventCheckCount(TargetType.CountType ct) {
      CopyOnWriteArraySet set = (CopyOnWriteArraySet)this.check_count.get(ct);
      Iterator it = set.iterator();

      while(it.hasNext()) {
         try {
            TaskTargetRate rate = (TaskTargetRate)it.next();
            rate.getTarget().checkRate(rate.getTask());
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }

   public void onEventCheckValue(TargetType.ValueType vt) {
      if (vt == TargetType.ValueType.RoleLevel) {
         if (this.curRCTask == null) {
            this.randomRCTask();
         }

         List list = this.getNewCanAcceptGHTaskList();
         TaskInform.sendMsgTaskData(this.owner, list, 0, list.size(), TaskState.TaskClientState.NEW);
         this.checkCanAcceptZXTask();
      }

      CopyOnWriteArraySet set = (CopyOnWriteArraySet)this.check_value.get(vt);
      Iterator it = set.iterator();

      while(it.hasNext()) {
         try {
            TaskTargetRate rate = (TaskTargetRate)it.next();
            rate.getTarget().checkRate(rate.getTask());
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }

   public void onEventCheckSpecify(TargetType.SpecifyType st, Object... specify) {
      CopyOnWriteArraySet set = (CopyOnWriteArraySet)this.check_specify.get(st);
      Iterator it = set.iterator();

      while(it.hasNext()) {
         try {
            TaskTargetRate rate = (TaskTargetRate)it.next();
            rate.getTarget().checkRate(rate.getTask(), specify);
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

   }

   public void onEventCheckMoreSpecify(TargetType.MoreSpecifyType st) {
      CopyOnWriteArraySet set = (CopyOnWriteArraySet)this.check_specifyMore.get(st);
      Iterator it = set.iterator();

      while(it.hasNext()) {
         try {
            TaskTargetRate rate = (TaskTargetRate)it.next();
            rate.getTarget().checkRate(rate.getTask());
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }

   private boolean canAccept(TaskData data) {
      if (data != null && this.currentTaskMap.get(data.getId()) == null) {
         switch($SWITCH_TABLE$com$mu$game$model$task$clazz$TaskClazz()[data.getClazz().ordinal()]) {
         case 2:
            return (this.curRCRemain > 0 || this.curRCBuy < 2) && this.curRCTask != null && this.curRCTask.is(data) && this.curRCTask.is(TaskState.NEW);
         case 3:
         case 5:
         default:
            return false;
         case 4:
            if (data.getLevel() > this.owner.getLevel() || !this.isTaskOver(data.getBeforeTask()) || this.historyTaskMap.get(data.getId()) != null && !((Task)this.historyTaskMap.get(data.getId())).is(TaskState.NEW)) {
               return false;
            }

            return true;
         case 6:
            if (data.getLevel() > this.owner.getLevel() || !this.isTaskOver(data.getBeforeTask()) || this.historyTaskMap.get(data.getId()) != null && !((Task)this.historyTaskMap.get(data.getId())).is(TaskState.NEW)) {
               return false;
            } else {
               return true;
            }
         }
      } else {
         return false;
      }
   }

   public List getNewCanAcceptGHTaskList() {
      List list = new ArrayList();
      Iterator iterator = TaskConfigManager.getGHIterator();

      while(iterator.hasNext()) {
         TaskData data = (TaskData)iterator.next();
         if (!this.canAcceptGHSet.contains(data) && this.canAccept(data)) {
            this.ghTasklist.add(data.getId());
            if (data.isAutoAccept()) {
               this.accept(data.getId(), false);
            } else {
               list.add(data);
               this.canAcceptGHSet.add(data);
            }
         }
      }

      return list;
   }

   public void checkCanAcceptZXTask() {
      TaskData data = this.curZXTask == null ? TaskConfigManager.getZxTaskHeader() : (this.curZXTask.is(TaskState.OVER) ? this.curZXTask.getClazzNext() : null);
      if (this.canAccept(data)) {
         this.accept(data.getId(), false);
         TaskInform.sendMsgZXTip(this.owner, this.curZXTask);
      }

   }

   public Task accept(int taskId, boolean autoAccept) {
      TaskData data = TaskConfigManager.getData(taskId);
      if (data == null) {
         SystemMessage.writeMessage(this.owner, 6001);
         logger.error("task is null " + taskId);
         return null;
      } else if (this.currentTaskMap.get(data.getId()) != null) {
         logger.error("task is accpet " + taskId);
         return (Task)this.currentTaskMap.get(data.getId());
      } else if (!autoAccept && data.is(TaskClazz.RC) && this.curRCRemain < 1) {
         SystemMessage.writeMessage(this.owner, 6006);
         return null;
      } else if (!autoAccept && !this.canAccept(data)) {
         logger.error("not can accept " + taskId);
         return null;
      } else {
         Task task = Task.createForNew(this.owner, data);
         task.accpet();
         this.historyTaskMap.put(task.getId(), task);
         this.currentTaskMap.put(task.getId(), task);
         this.registerChecker(task);
         switch($SWITCH_TABLE$com$mu$game$model$task$clazz$TaskClazz()[task.getClazz().ordinal()]) {
         case 1:
            this.curZJTask = task;
            boolean include = this.curZJTask.getClazzNext() != null;
            if (include) {
               TaskInform.sendMsgTaskData(this.owner, task);
               if (this.owner.isNeddAutoTask()) {
                  TaskPushNext.pushNext(this.owner, true);
               }
            }

            dbInsert(task, true);
            break;
         case 2:
            this.curRCTask = task;
            dbInsert(task, true);
            TaskInform.sendMsgTaskState(task);
            TaskInform.sendMsgTaskFly(this.owner, taskId);
            break;
         case 3:
            this.curXSTaskMap.put((TaskClazzXS)task.getClazzData(), task);
            TaskInform.sendMsgTaskData(this.owner, task);
            TaskInform.sendMsgCurXSInform(this.owner, (TaskClazzXS)task.getClazzData(), task);
            dbInsert(task, false);
            break;
         case 4:
            this.canAcceptGHSet.remove(data);
            TaskInform.sendMsgTaskData(this.owner, task);
            TaskInform.sendMsgCurGHInform(this.owner, this.ghTasklist);
            dbInsert(task, false);
            break;
         case 5:
            this.curZZTask = task;
            TaskInform.sendMsgTaskState(task);
            dbInsert(task, true);
            TaskInform.sendMsgCurZZTask(this.owner, task.getId());
            break;
         case 6:
            this.curZXTask = task;
            TaskInform.sendMsgTaskData(this.owner, task);
            dbInsert(task, true);
            break;
         case 7:
            this.curTXTask = task;
            TaskInform.sendMsgTaskData(this.owner, task);
            dbInsert(task, true);
         }

         UpdateNpcHeadIcon.updatePlayerSeeNpcHeadIcon(this.owner, data.getAcceptNpc());
         TaskActionManager.doAcceptAction(this.owner, task.getId());
         if (!task.isSkip() && !task.isDisabled()) {
            if (task.isComplete()) {
               task.onRateChangeCheckComplete();
            }
         } else {
            task.forceComplete();
         }

         TaskInform.sendMsgTaskTraceList(this.owner, this.curZJTask, this.curRCTask, this.curZXTask, this.curTXTask, this.curXSTaskMap, this.curZZTask, this.currentTaskMap);
         return task;
      }
   }

   private boolean doSubmit(Task task) {
      this.unregisterChecker(task);
      task.submit();
      this.currentTaskMap.remove(task.getId());
      this.onEventCheckSpecify(TargetType.SpecifyType.SubmitTask, task.getId());
      TaskInform.sendMsgTaskEffect(this.owner, task.getId(), task.is(TaskClazz.ZJ));
      switch($SWITCH_TABLE$com$mu$game$model$task$clazz$TaskClazz()[task.getClazz().ordinal()]) {
      case 1:
         this.historyTaskMap.remove(task.getId());
         TaskInform.sendMsgTaskState(task);
         this.accept(task.getClazzNextId(), true);
         TaskInform.sendMsgTaskContinue(this.getOwner(), this.curZJTask.getClazzIndex() <= TaskConfigManager.TASK_TRACE_ZJ_INDEX);
         break;
      case 2:
         --this.curRCRemain;
         TaskInform.sendMsgTaskState(task);
         if (this.curRCRemain <= 0 && this.curRCBuy >= 2) {
            TaskInform.sendMsgRCDialog(this.owner, false);
            TaskInform.sendMsgCurRCInform(this);
            dbUpdate(this.curRCTask);
         } else {
            this.historyTaskMap.remove(task.getId());
            TaskInform.sendMsgClear(this.owner, task);
            this.randomRCTask();
         }

         this.owner.getTaskManager().onEventCheckCount(TargetType.CountType.Task_RiChang);
         dbReplaceInform(this);
         this.owner.getVitalityManager().onTaskEvent(VitalityTaskType.RWJS, 1, 1);
         break;
      case 3:
         this.historyTaskMap.remove(task.getId());
         TaskInform.sendMsgTaskState(task);
         this.accept(task.getClazzNextId(), true);
         if (task.getClazzNext() == null) {
            TaskClazzXS xs = (TaskClazzXS)task.getClazzData();
            this.curXSTaskMap.remove(xs);
            TaskInform.sendMsgCurXSInform(this.owner, (TaskClazzXS)task.getClazzData(), (Task)null);
            TaskInform.sendMsgClear(this.owner, xs.getTaskList());
            int count = this.curXSCountMap.get(xs) != null ? ((Integer)this.curXSCountMap.get(xs)).intValue() : 0;
            int maxCount = xs.getOneDayCountLimit();
            OpenXSUseItem.open(this.owner, xs.getId(), false, Math.max(0, maxCount - count));
            this.owner.getVitalityManager().onTaskEvent(VitalityTaskType.RWJS, 2, 1);
            this.owner.getTaskManager().onEventCheckCount(TargetType.CountType.Task_XuanShang);
         }

         dbDelete(task);
         break;
      case 4:
         TaskInform.sendMsgTaskState(task);
         dbUpdate(task);
         break;
      case 5:
         this.historyTaskMap.remove(task.getId());
         TaskInform.sendMsgTaskState(task);
         Transfer transfer = TransferConfigManager.getWillTransfer(this.owner.getProType(), this.owner.getProLevel());
         if (transfer != null && this.owner.getLevel() >= transfer.getLevel()) {
            Iterator it = transfer.getStepList().iterator();

            while(it.hasNext()) {
               TransferStep step = (TransferStep)it.next();
               step.complete(task);
            }
         }

         if (this.curZZTask.getClazzNext() == null) {
            this.curZZTask = null;
            TaskInform.sendMsgCurZZTask(this.owner, 0);
            dbDelete(task);
            this.owner.transfer();
         } else {
            this.accept(task.getClazzNextId(), true);
         }
         break;
      case 6:
         TaskInform.sendMsgTaskState(task);
         TaskInform.sendMsgZXTip(this.owner, this.curZXTask);
         dbUpdate(task);
         break;
      case 7:
         TaskInform.sendMsgTaskState(task);
         dbUpdate(task);
         this.getOwner().getTanXianManager().complete();
      }

      UpdateNpcHeadIcon.updatePlayerSeeNpcHeadIcon(this.owner, task.getData().getSubmitNpc());
      List newTaskList = this.getNewCanAcceptGHTaskList();
      TaskInform.sendMsgTaskData(this.owner, newTaskList, 0, newTaskList.size(), TaskState.TaskClientState.NEW);
      this.checkCanAcceptZXTask();
      this.onEventCheckSpecify(TargetType.SpecifyType.SubmitTask, task.getId());
      TaskInform.sendMsgTaskTraceList(this.owner, this.curZJTask, this.curRCTask, this.curZXTask, this.curTXTask, this.curXSTaskMap, this.curZZTask, this.currentTaskMap);
      TaskActionManager.doSubmitAction(this.owner, task.getId());
      return true;
   }

   public void randomRCTask() {
      TaskClazzRC rc = TaskConfigManager.getRC(this.owner);
      TaskData data = rc.randomTask();
      this.curRCTask = Task.createForNew(this.owner, data);
      if (this.curRCTask != null) {
         ArrowGuideManager am = this.owner.getArrowGuideManager();
         this.curRCStar = Rnd.get(1, 10);
         if (am.getGuideTimes(13) < ArrowGuideManager.getArrowInfo(13).getTimes()) {
            this.curRCStar = Rnd.get(1, 9);
         } else if (am.getGuideTimes(13) == ArrowGuideManager.getArrowInfo(13).getTimes()) {
            this.curRCStar = 10;
         }

         TaskInform.sendMsgTaskData(this.owner, this.curRCTask);
         dbInsert(this.curRCTask, true);
         dbReplaceInform(this);
         TaskInform.sendMsgCurRCInform(this);
      }

   }

   public boolean submit(int taskId, boolean ignore) {
      TaskData data = TaskConfigManager.getData(taskId);
      Task task = (Task)this.currentTaskMap.get(taskId);
      if (data != null && task != null) {
         if (!task.isComplete()) {
            return false;
         } else if (!task.reward(1.0D) && !ignore) {
            SystemMessage.writeMessage(this.owner, 6002);
            return false;
         } else {
            return this.doSubmit(task);
         }
      } else {
         SystemMessage.writeMessage(this.owner, 6001);
         return false;
      }
   }

   public boolean submitRC(boolean more) {
      Task task = this.curRCTask;
      if (task == null) {
         return false;
      } else if (!task.isComplete()) {
         return false;
      } else {
         int moreFactor = more ? 5 : 1;
         TaskClazzRC rc = TaskConfigManager.getRC(this.owner);
         if (moreFactor == 5 && 1 != PlayerManager.reduceIngot(this.owner, rc.getSubmitFiveMultipleExpend(), IngotChangeType.TASK, "2," + task.getData().getId())) {
            SystemMessage.writeMessage(this.owner, 1015);
            return false;
         } else {
            List rewardList = task.getRewardList((double)moreFactor * rc.getRewardStarFactor(this.curRCStar));
            if (!task.reward(rewardList)) {
               SystemMessage.writeMessage(this.owner, 6002);
               TaskRewardItemData.destroyList(rewardList);
               rewardList = null;
               return false;
            } else {
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

               TaskRewardItemData.destroyList(rewardList);
               rewardList = null;
               boolean result = this.doSubmit(task);
               if (result) {
                  SubmitRCTask.sendMsgResult(this.owner, moreFactor, exp, money);
               }

               return result;
            }
         }
      }
   }

   public boolean rcBuy() {
      if (this.curRCBuy >= TaskConfigManager.getMaxRCLoop()) {
         return false;
      } else {
         TaskClazzRC rc = TaskConfigManager.getRC(this.owner);
         if (1 != PlayerManager.reduceIngot(this.owner, rc.getBuyCountExpend(), IngotChangeType.TASK, "1," + rc.getLevel())) {
            SystemMessage.writeMessage(this.owner, 1015);
            return false;
         } else {
            ++this.curRCBuy;
            ++this.curRCRemain;
            TaskInform.sendMsgCurRCInform(this);
            dbReplaceInform(this);
            return true;
         }
      }
   }

   public boolean refreshRCStar() {
      if (this.curRCTask == null) {
         return false;
      } else if (this.curRCStar == 10) {
         SystemMessage.writeMessage(this.owner, 6005);
         return false;
      } else {
         TaskClazzRC rc = TaskConfigManager.getRC(this.owner);
         if (1 != PlayerManager.reduceMoney(this.owner, rc.getRefreshStarExpend())) {
            SystemMessage.writeMessage(this.owner, 23003);
            return false;
         } else {
            this.curRCStar = Rnd.get(1, 10);
            ArrowGuideManager am = this.owner.getArrowGuideManager();
            if (am.shouldPushRingTask()) {
               if (am.getGuideTimes(13) < ArrowGuideManager.getArrowInfo(13).getTimes()) {
                  this.curRCStar = Rnd.get(1, 9);
               } else if (am.getGuideTimes(13) == ArrowGuideManager.getArrowInfo(13).getTimes()) {
                  this.curRCStar = 10;
                  ArrowGuideManager.pushArrow(this.owner, 24, (String)null);
               }
            }

            List rewardList = this.curRCTask.getRewardList(rc.getRewardStarFactor(this.curRCStar));
            RefreshRCStar.sendResult(this.owner, this.curRCStar, rewardList);
            TaskRewardItemData.destroyList(rewardList);
            rewardList = null;
            TaskInform.sendMsgCurRCInform(this);
            dbReplaceInform(this);
            if (am.shouldPushRingTask()) {
               ArrowGuideManager.pushArrow(this.owner, 13, (String)null);
            }

            return true;
         }
      }
   }

   public int getCurRCStar() {
      return this.curRCStar;
   }

   public Task getCurZJTask() {
      return this.curZJTask;
   }

   public boolean isTaskOver(int taskId) {
      TaskData data = TaskConfigManager.getData(taskId);
      if (data == null) {
         return true;
      } else if (data.is(TaskClazz.ZJ)) {
         return this.curZJTask != null && data.getClazzIndex() < this.curZJTask.getClazzIndex();
      } else if (data.is(TaskClazz.ZZ)) {
         return this.curZZTask != null && data.getClazzIndex() < this.curZZTask.getClazzIndex();
      } else {
         return this.historyTaskMap.get(taskId) != null && ((Task)this.historyTaskMap.get(taskId)).is(TaskState.OVER);
      }
   }

   public Task getCurZZTask() {
      return this.curZZTask;
   }

   public Task getCurRCTask() {
      return this.curRCTask;
   }

   public boolean openXS(int xsId) {
      TaskClazzXS xs = TaskConfigManager.getXS(xsId);
      if (xs == null) {
         return false;
      } else if (this.curXSTaskMap.get(xs) != null) {
         SystemMessage.writeMessage(this.owner, 6009);
         return false;
      } else {
         int count = this.curXSCountMap.get(xs) != null ? ((Integer)this.curXSCountMap.get(xs)).intValue() : 0;
         int maxCount = xs.getOneDayCountLimit();
         if (count >= maxCount) {
            SystemMessage.writeMessage(this.owner, 6010);
            return false;
         } else if (!this.owner.getItemManager().deleteItemByModel(xs.getOpenItem().getModelID(), xs.getOpenItem().getCount(), 5).isOk()) {
            SystemMessage.writeMessage(this.owner, 6008);
            return false;
         } else {
            ++count;
            this.curXSCountMap.put(xs, count);
            OpenXSUseItem.open(this.owner, xs.getId(), true, Math.max(0, maxCount - count));
            this.accept(xs.getTaskHeader().getId(), true);
            dbReplaceXSCount(this.owner, xs.getId(), count);
            return true;
         }
      }
   }

   public boolean isCurrent(int taskId) {
      return this.currentTaskMap.get(taskId) != null && ((Task)this.currentTaskMap.get(taskId)).is(TaskState.RUN);
   }

   public int getCurRCRemain() {
      return this.curRCRemain;
   }

   public static void dbInsert(Task task, boolean deleteClazz) {
      if (task != null) {
         try {
            WriteOnlyPacket packet = Executor.InsertTask.toPacket(task, deleteClazz);
            task.getOwner().writePacket(packet);
            packet.destroy();
            packet = null;
         } catch (Exception var3) {
            var3.printStackTrace();
         }

      }
   }

   public static void dbUpdate(Task task) {
      if (task != null) {
         try {
            WriteOnlyPacket packet = Executor.UpdateTask.toPacket(task);
            task.getOwner().writePacket(packet);
            packet.destroy();
            packet = null;
         } catch (Exception var2) {
            var2.printStackTrace();
         }

      }
   }

   public static void dbDelete(Task task) {
      if (task != null) {
         try {
            WriteOnlyPacket packet = Executor.DeleteTask.toPacket(task.getOwner().getID(), task.getId());
            task.getOwner().writePacket(packet);
            packet.destroy();
            packet = null;
         } catch (Exception var2) {
            var2.printStackTrace();
         }

      }
   }

   public static void dbDeleteClazz(Player player, TaskClazz clazz) {
      try {
         WriteOnlyPacket packet = Executor.DeleteTaskClazz.toPacket(player.getID(), clazz.getValue());
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void dbReplaceXSCount(Player player, int xsId, int count) {
      try {
         WriteOnlyPacket packet = Executor.ReplaceTaskXSCount.toPacket(player.getID(), xsId, count);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void dbClearXSCount(Player player) {
      try {
         WriteOnlyPacket packet = Executor.ClearTaskXSCount.toPacket(player.getID());
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void dbReplaceInform(PlayerTaskManager ptm) {
      Player player = null;
      if (ptm != null && (player = ptm.getOwner()) != null) {
         try {
            WriteOnlyPacket packet = Executor.ReplaceTaskInform.toPacket(player.getID(), ptm.getCurRCStar(), ptm.curRCRemain, ptm.curRCBuy);
            player.writePacket(packet);
            packet.destroy();
            packet = null;
         } catch (Exception var3) {
            var3.printStackTrace();
         }

      }
   }

   public void destroy() {
      this.curZJTask = null;
      this.curRCTask = null;
      this.curZZTask = null;
      if (this.curXSTaskMap != null) {
         this.curXSTaskMap.clear();
         this.curXSTaskMap = null;
      }

      if (this.curXSCountMap != null) {
         this.curXSCountMap.clear();
         this.curXSCountMap = null;
      }

      if (this.canAcceptGHSet != null) {
         this.canAcceptGHSet.clear();
         this.canAcceptGHSet = null;
      }

      if (this.ghTasklist != null) {
         this.ghTasklist.clear();
         this.ghTasklist = null;
      }

      Iterator it;
      CopyOnWriteArraySet v;
      if (this.check_count != null) {
         it = this.check_count.values().iterator();

         while(it.hasNext()) {
            v = (CopyOnWriteArraySet)it.next();
            v.clear();
         }

         this.check_count.clear();
         this.check_count = null;
      }

      if (this.check_value != null) {
         it = this.check_value.values().iterator();

         while(it.hasNext()) {
            v = (CopyOnWriteArraySet)it.next();
            v.clear();
         }

         this.check_value.clear();
         this.check_value = null;
      }

      if (this.check_specify != null) {
         it = this.check_specify.values().iterator();

         while(it.hasNext()) {
            v = (CopyOnWriteArraySet)it.next();
            v.clear();
         }

         this.check_specify.clear();
         this.check_specify = null;
      }

      if (this.check_specifyMore != null) {
         it = this.check_specifyMore.values().iterator();

         while(it.hasNext()) {
            v = (CopyOnWriteArraySet)it.next();
            v.clear();
         }

         this.check_specifyMore.clear();
         this.check_specifyMore = null;
      }

      Task task;
      if (this.currentTaskMap != null) {
         it = this.currentTaskMap.values().iterator();

         while(it.hasNext()) {
            task = (Task)it.next();
            task.destroy();
         }

         this.currentTaskMap.clear();
         this.currentTaskMap = null;
      }

      if (this.historyTaskMap != null) {
         it = this.historyTaskMap.values().iterator();

         while(it.hasNext()) {
            task = (Task)it.next();
            task.destroy();
         }

         this.historyTaskMap.clear();
         this.historyTaskMap = null;
      }

   }

   public Task getCurZXTask() {
      return this.curZXTask;
   }

   public Task getCurTXTask() {
      return this.curTXTask;
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$task$clazz$TaskClazz() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$task$clazz$TaskClazz;
      if ($SWITCH_TABLE$com$mu$game$model$task$clazz$TaskClazz != null) {
         return var10000;
      } else {
         int[] var0 = new int[TaskClazz.values().length];

         try {
            var0[TaskClazz.GH.ordinal()] = 4;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            var0[TaskClazz.RC.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[TaskClazz.TanXian.ordinal()] = 7;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[TaskClazz.XS.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[TaskClazz.ZJ.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[TaskClazz.ZX.ordinal()] = 6;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[TaskClazz.ZZ.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$task$clazz$TaskClazz = var0;
         return var0;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$task$target$TargetType() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$task$target$TargetType;
      if ($SWITCH_TABLE$com$mu$game$model$task$target$TargetType != null) {
         return var10000;
      } else {
         int[] var0 = new int[TargetType.values().length];

         try {
            var0[TargetType.COUNT.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[TargetType.MoreSPECIFY.ordinal()] = 5;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[TargetType.SPECIFY.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[TargetType.VALUE.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[TargetType.VisitNpc.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$task$target$TargetType = var0;
         return var0;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$dialog$DialogOptionSee() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$dialog$DialogOptionSee;
      if ($SWITCH_TABLE$com$mu$game$model$dialog$DialogOptionSee != null) {
         return var10000;
      } else {
         int[] var0 = new int[DialogOptionSee.values().length];

         try {
            var0[DialogOptionSee.HIDE.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[DialogOptionSee.VISIBLE.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[DialogOptionSee.VISIBLE_TASK_ACCEPT.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[DialogOptionSee.VISIBLE_TASK_SUBMIT.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[DialogOptionSee.VISIBLE_TASK_VISIT.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$dialog$DialogOptionSee = var0;
         return var0;
      }
   }
}
