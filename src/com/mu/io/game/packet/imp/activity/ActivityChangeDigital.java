package com.mu.io.game.packet.imp.activity;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class ActivityChangeDigital extends WriteOnlyPacket {
   public ActivityChangeDigital() {
      super(10807);
   }

   public ActivityChangeDigital(int type, int num) {
      super(10807);

      try {
         this.writeByte(1);
         this.writeByte(type);
         this.writeByte(num);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void pushDigital(Player player, int did, int num) {
      try {
         ActivityChangeDigital ad = new ActivityChangeDigital();
         ad.writeByte(1);
         ad.writeByte(did);
         ad.writeByte(num);
         player.writePacket(ad);
         ad.destroy();
         ad = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void pushDigital(Player player, ArrayList list) {
      if (list != null && list.size() != 0) {
         try {
            ActivityChangeDigital ad = new ActivityChangeDigital();
            ad.writeByte(list.size());
            Iterator var4 = list.iterator();

            while(var4.hasNext()) {
               int[] in = (int[])var4.next();
               ad.writeByte(in[0]);
               ad.writeByte(in[1]);
            }

            player.writePacket(ad);
            ad.destroy();
            ad = null;
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      }
   }
}
