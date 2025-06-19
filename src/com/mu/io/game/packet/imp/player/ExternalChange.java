package com.mu.io.game.packet.imp.player;

import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class ExternalChange extends WriteOnlyPacket {
   public ExternalChange() {
      super(10210);
   }

   public void setData(Player player) {
      try {
         this.writeDouble((double)player.getID());
         ArrayList externalList = player.getCurrentExternal();
         writeRoleExternal(externalList, this);
         externalList.clear();
         externalList = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendToClient(Player player) {
      ExternalChange ec = new ExternalChange();
      ec.setData(player);
      player.getMap().sendPacketToAroundPlayer(ec, player, false);
      ec.destroy();
      ec = null;
   }

   public static void writeRoleExternal(ArrayList list, WriteOnlyPacket packet) throws Exception {
      packet.writeByte(list.size());
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         ExternalEntry entry = (ExternalEntry)var3.next();
         packet.writeByte(entry.getType());
         packet.writeShort(entry.getModelID());
         packet.writeShort(entry.getEffectID());
      }

   }
}
