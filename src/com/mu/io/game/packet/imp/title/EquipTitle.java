package com.mu.io.game.packet.imp.title;

import com.mu.io.game.packet.ReadAndWritePacket;

public class EquipTitle extends ReadAndWritePacket {
   public EquipTitle(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int tid = this.readUnsignedByte();
      boolean equip = this.readBoolean();
      this.getPlayer().getTitleManager().equipTitle(tid, equip);
   }
}
