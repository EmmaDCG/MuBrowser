package com.mu.io.game.packet.imp.buff;

import com.mu.game.model.unit.buff.Buff;
import com.mu.game.model.unit.buff.model.click.ClickPopup;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class ClickBuff extends ReadAndWritePacket {
   public ClickBuff(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int buffID = this.readInt();
      Buff buff = player.getBuffManager().getBuff(buffID);
      if (buff == null) {
         SystemMessage.writeMessage(player, 16001);
      } else if (!buff.getModel().isClickFunction()) {
         SystemMessage.writeMessage(player, 16002);
      } else {
         ClickPopup popup = null;
         boolean eff = buff.isHasEffect();
         if (eff) {
            popup = ClickPopup.getClickPopup(buff.getModel().getAfterClickPopup());
         } else {
            popup = ClickPopup.getClickPopup(buff.getModel().getPriorClickPopup());
         }

         if (popup != null) {
            popup.doClick(player, buff);
         }

      }
   }
}
