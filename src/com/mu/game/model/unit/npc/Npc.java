package com.mu.game.model.unit.npc;

import com.mu.game.model.dialog.Dialog;
import com.mu.game.model.dialog.DialogConfigManager;
import com.mu.game.model.dialog.DialogOption;
import com.mu.game.model.dialog.DialogOptionSee;
import com.mu.game.model.dialog.DialogOptionType;
import com.mu.game.model.dialog.options.DialogOptionTask;
import com.mu.game.model.map.Map;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.dialog.CloseDialog;
import com.mu.io.game.packet.imp.dialog.OpenDialog;
import com.mu.io.game.packet.imp.npc.AroundNpc;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Npc extends Creature {
   private static Logger logger = LoggerFactory.getLogger(Npc.class);
   private boolean isDynamic = false;
   private Dialog dialog;

   public Npc(long id, Map map) {
      super(id, map);
   }

   public final boolean isDynamic() {
      return this.isDynamic;
   }

   public final void setDynamic(boolean isDynamic) {
      this.isDynamic = isDynamic;
   }

   public int getType() {
      return 3;
   }

   public WriteOnlyPacket createAroundSelfPacket(Player viewer) {
      return new AroundNpc(this);
   }

   public void switchArea(Rectangle newArea, Rectangle oldArea) {
      this.toNewArea(newArea, oldArea);
   }

   public boolean doAttack(Creature target, AttackResult result, boolean handleMotion) {
      boolean attackSuccess = target.beAttacked(this, result);
      if (attackSuccess) {
         target.hpReduceForDamage(this, result);
      }

      return attackSuccess;
   }

   public Dialog getDialog() {
      return this.dialog;
   }

   public void setDialog(Dialog dialog) {
      this.dialog = dialog;
   }

   public void onDialogRequest(Player player) {
      if (this.getDialog() == null) {
         logger.error("dialog is null");
      } else {
         DialogOptionTask shortTo = null;
         int priority = 10;
         HashMap optionSeeMap = new LinkedHashMap();
         List list = this.dialog.getOptionList();

         for(int i = 0; i < list.size(); ++i) {
            DialogOption option = (DialogOption)list.get(i);
            DialogOptionSee see = option.canSee(player, this);
            if (see.isVisible()) {
               if (option.getType() == DialogOptionType.DOT_TASK) {
                  DialogOptionTask to = (DialogOptionTask)option;
                  if (priority >= 1 && to.getSee() == DialogOptionSee.VISIBLE_TASK_SUBMIT) {
                     shortTo = to;
                     boolean var10 = true;
                     break;
                  }

                  if (priority >= 2 && to.getSee() == DialogOptionSee.VISIBLE_TASK_VISIT) {
                     shortTo = to;
                     priority = 2;
                  } else if (priority >= 3 && to.getSee() == DialogOptionSee.VISIBLE_TASK_ACCEPT) {
                     shortTo = to;
                     priority = 3;
                  }
               }

               optionSeeMap.put(option, see);
            }
         }

         if (shortTo == null) {
            OpenDialog.showNpcDialog(player, this, this.dialog, optionSeeMap);
         } else {
            this.onDialogChooseOption(player, shortTo.getId());
         }

      }
   }

   public void onDialogChooseOption(Player player, int optionId) {
      if (this.getMapID() != player.getMapID()) {
         logger.error("npc[{}] and role[{}] not in same map", this.getID(), player.getName());
      } else {
         Dialog dialog = this.getDialog();
         DialogOption option = DialogConfigManager.getOption(optionId);
         if (dialog != null && option != null && dialog.contains(option)) {
            if (option.canSee(player, this).isHide()) {
               logger.error("can not see option {} ", option.getId());
            } else {
               if (option.isClose()) {
                  CloseDialog.close(player);
               }

               option.option(player, this);
            }
         } else {
            logger.error("not find DialogOption[{}] by npc[{}] ", optionId, this.getID());
         }
      }
   }

   public DialogOptionTask getCanSeeTaskOption(TaskData data, Player player) {
      if (this.dialog == null) {
         return null;
      } else {
         Iterator var4 = this.dialog.getOptionList().iterator();

         while(var4.hasNext()) {
            DialogOption option = (DialogOption)var4.next();
            if (option.getType() == DialogOptionType.DOT_TASK) {
               DialogOptionTask dot = (DialogOptionTask)option;
               if (dot.getData() == data && dot.canSee(player, this).isVisible()) {
                  return dot;
               }
            }
         }

         return null;
      }
   }

   public int canBeAttackedByPlayer(Player attacker) {
      return 8013;
   }

   public boolean hasAttackedMarkForShow(Player observer) {
      return false;
   }

   public int queryPlayerSeeHeader(Player player) {
      int priority = 10;
      if (this.dialog == null) {
         return -1;
      } else {
         List list = this.dialog.getOptionList();

         for(int i = 0; i < list.size(); ++i) {
            DialogOption option = (DialogOption)list.get(i);
            DialogOptionSee see = option.canSee(player, this);
            if (see.isVisible() && option.getType() == DialogOptionType.DOT_TASK) {
               DialogOptionTask to = (DialogOptionTask)option;
               if (to.getSee() == DialogOptionSee.VISIBLE_TASK_SUBMIT) {
                  priority = 1;
                  break;
               }

               if (priority > 2 && to.getSee() == DialogOptionSee.VISIBLE_TASK_VISIT) {
                  priority = 2;
               } else if (priority > 3 && to.getSee() == DialogOptionSee.VISIBLE_TASK_ACCEPT) {
                  priority = 3;
               }
            }
         }

         switch(priority) {
         case 1:
            return TaskConfigManager.NPC_TASK_HEADER[2];
         case 2:
            return TaskConfigManager.NPC_TASK_HEADER[1];
         case 3:
            return TaskConfigManager.NPC_TASK_HEADER[0];
         default:
            return -1;
         }
      }
   }

   public void destroy() {
      if (!this.isDestroy()) {
         this.setDestroy(true);
         this.dialog = null;
         super.destroy();
      }
   }
}
