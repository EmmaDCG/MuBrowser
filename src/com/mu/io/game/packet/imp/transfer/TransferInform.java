package com.mu.io.game.packet.imp.transfer;

import com.mu.game.model.task.TaskData;
import com.mu.game.model.transfer.Transfer;
import com.mu.game.model.transfer.TransferConfigManager;
import com.mu.game.model.transfer.TransferSkill;
import com.mu.game.model.transfer.TransferStep;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.List;

public class TransferInform extends ReadAndWritePacket {
   public TransferInform(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      sendMsgTransferInform(this.getPlayer());
   }

   public static void sendMsgTransferSuccess(Player player) {
      try {
         TransferAkeyComplete packet = new TransferAkeyComplete(44002, (byte[])null);
         packet.writeBoolean(true);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void sendMsgTransferInform(Player player) {
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
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }
}
