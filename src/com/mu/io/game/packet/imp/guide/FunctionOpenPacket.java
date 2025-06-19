package com.mu.io.game.packet.imp.guide;

import com.mu.game.model.fo.FunctionOpen;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.fo.FunctionOpenStruct;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class FunctionOpenPacket extends WriteOnlyPacket {
   public FunctionOpenPacket() {
      super(10019);
   }

   public static void pushRoleFunctionOpen(Player player) {
      try {
         ArrayList list = FunctionOpenManager.getPlayerFunctionOpen(player);
         FunctionOpenPacket oc = new FunctionOpenPacket();
         oc.writeByte(list.size());
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            FunctionOpenStruct fs = (FunctionOpenStruct)var4.next();
            oc.writeByte(fs.getId());
            oc.writeBoolean(fs.isOpen());
            oc.writeShort(fs.getIcon());
            oc.writeUTF(fs.getDes());
         }

         player.writePacket(oc);
         oc.destroy();
         oc = null;
         list.clear();
         list = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void open(FunctionOpen fo, Player player) {
      try {
         FunctionOpenPacket oc = new FunctionOpenPacket();
         oc.writeByte(1);
         oc.writeByte(fo.getId());
         oc.writeBoolean(true);
         oc.writeShort(fo.getIcon());
         oc.writeUTF(fo.getName());
         player.writePacket(oc);
         oc.destroy();
         oc = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
