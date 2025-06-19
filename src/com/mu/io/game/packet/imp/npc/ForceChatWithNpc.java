package com.mu.io.game.packet.imp.npc;

import com.mu.io.game.packet.WriteOnlyPacket;

public class ForceChatWithNpc extends WriteOnlyPacket {
   public ForceChatWithNpc(long npcId) {
      super(10408);

      try {
         this.writeDouble((double)npcId);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
