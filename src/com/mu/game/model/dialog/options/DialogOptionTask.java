package com.mu.game.model.dialog.options;

import com.mu.game.model.dialog.DialogOption;
import com.mu.game.model.dialog.DialogOptionSee;
import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.TaskDialog;
import com.mu.game.model.task.clazz.TaskClazz;
import com.mu.game.model.task.target.TaskTarget;
import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dialog.OpenDialog;
import com.mu.io.game.packet.imp.task.TaskInform;

public class DialogOptionTask extends DialogOption {
   private TaskData data;
   private DialogOptionSee see;
   private TaskDialog dialog;
   private TaskTarget target;
   private long taskNpc;

   public DialogOptionSee canSee(Player player, Npc npc) {
      PlayerTaskManager ptm = player.getTaskManager();
      return this.taskNpc == npc.getID() && ptm != null && ptm.onEventDialogSee(npc, this) ? this.see : DialogOptionSee.HIDE;
   }

   public void option(Player player, Npc npc) {
      player.setDot(this);
      if (this.data.is(TaskClazz.RC)) {
         TaskInform.sendMsgRCDialog(player, true);
      } else {
         OpenDialog.showTaskDialog(player, npc, this.data, this.dialog);
      }
   }

   public TaskData getData() {
      return this.data;
   }

   public DialogOptionSee getSee() {
      return this.see;
   }

   public void setData(TaskData data) {
      this.data = data;
   }

   public void setSee(DialogOptionSee see) {
      this.see = see;
   }

   public TaskDialog getDialog() {
      return this.dialog;
   }

   public void setDialog(TaskDialog dialog) {
      this.dialog = dialog;
   }

   public TaskTarget getTarget() {
      return this.target;
   }

   public void setTarget(TaskTarget target) {
      this.target = target;
   }

   public long getTaskNpc() {
      return this.taskNpc;
   }

   public void setTaskNpc(long taskNpc) {
      this.taskNpc = taskNpc;
   }
}
