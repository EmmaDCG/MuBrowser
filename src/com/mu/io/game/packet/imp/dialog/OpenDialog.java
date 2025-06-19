package com.mu.io.game.packet.imp.dialog;

import com.mu.game.model.dialog.Dialog;
import com.mu.game.model.dialog.DialogOption;
import com.mu.game.model.task.TaskData;
import com.mu.game.model.task.TaskDialog;
import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;

public class OpenDialog extends WriteOnlyPacket {
   public OpenDialog() {
      super(10402);
   }

   public static void showNpcDialog(Player player, Npc npc, Dialog dialog, HashMap optionSeeMap) {
      try {
         OpenDialog sd = new OpenDialog();
         sd.writeDouble((double)npc.getID());
         sd.writeUTF(npc.getName());
         sd.writeShort(dialog.getIcon());
         sd.writeUTF(dialog.getContentStr());
         if (optionSeeMap == null) {
            sd.writeByte(0);
         } else {
            sd.writeByte(optionSeeMap.size());
            Iterator it = optionSeeMap.keySet().iterator();

            while(it.hasNext()) {
               DialogOption option = (DialogOption)it.next();
               sd.writeShort(option.getId());
               sd.writeShort(option.getIcon());
               sd.writeUTF(option.getName());
            }
         }

         sd.writeInt(0);
         player.writePacket(sd);
         sd.destroy();
         sd = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public static void showTaskDialog(Player player, Npc npc, TaskData data, TaskDialog dialog) {
      try {
         OpenDialog sd = new OpenDialog();
         sd.writeDouble((double)npc.getID());
         sd.writeUTF(npc.getName());
         sd.writeShort(dialog.getIcon());
         sd.writeUTF(dialog.getContent());
         sd.writeByte(0);
         sd.writeInt(data.getId());
         player.writePacket(sd);
         sd.destroy();
         sd = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
