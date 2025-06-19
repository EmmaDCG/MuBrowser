package com.mu.io.game.packet.imp.player;

import com.mu.game.model.properties.NumConversion;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.Map.Entry;

public class PreCalPotential extends ReadAndWritePacket {
   public PreCalPotential(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int size = this.readByte();
      HashMap readMap = new HashMap();

      StatEnum stat;
      for(int i = 0; i < size; i += 2) {
         int statID = this.readShort();
         stat = StatEnum.find(statID);
         int value = this.readInt();
         readMap.put(stat, 0.0F + (float)value);
      }

      NumConversion conversion = NumConversion.getNumConversion(player.getProfessionID());
      HashMap map = null;
      Iterator var16 = readMap.keySet().iterator();

      while(var16.hasNext()) {
         stat = (StatEnum)var16.next();
         SortedMap floatMap = (SortedMap)conversion.getFirstAffectMaps().get(stat);
         if (floatMap == null) {
            SystemMessage.writeMessage(player, 1022);
            return;
         }

         if (map == null) {
            map = new HashMap();
         }

         Iterator it = floatMap.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            float hasValue = (float)((int)((float)player.getStatValue(stat) * ((Float)entry.getValue()).floatValue()));
            float value = (float)player.getStatValue(stat) + ((Float)readMap.get(stat)).floatValue();
            value = ((Float)entry.getValue()).floatValue() * value;
            value -= hasValue;
            value = (float)((int)value);
            map.put((StatEnum)entry.getKey(), value);
         }
      }

      if (map == null) {
         SystemMessage.writeMessage(player, 1022);
      } else {
         map.putAll(readMap);
         this.writeByte(map.size());
         Iterator it = map.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            this.writeShort(((StatEnum)entry.getKey()).getStatId());
            this.writeDouble((double)((Float)entry.getValue()).floatValue());
         }

         player.writePacket(this);
         map.clear();
         map = null;
      }
   }
}
