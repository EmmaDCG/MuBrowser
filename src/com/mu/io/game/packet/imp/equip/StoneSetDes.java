package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.newStone.StoneSet;
import com.mu.game.model.item.container.imp.Equipment;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class StoneSetDes extends WriteOnlyPacket {
   public StoneSetDes() {
      super(20240);
   }

   public static void sendToClient(Player player) {
      try {
         Equipment equipment = player.getEquipment();
         HashMap stoneMap = equipment.getStoneMap();
         List stoneType = StoneSet.getStoneTypeList();
         StoneSetDes ssd = new StoneSetDes();
         writeData(stoneType, stoneMap, ssd);
         List setList = StoneSet.getSetList();
         ssd.writeByte(setList.size());
         Iterator var7 = setList.iterator();

         while(var7.hasNext()) {
            StoneSet ss = (StoneSet)var7.next();
            writeData(stoneType, ss.getTypeMap(), ssd);
            ssd.writeUTF(ss.getStatStr());
            ssd.writeBoolean(ss.isEffect(stoneMap));
         }

         player.writePacket(ssd);
         ssd.destroy();
         ssd = null;
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void writeData(List stoneType, HashMap ownMap, WriteOnlyPacket packet) throws Exception {
      packet.writeByte(stoneType.size());
      Iterator var4 = stoneType.iterator();

      while(var4.hasNext()) {
         Integer type = (Integer)var4.next();
         packet.writeByte(ownMap.containsKey(type) ? ((Integer)ownMap.get(type)).intValue() : 0);
      }

   }
}
