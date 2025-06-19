package com.mu.io.game.packet.imp.map;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class ChangeBlocks extends WriteOnlyPacket {
   public ChangeBlocks() {
      super(10114);
   }

   public static ChangeBlocks getChangeBlocks(ArrayList blocks, boolean canMove) {
      ChangeBlocks cb = new ChangeBlocks();

      try {
         cb.writeShort(blocks.size());
         Iterator var4 = blocks.iterator();

         while(var4.hasNext()) {
            int[] in = (int[])var4.next();
            cb.writeShort(in[0]);
            cb.writeShort(in[1]);
            cb.writeBoolean(canMove);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return cb;
   }

   public static void changeMapBlocks(Player player, ArrayList blocks, boolean canMove) {
      ChangeBlocks cb = new ChangeBlocks();

      try {
         cb.writeShort(blocks.size());
         Iterator var5 = blocks.iterator();

         while(var5.hasNext()) {
            int[] in = (int[])var5.next();
            cb.writeShort(in[0]);
            cb.writeShort(in[1]);
            cb.writeBoolean(canMove);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      player.writePacket(cb);
      cb.destroy();
      cb = null;
   }
}
