package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.SpiritDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;

public class InitSpirit extends ReadAndWritePacket {
   public InitSpirit(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static WriteOnlyPacket getInitSpirit(long roleID) {
      String[] entries = SpiritDBManager.searchSpirit(roleID);
      WriteOnlyPacket packet = new InitSpirit(entries);
      return packet;
   }

   public InitSpirit(String[] entries) {
      super(59011, (byte[])null);

      try {
         this.writeByte(Integer.parseInt(entries[0]));
         this.writeShort(Integer.parseInt(entries[1]));
         this.writeLong((long)Integer.parseInt(entries[2]));
         this.writeInt(Integer.parseInt(entries[3]));
         this.writeUTF(entries[4]);
         this.writeUTF(entries[5]);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int rank = this.readByte();
      int level = this.readShort();
      long exp = this.readLong();
      int ingotRefineCount = this.readInt();
      String itemCountStr = this.readUTF();
      String conStr = this.readUTF();
      player.getSpiritManager().init(rank, level, exp, ingotRefineCount, itemCountStr, conStr);
   }
}
