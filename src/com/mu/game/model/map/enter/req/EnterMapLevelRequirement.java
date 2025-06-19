package com.mu.game.model.map.enter.req;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Tools;

public class EnterMapLevelRequirement implements EnterMapRequirement {
   private int level = 1;
   private String msg = null;
   private int msgId;

   public EnterMapLevelRequirement(int level) {
      this.level = level;
      if (Tools.isMaster(this.level)) {
         this.msgId = 1030;
         this.msg = MessageText.getText(1030).replace("%s%", String.valueOf(Tools.getMasterLevel(this.level)));
      } else {
         this.msgId = 1029;
         this.msg = MessageText.getText(1029).replace("%s%", String.valueOf(this.level));
      }

   }

   public boolean canEnter(Player player, boolean b) {
      if (player.getLevel() < this.level) {
         if (b) {
            SystemMessage.writeMessage(player, this.msg, this.msgId);
         }

         return false;
      } else {
         return true;
      }
   }

   public int getLevel() {
      return this.level;
   }
}
