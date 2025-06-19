package com.mu.io.game.packet.imp.item;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ExchangePosition extends ReadAndWritePacket {
   public ExchangePosition(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int containerType = this.readByte();
      int firstPosition = this.readShort();
      int secondPosition = this.readShort();
      player.getItemManager().exchangePosition(containerType, firstPosition, secondPosition);
   }
}
