package com.mu.io.game.packet.imp.mail;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.ListPacket;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMailList extends ReadAndWritePacket {
   public RequestMailList(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      ConcurrentHashMap map = player.getMailManager().getAllMails();
      int size = map.size();
      if (size != 0) {
         AddMail am = new AddMail(map);
         if (size > 20) {
            ListPacket lp = ListPacket.forClient();
            lp.addPacket(am);
            player.writePacket(lp);
            lp.destroy();
            lp = null;
         } else {
            player.writePacket(am);
         }

         am.destroy();
         am = null;
      }
   }
}
