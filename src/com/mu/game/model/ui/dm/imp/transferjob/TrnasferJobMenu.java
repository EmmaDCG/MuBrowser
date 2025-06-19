package com.mu.game.model.ui.dm.imp.transferjob;

import com.mu.config.Global;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.transfer.Transfer;
import com.mu.game.model.transfer.TransferConfigManager;
import com.mu.game.model.transfer.TransferSkill;
import com.mu.game.model.transfer.TransferStep;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.transfer.TransferInform;
import java.util.Iterator;
import java.util.List;

public class TrnasferJobMenu extends DynamicMenu {
   public int transferId;

   public TrnasferJobMenu(int menuId, int tid) {
      super(menuId);
      this.transferId = tid;
   }

   public int getShowNumber(Player player) {
      return 0;
   }

   public boolean hasEffect(Player player, int showNumber) {
      return true;
   }

   public boolean isShow(Player player) {
      return Global.isInterServiceServer() ? false : player.canTransfer(this.transferId);
   }

   public void onClick(Player player) {
      try {
         TransferInform packet = new TransferInform(44001, (byte[])null);
         int transfer = player.getProLevel();
         int job = player.getProType();
         Transfer ts = TransferConfigManager.getWillTransfer(job, transfer);
         if (ts == null) {
            return;
         }

         List skillList = ts.getSkillList();
         List stepList = ts.getStepList();
         packet.writeByte(ts.getJob2());
         packet.writeShort(ts.getImage());
         packet.writeBoolean(ts.isAKeyComplete());
         packet.writeByte(skillList.size());
         Iterator it = skillList.iterator();

         while(it.hasNext()) {
            TransferSkill skill = (TransferSkill)it.next();
            packet.writeShort(skill.getIcon());
            packet.writeUTF(skill.getTip());
         }

         packet.writeByte(stepList.size());
         it = stepList.iterator();

         while(it.hasNext()) {
            TransferStep step = (TransferStep)it.next();
            List taskList = step.getTaskList();
            packet.writeByte(taskList.size());
            Iterator itt = taskList.iterator();

            while(itt.hasNext()) {
               TaskData data = (TaskData)itt.next();
               packet.writeInt(data.getId());
            }

            packet.writeUTF(step.getDescription());
         }

         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }
}
