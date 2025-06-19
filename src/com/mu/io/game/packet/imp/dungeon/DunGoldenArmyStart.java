package com.mu.io.game.packet.imp.dungeon;

import com.mu.io.game.packet.WriteOnlyPacket;

public class DunGoldenArmyStart extends WriteOnlyPacket {
   public DunGoldenArmyStart() {
      super(12013);
   }

   public static DunGoldenArmyStart getDunGoldenArmyStart(String time) {
      DunGoldenArmyStart ds = new DunGoldenArmyStart();

      try {
         ds.writeUTF(time);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return ds;
   }
}
