package com.mu.io.game.packet.imp.dungeon;

import com.mu.io.game.packet.WriteOnlyPacket;

public class DungeonInfoUpdate extends WriteOnlyPacket {
   public DungeonInfoUpdate() {
      super(12006);
   }
}
