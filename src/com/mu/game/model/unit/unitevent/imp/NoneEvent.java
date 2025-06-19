package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.dialog.options.DialogOptionTask;
import com.mu.game.model.map.Map;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.unit.MapUnit;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.io.game.packet.imp.dialog.CloseDialog;
import com.mu.io.game.packet.imp.task.TaskPushNext;

public class NoneEvent extends Event {
   private long beginTime = System.currentTimeMillis();

   public NoneEvent(MapUnit owner) {
      super(owner);
   }

   public void work(long now) throws Exception {
      if (this.getOwner().getUnitType() == 1) {
         Player player = (Player)this.getOwner();
         if (player.isDie() || player.isDestroy()) {
            return;
         }

         if (player.isInDungeon()) {
            if (now - this.beginTime >= 10000L) {
               Map map = player.getDungeonMap();
               map.checkIdle(player);
               this.beginTime = now;
            }

            return;
         }

         if (player.isNeddAutoTask()) {
            if (now - this.beginTime >= 10000L) {
               DialogOptionTask dot = player.getDot();
               if (dot != null && dot.getData().getClazzIndex() < TaskConfigManager.TASK_TRACE_ZJ_INDEX && dot.getData() == player.getTaskManager().getCurZJTask().getData()) {
                  player.getTaskManager().onEventDialogEnd(dot.getData().getId(), dot.getTaskNpc());
                  player.setDot((DialogOptionTask)null);
                  CloseDialog.close(player);
                  this.beginTime = now;
                  return;
               }

               if (TaskPushNext.pushNext(player, false)) {
                  this.beginTime = now;
                  return;
               }
            }

            player = null;
         }
      }

   }

   public Status getStatus() {
      return Status.NONE;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
