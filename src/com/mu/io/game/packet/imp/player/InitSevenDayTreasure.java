package com.mu.io.game.packet.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;

public class InitSevenDayTreasure extends ReadAndWritePacket {
   public InitSevenDayTreasure(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitSevenDayTreasure(String[] data) {
      super(59010, (byte[])null);

      try {
         this.writeInt(Integer.parseInt(data[0]));
         this.writeUTF(data[1]);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static WriteOnlyPacket initSevenDayTreasure(long roleID) {
      String[] data = PlayerDBManager.searchSevenDayTreasure(roleID);
      WriteOnlyPacket ii = new InitSevenDayTreasure(data);
      return ii;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int loginDays = this.readInt();
      String foundIndexes = this.readUTF();
      player.getSevenManager().init(loginDays, foundIndexes);
   }
}
