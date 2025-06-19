package com.mu.game.model.task;

import com.mu.game.model.dialog.DialogConfigManager;
import com.mu.game.model.dialog.DialogOptionSee;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.tanxian.TanXianConfigManager;
import com.mu.game.model.task.clazz.TaskClazz;
import com.mu.game.model.task.target.TaskTarget;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.Profession;
import com.mu.utils.CommonRegPattern;
import com.mu.utils.buffer.BufferWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import org.jdom.IllegalDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskData {
   private static Logger logger = LoggerFactory.getLogger(TaskData.class);
   private int id;
   private String name;
   private int icon0;
   private int icon1;
   private int icon2;
   private int quality;
   private boolean disabled;
   private TaskClazz clazz;
   private String clazzName;
   private boolean autoAccept;
   private boolean autoSubmit;
   private int level;
   private int beforeTask;
   private long acceptNpc;
   private TaskDialog acceptDialog;
   private long submitNpc;
   private TaskDialog submitDialog;
   private String description;
   private TaskTarget[] targets;
   private byte[] linkData;
   private int linkSize;
   private String acceptTraceStr;
   private String targetTraceStr;
   private String submitTraceStr;
   private List rewardProfessionList0;
   private List rewardProfessionList1;
   private List rewardProfessionList2;
   private int[] runLink;
   private int clazzId;
   private int clazzIndex;
   private TaskData clazzNext;
   private boolean skip;
   private int cjId;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$task$clazz$TaskClazz;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$task$TaskState$TaskClientState;

   public TaskData(int id, String name) {
      this.id = id;
      this.name = name;
   }

   public boolean is(TaskClazz clazz) {
      return this.clazz == clazz;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public boolean isDisabled() {
      return this.disabled;
   }

   public TaskClazz getClazz() {
      return this.clazz;
   }

   public String getClazzName() {
      return this.clazzName;
   }

   public Object getClazzData(Player player) {
      switch($SWITCH_TABLE$com$mu$game$model$task$clazz$TaskClazz()[this.getClazz().ordinal()]) {
      case 1:
         return TaskConfigManager.getZJ(this.getClazzId());
      case 2:
         return TaskConfigManager.getRC(player);
      case 3:
         return TaskConfigManager.getXS(this.getClazzId());
      case 4:
      case 5:
      case 6:
      default:
         return null;
      case 7:
         return TanXianConfigManager.getData(this.getClazzId());
      }
   }

   public boolean isAutoAccept() {
      return this.autoAccept;
   }

   public boolean isAutoSubmit() {
      return this.autoSubmit;
   }

   public int getLevel() {
      return this.level;
   }

   public int getBeforeTask() {
      return this.beforeTask;
   }

   public long getAcceptNpc() {
      return this.acceptNpc;
   }

   public long getSubmitNpc() {
      return this.submitNpc;
   }

   public String getDescription() {
      return this.description;
   }

   public TaskTarget[] getTargets() {
      return this.targets;
   }

   public byte[] getLinkData() {
      return this.linkData;
   }

   public String getAcceptTraceStr() {
      return this.acceptTraceStr;
   }

   public String getTargetTraceStr() {
      return this.targetTraceStr;
   }

   public String getSubmitTraceStr() {
      return this.submitTraceStr;
   }

   public boolean isSkip() {
      return this.skip;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setDisabled(boolean disabled) {
      this.disabled = disabled;
   }

   public void setClazz(TaskClazz clazz) {
      this.clazz = clazz;
   }

   public void setClazzName(String clazzName) {
      this.clazzName = clazzName;
   }

   public void setAutoAccept(boolean autoAccept) {
      this.autoAccept = autoAccept;
   }

   public void setAutoSubmit(boolean autoSubmit) {
      this.autoSubmit = autoSubmit;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public void setBeforeTask(int beforeTask) {
      this.beforeTask = beforeTask;
   }

   public void setAcceptNpc(long acceptNpc) {
      this.acceptNpc = acceptNpc;
   }

   public void setSubmitNpc(long submitNpc) {
      this.submitNpc = submitNpc;
   }

   public TaskDialog getAcceptDialog() {
      return this.acceptDialog;
   }

   public void setAcceptDialog(TaskDialog acceptDialog) {
      try {
         DialogConfigManager.addTaskOption(this, this.acceptNpc, acceptDialog, DialogOptionSee.VISIBLE_TASK_ACCEPT, (TaskTarget)null);
         this.acceptDialog = acceptDialog;
      } catch (Exception var3) {
         logger.error("task[{}] config err, set accept dialog err, npc[{}]", this.id, this.acceptNpc);
      }

   }

   public TaskDialog getSubmitDialog() {
      return this.submitDialog;
   }

   public void setSubmitDialog(TaskDialog submitDialog) {
      try {
         DialogConfigManager.addTaskOption(this, this.submitNpc, submitDialog, DialogOptionSee.VISIBLE_TASK_SUBMIT, (TaskTarget)null);
         if (this.is(TaskClazz.RC)) {
            DialogConfigManager.addTaskOption(this, this.submitNpc, submitDialog, DialogOptionSee.VISIBLE_TASK_VISIT, (TaskTarget)null);
         }

         this.submitDialog = submitDialog;
      } catch (Exception var3) {
         logger.error("task[{}] config err, set submit dialog err, npc[{}]", this.id, this.submitNpc);
      }

   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setAcceptTraceStr(String acceptTraceStr) {
      this.acceptTraceStr = acceptTraceStr;
   }

   public void setTargetTraceStr(String targetTraceStr) {
      this.targetTraceStr = targetTraceStr;
   }

   public void setSubmitTraceStr(String submitTraceStr) {
      this.submitTraceStr = submitTraceStr;
   }

   public void setSkip(boolean skip) {
      this.skip = skip;
   }

   public int getIcon(int profession) {
      return profession == 0 ? this.icon0 : (profession == 1 ? this.icon1 : this.icon2);
   }

   public void setIcon(String iconStr) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(iconStr);
      m.find();
      this.icon0 = Integer.parseInt(m.group());
      m.find();
      this.icon1 = Integer.parseInt(m.group());
      m.find();
      this.icon2 = Integer.parseInt(m.group());
   }

   public int getClazzId() {
      return this.clazzId;
   }

   public void setClazzId(int clazzId) {
      this.clazzId = clazzId;
   }

   public void setTargetStr(String targetStr) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(targetStr);
      List targetList = new ArrayList();

      for(int i = 0; m.find(); ++i) {
         TaskTarget target = TaskTarget.newInstance(this, i, Integer.parseInt(m.group()), m);
         if (target == null) {
            throw new IllegalDataException("task config err");
         }

         targetList.add(target);
      }

      this.targets = (TaskTarget[])targetList.toArray(new TaskTarget[targetList.size()]);
   }

   public void setLinkStr(String linkStr) {
      BufferWriter bw = new BufferWriter();

      try {
         Matcher m = CommonRegPattern.PATTERN_INT.matcher(linkStr);
         this.linkSize = 0;
         bw.writeByte(this.linkSize);

         while(m.find()) {
            TaskPosition tp = null;
            int linkType = Integer.parseInt(m.group());
            m.find();
            ++this.linkSize;
            bw.writeByte(linkType);
            switch(linkType) {
            case 0:
               tp = TaskConfigManager.getPosition(Integer.parseInt(m.group()));
               bw.writeShort(tp.getMapId());
               bw.writeInt(tp.getX());
               bw.writeInt(tp.getY());
               break;
            case 1:
               tp = TaskConfigManager.getPosition(Integer.parseInt(m.group()));
               bw.writeShort(tp.getMapId());
               bw.writeInt(tp.getX());
               bw.writeInt(tp.getY());
               m.find();
               bw.writeDouble((double)Long.parseLong(m.group()));
               break;
            case 2:
               tp = TaskConfigManager.getPosition(Integer.parseInt(m.group()));
               bw.writeShort(tp.getMapId());
               bw.writeInt(tp.getX());
               bw.writeInt(tp.getY());
               m.find();
               bw.writeInt(Integer.parseInt(m.group()));
               break;
            case 3:
               tp = TaskConfigManager.getPosition(Integer.parseInt(m.group()));
               bw.writeShort(tp.getMapId());
               bw.writeInt(tp.getX());
               bw.writeInt(tp.getY());
               m.find();
               this.cjId = Integer.parseInt(m.group());
               bw.writeInt(this.cjId);
               TaskConfigManager.addCJMappingTask(this.cjId, this.id);
               break;
            case 4:
            case 5:
            case 9:
            case 10:
            case 11:
            default:
               logger.error("task[{}] config error, linkstr parse failed!", this.id);
               break;
            case 6:
               bw.writeShort(Integer.parseInt(m.group()));
               m.find();
               bw.writeByte(Integer.parseInt(m.group()));
               break;
            case 7:
               bw.writeByte(Integer.parseInt(m.group()));
               break;
            case 8:
            case 12:
               bw.writeInt(Integer.parseInt(m.group()));
               break;
            case 13:
               bw.writeByte(Integer.parseInt(m.group()));
               m.find();
               bw.writeShort(Integer.parseInt(m.group()));
               break;
            case 14:
               bw.writeByte(Integer.parseInt(m.group()));
               m.find();
               bw.writeByte(Integer.parseInt(m.group()));
               m.find();
               bw.writeShort(Integer.parseInt(m.group()));
            }
         }

         bw.flush();
         this.linkData = bw.toByteArray();
         this.linkData[0] = (byte)this.linkSize;
         bw.destroy();
      } catch (Exception var6) {
         logger.error("task[{}] config error, linkstr parse failed! {}", this.id, var6.toString());
      }

   }

   public void setRewardStr(String rewardStr) {
      List list0 = new ArrayList();
      List list1 = new ArrayList();
      List list2 = new ArrayList();
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(rewardStr);

      while(true) {
         while(true) {
            while(m.find()) {
               int itemId = Integer.parseInt(m.group());
               m.find();
               int count = Integer.parseInt(m.group());
               m.find();
               boolean bind = Integer.parseInt(m.group()) != 0;
               ItemModel im = ItemModel.getModel(itemId);
               m.find();
               int attributeGroupId = Integer.parseInt(m.group());
               m.find();
               long expireTime = Long.parseLong(m.group());
               TaskRewardItemData tr = TaskRewardItemData.newInstance(itemId, count, bind, attributeGroupId, expireTime);
               if (im != null && tr != null) {
                  if (im.getProfession().isEmpty()) {
                     list0.add(tr);
                     list1.add(tr);
                     list2.add(tr);
                  } else {
                     Iterator var15 = im.getProfession().iterator();

                     while(var15.hasNext()) {
                        Integer proID = (Integer)var15.next();
                        int proType = Profession.getProfessionID(proID.intValue());
                        switch(proType) {
                        case 0:
                           if (!list0.contains(tr)) {
                              list0.add(tr);
                           }
                           break;
                        case 1:
                           if (!list1.contains(tr)) {
                              list1.add(tr);
                           }
                           break;
                        case 2:
                           if (!list2.contains(tr)) {
                              list2.add(tr);
                           }
                        }
                     }
                  }
               } else {
                  logger.error("TaskData[{}] config error, not found item by id[{}]", this.id, itemId);
               }
            }

            this.rewardProfessionList0 = list0;
            this.rewardProfessionList1 = list1;
            this.rewardProfessionList2 = list2;
            return;
         }
      }
   }

   public int getClazzIndex() {
      return this.clazzIndex;
   }

   public void setClazzIndex(int clazzIndex) {
      this.clazzIndex = clazzIndex;
   }

   public TaskData getClazzNext() {
      return this.clazzNext;
   }

   public void setClazzNext(TaskData clazzNext) {
      this.clazzNext = clazzNext;
   }

   public List getRewardProfessionList0() {
      return this.rewardProfessionList0;
   }

   public List getRewardProfessionList1() {
      return this.rewardProfessionList1;
   }

   public List getRewardProfessionList2() {
      return this.rewardProfessionList2;
   }

   public int getLinkSize() {
      return this.linkSize;
   }

   public List getRewardList(int profession) {
      return profession == 0 ? this.rewardProfessionList0 : (profession == 1 ? this.rewardProfessionList1 : (profession == 2 ? this.rewardProfessionList2 : null));
   }

   public int getQuality() {
      return this.quality;
   }

   public void setQuality(int quality) {
      this.quality = quality;
   }

   public int getCjId() {
      return this.cjId;
   }

   public void setRunLink(String indexStr) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(indexStr);
      this.runLink = new int[3];

      for(int i = 0; i < this.runLink.length; ++i) {
         m.find();
         this.runLink[i] = Integer.parseInt(m.group());
      }

   }

   public int getRunLink(TaskState.TaskClientState state) {
      switch($SWITCH_TABLE$com$mu$game$model$task$TaskState$TaskClientState()[state.ordinal()]) {
      case 1:
         return this.runLink[0];
      case 2:
         return this.runLink[1];
      case 3:
      case 4:
         return this.runLink[2];
      default:
         return 0;
      }
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
   static int[] $SWITCH_TABLE$com$mu$game$model$task$TaskState$TaskClientState() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$task$TaskState$TaskClientState;
      if ($SWITCH_TABLE$com$mu$game$model$task$TaskState$TaskClientState != null) {
         return var10000;
      } else {
         int[] var0 = new int[TaskState.TaskClientState.values().length];

         try {
            var0[TaskState.TaskClientState.NEW.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[TaskState.TaskClientState.OVER.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[TaskState.TaskClientState.RUN_COMPLETED.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[TaskState.TaskClientState.RUN_UNCOMPLETED.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$task$TaskState$TaskClientState = var0;
         return var0;
      }
   }
}
