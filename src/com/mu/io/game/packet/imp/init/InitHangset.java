package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.HangDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.hang.HangDBEntry;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.ListPacket;

public class InitHangset extends ReadAndWritePacket {
   public InitHangset(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static void addHangset(ListPacket lp, long roleID) {
      try {
         HangDBEntry entry = HangDBManager.searchHangset(roleID);
         if (entry != null) {
            InitHangset ihs = new InitHangset(entry);
            lp.addPacket(ihs);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public InitHangset(HangDBEntry entry) {
      super(59008, (byte[])null);

      try {
         this.writeUTF(entry.getCycles());
         this.writeUTF(entry.getPickupOthers());
         this.writeUTF(entry.getQualities());
         this.writeUTF(entry.getHangSkills());
         this.writeUTF(entry.getHangSales());
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      String cycles = this.readUTF();
      String pickupOthers = this.readUTF();
      String qualities = this.readUTF();
      String hangSkills = this.readUTF();
      String hangSales = this.readUTF();
      player.getGameHang().load(cycles, pickupOthers, qualities, hangSkills, hangSales);
   }
}
