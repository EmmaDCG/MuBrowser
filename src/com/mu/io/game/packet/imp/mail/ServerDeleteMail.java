package com.mu.io.game.packet.imp.mail;

import com.mu.executor.imp.mail.DeleteMailExecutor;
import com.mu.game.model.mail.Mail;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerDeleteMail extends WriteOnlyPacket {
   public ServerDeleteMail(ArrayList list) {
      super(10903);

      try {
         this.writeShort(list.size());
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            long id = ((Long)var4.next()).longValue();
            this.writeDouble((double)id);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void deleteMail(Player player, ArrayList list) {
      ServerDeleteMail sm = new ServerDeleteMail(list);
      player.writePacket(sm);
      sm.destroy();
      sm = null;
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
         long id = ((Long)var5.next()).longValue();
         Mail mail = player.getMailManager().removeMail(id);
         if (mail != null) {
            mail.destroy();
         }
      }

      DeleteMailExecutor.deleteMail(player, list);
   }
}
