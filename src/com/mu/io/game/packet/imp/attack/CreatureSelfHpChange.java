package com.mu.io.game.packet.imp.attack;

import com.mu.io.game.packet.WriteOnlyPacket;

public class CreatureSelfHpChange extends WriteOnlyPacket {
   public CreatureSelfHpChange(int code) {
      super(code);
   }
}
