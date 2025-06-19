package com.mu.game.model.tanxian;

import com.mu.config.MessageText;
import com.mu.db.log.IngotChangeType;
import com.mu.executor.Executor;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.TaskState;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.vip.effect.VIPEffectType;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.sys.BottomMessage;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.io.game.packet.imp.tanxian.TanXianInform;
import com.mu.io.game.packet.imp.tanxian.TanXianInit;
import java.io.IOException;

public class PlayerTanXianManager {
   private Player owner;
   private int level;
   private int exp;
   private int ingotCount;

   public PlayerTanXianManager(Player player) {
      this.owner = player;
      this.level = 1;
   }

   public void init(TanXianInit packet) {
      try {
         boolean has = packet.readBoolean();
         if (has) {
            this.level = packet.readInt();
            this.exp = packet.readInt();
            this.ingotCount = packet.readInt();
         }

         if (this.owner.isNeedZeroClear()) {
            this.ingotCount = 0;
            this.dbUpdate();
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public Player getOwner() {
      return this.owner;
   }

   public int getLevel() {
      return this.level;
   }

   public int getExp() {
      return this.exp;
   }

   public int getIngotCount() {
      return this.ingotCount;
   }

   public int getRemainCount() {
      return TanXianConfigManager.TANXIAN_BUY_COUNT + this.getOwner().getVIPManager().getEffectIntegerValue(VIPEffectType.VE_23) - this.ingotCount;
   }

   public void complete() {
      Task task = this.getOwner().getTaskManager().getCurTXTask();
      TanXianData data = (TanXianData)task.getClazzData();
      int sum = this.exp + data.getExp();
      if (sum >= TanXianConfigManager.getLevelExp(this.level) && this.level < TanXianConfigManager.getMaxLevel()) {
         this.exp = sum - TanXianConfigManager.getLevelExp(this.level);
         ++this.level;
      } else {
         this.exp = sum;
      }

      this.dbUpdate();
      TanXianInform.sendMsgTanXianSuccess(this.owner);
      this.getOwner().getTaskManager().onEventCheckCount(TargetType.CountType.TanXian);
      if (this.level < TanXianConfigManager.getMaxLevel()) {
         BottomMessage.pushMessage(this.owner, MessageText.getText(23407).replace("%s%", String.valueOf(data.getExp())), 23407);
      }

   }

   public void addExp(int addexp) {
      int sum = this.exp + addexp;
      int maxExp = TanXianConfigManager.getLevelExp(this.level);
      if (sum >= maxExp && this.level < TanXianConfigManager.getMaxLevel()) {
         ++this.level;
         this.exp = Math.min(sum - maxExp, TanXianConfigManager.getLevelExp(this.level) - 1);
      } else {
         this.exp = sum;
      }

      this.dbUpdate();
      TanXianInform.sendMsgTanXianSuccess(this.owner);
      boolean pushAddExp = false;
      if (this.level < TanXianConfigManager.getMaxLevel()) {
         pushAddExp = true;
      } else if (sum < maxExp) {
         pushAddExp = true;
      }

      if (pushAddExp) {
         BottomMessage.pushMessage(this.owner, MessageText.getText(23407).replace("%s%", String.valueOf(this.exp)), 23407);
      }

   }

   public boolean start(int type, int id) {
      TanXianData data = TanXianConfigManager.getData(id);
      if (data == null) {
         return false;
      } else if (this.inTanXian()) {
         return false;
      } else {
         MapData md = MapConfig.getMapData(id);
         if (md != null && md.getReqLevel() <= this.getOwner().getLevel()) {
            switch(type) {
            case 0:
               if (!this.owner.getItemManager().deleteItemByModel(TanXianConfigManager.TANXIAN_EXPEND_ITEM, 1, 41).isOk()) {
                  SystemMessage.writeMessage(this.owner, 6008);
                  return false;
               }
               break;
            case 1:
               if (this.getRemainCount() < 1) {
                  return false;
               }

               if (1 != PlayerManager.reduceIngot(this.owner, TanXianConfigManager.TANXIAN_EXPEND_INGOT, IngotChangeType.TanXian, this.level + ", " + id)) {
                  SystemMessage.writeMessage(this.owner, 1015);
                  return false;
               }

               ++this.ingotCount;
               this.dbUpdate();
               break;
            default:
               return false;
            }

            TaskData td = data.getData();
            if (td != null) {
               this.getOwner().getTaskManager().accept(td.getId(), true);
            }

            return true;
         } else {
            SystemMessage.writeMessage(this.owner, MessageText.getText(1029).replace("%s%", "" + md.getReqLevel()), 1029);
            return false;
         }
      }
   }

   public boolean inTanXian() {
      Task task = this.owner.getTaskManager().getCurTXTask();
      return task != null && task.is(TaskState.RUN);
   }

   public void dbUpdate() {
      try {
         WriteOnlyPacket packet = Executor.UpdateTanXian.toPacket(this.owner.getID(), this.level, this.exp, this.ingotCount);
         this.getOwner().writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void setIngotCount(int ingotCount) {
      this.ingotCount = ingotCount;
   }
}
