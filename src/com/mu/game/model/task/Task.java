package com.mu.game.model.task;

import com.mu.game.model.guide.TaskActionManager;
import com.mu.game.model.task.clazz.TaskClazz;
import com.mu.game.model.task.clazz.TaskClazzRC;
import com.mu.game.model.task.target.TaskTarget;
import com.mu.game.model.task.target.TaskTargetRate;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.npc.UpdateNpcHeadIcon;
import com.mu.io.game.packet.imp.task.InitTask;
import com.mu.io.game.packet.imp.task.TaskInform;
import com.mu.io.game.packet.imp.task.TaskPushNext;
import com.mu.utils.CommonRegPattern;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

public class Task {
   private Player owner;
   private TaskData data;
   private TaskState state;
   private TaskTarget[] targets;
   private TaskTargetRate[] rates;
   private boolean destroy;
   private boolean forceComplete;
   private String curTraceStr = "";

   private Task(Player owner, TaskData data) {
      this.owner = owner;
      this.data = data;
      this.state = TaskState.NEW;
   }

   public static Task createForNew(Player owner, TaskData data) {
      try {
         if (data != null && owner != null) {
            Task task = new Task(owner, data);
            task.init(true, (String)null);
            return task;
         } else {
            return null;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static Task createForDB(Player owner, InitTask packet) {
      try {
         int taskId = packet.readInt();
         int state = packet.readByte();
         String rateStr = packet.readUTF();
         TaskData data = TaskConfigManager.getData(taskId);
         if (data == null) {
            return null;
         } else {
            Task task = new Task(owner, data);
            task.setState(TaskState.valueOf(state));
            task.init(false, rateStr);
            return task;
         }
      } catch (Exception var7) {
         var7.printStackTrace();
         return null;
      }
   }

   private void init(boolean isNew, String rateStr) {
      this.targets = this.data.getTargets();
      this.rates = new TaskTargetRate[this.targets.length];

      int i;
      for(i = 0; i < this.targets.length; ++i) {
         this.rates[i] = new TaskTargetRate(this, this.targets[i]);
      }

      if (!isNew) {
         Matcher m = CommonRegPattern.PATTERN_INT.matcher(rateStr);

         for(i = 0; i < this.rates.length && m.find(); ++i) {
            this.rates[i].setRate(Integer.parseInt(m.group()));
         }
      }

      for(i = 0; i < this.targets.length; ++i) {
         this.targets[i].init(this);
      }

   }

   public Object getClazzData() {
      return this.data.getClazzData(this.owner);
   }

   public Player getOwner() {
      return this.owner;
   }

   public TaskData getData() {
      return this.data;
   }

   public boolean isSkip() {
      return this.data.isSkip();
   }

   public boolean isDisabled() {
      return this.data.isDisabled();
   }

   public boolean isComplete() {
      if (!this.is(TaskState.RUN)) {
         return false;
      } else if (this.isForceComplete()) {
         return true;
      } else {
         for(int i = 0; i < this.rates.length; ++i) {
            if (!this.rates[i].ok()) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean is(TaskState state) {
      return this.state == state;
   }

   public boolean is(TaskClazz clazz) {
      return this.data.is(clazz);
   }

   public boolean is(TaskData data) {
      return this.data == data;
   }

   public boolean isAutoAccept() {
      return this.data.isAutoAccept();
   }

   public boolean isAutoSubmit() {
      return this.data.isAutoSubmit();
   }

   public boolean nextIs(TaskData data) {
      return this.data.getClazzNext() == data;
   }

   public void setState(TaskState state) {
      this.state = state;
   }

   public TaskState getState() {
      return this.state;
   }

   public TaskTarget[] getTargets() {
      return this.targets;
   }

   public TaskTargetRate[] getRates() {
      return this.rates;
   }

   public TaskTargetRate getRate(int index) {
      return this.rates[index];
   }

   public String getRateStr() {
      String rateStr = "";

      for(int i = 0; i < this.rates.length; ++i) {
         rateStr = rateStr + this.rates[i].getCurRate() + ", ";
      }

      return rateStr;
   }

   public int getId() {
      return this.data.getId();
   }

   public TaskClazz getClazz() {
      return this.data.getClazz();
   }

   public int getClazzId() {
      return this.data.getClazzId();
   }

   public int getClazzIndex() {
      return this.data.getClazzIndex();
   }

   public TaskData getClazzNext() {
      return this.data.getClazzNext();
   }

   public int getClazzNextId() {
      return this.data.getClazzNext() != null ? this.data.getClazzNext().getId() : 0;
   }

   public boolean isForceComplete() {
      return this.forceComplete;
   }

   public void accpet() {
      this.setState(TaskState.RUN);

      for(int i = 0; i < this.targets.length; ++i) {
         try {
            this.rates[i].clear();
            this.targets[i].accept(this);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

   }

   public void submit() {
      this.setState(TaskState.OVER);

      try {
         TaskTarget[] var4 = this.targets;
         int var3 = this.targets.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            TaskTarget target = var4[var2];
            target.submit(this);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void onRateChangeCheckComplete() {
      Player player = this.getOwner();
      TaskInform.sendMsgTaskState(this);
      PlayerTaskManager.dbUpdate(this);
      if (this.isComplete()) {
         TaskActionManager.doCompleteAction(player, this.getId());
         if (this.is(TaskClazz.RC)) {
            TaskInform.sendMsgCurRCInform(this.owner.getTaskManager());
         }

         if (this.is(TaskClazz.ZX)) {
            TaskInform.sendMsgZXTip(this.owner, this);
         }

         if (this.isAutoSubmit()) {
            PlayerTaskManager ptm = player.getTaskManager();
            ptm.submit(this.getId(), true);
         } else {
            UpdateNpcHeadIcon.updatePlayerSeeNpcHeadIcon(player, this.getData().getSubmitNpc());
            if (this.owner.isNeddAutoTask() && !this.owner.isInDungeon()) {
               TaskPushNext.pushNext(this.owner, true);
            }
         }
      }

   }

   public boolean forceComplete() {
      if (!this.isForceComplete() && this.state == TaskState.RUN) {
         this.forceComplete = true;
         TaskTargetRate[] rates = this.getRates();
         boolean flag = false;

         for(int i = 0; i < rates.length; ++i) {
            TaskTargetRate rate = rates[i];
            if (!rate.ok()) {
               flag = true;
            }

            rate.forceComplete();
         }

         if (rates.length == 0 || flag) {
            this.onRateChangeCheckComplete();
         }

         return true;
      } else {
         return false;
      }
   }

   public int getAutoLinkIndex() {
      return 0;
   }

   public List getRewardList() {
      if (this.is(TaskClazz.RC)) {
         TaskClazzRC rc = (TaskClazzRC)this.getClazzData();
         List list = rc.getBaseRewardList();
         return list;
      } else {
         return this.data.getRewardList(this.owner.getProType());
      }
   }

   public List getRewardList(double multipe) {
      return this.multipeReward(this.getRewardList(), multipe);
   }

   private List multipeReward(List list, double multipe) {
      List newList = new ArrayList();
      Iterator it = list.iterator();

      while(it.hasNext()) {
         TaskRewardItemData TRI = (TaskRewardItemData)it.next();
         TaskRewardItemData newTRI = TRI.newInstance((int)((double)TRI.getCount() * multipe));
         newList.add(newTRI);
      }

      return newList;
   }

   public boolean reward(double multipe) {
      List rewardList = this.getRewardList(multipe);
      boolean success = rewardList.isEmpty() ? true : this.owner.getItemManager().addItem(rewardList).isOk();
      TaskRewardItemData.destroyList(rewardList);
      rewardList = null;
      return success;
   }

   public boolean reward(List rewardList) {
      return rewardList.isEmpty() ? true : this.owner.getItemManager().addItem(rewardList).isOk();
   }

   public boolean isDestroy() {
      return this.destroy;
   }

   public void destroy() {
      if (!this.isDestroy()) {
         this.destroy = true;
         this.owner = null;
         this.data = null;
         this.state = null;
         this.targets = null;
         if (this.rates != null) {
            for(int i = 0; i < this.rates.length; ++i) {
               this.rates[i].destroy();
               this.rates[i] = null;
            }

            this.rates = null;
         }

      }
   }

   public String getCurTraceStr() {
      return this.curTraceStr;
   }

   public void setCurTraceStr(String curTraceStr) {
      this.curTraceStr = curTraceStr;
   }
}
