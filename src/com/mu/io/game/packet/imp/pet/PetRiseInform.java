package com.mu.io.game.packet.imp.pet;

import com.mu.game.model.pet.PetItemData;
import com.mu.game.model.pet.PetRank;
import com.mu.game.model.pet.PlayerPetManager;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class PetRiseInform extends ReadAndWritePacket {
   public PetRiseInform(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      PlayerPetManager ppm = null;
      PetRank rank = null;
      if (player != null && (ppm = player.getPetManager()) != null && (rank = ppm.getRank()) != null) {
         this.writePetProperties(rank.getPropertyData(ppm.getLevel()).getPropertyList(), rank.getNext() != null ? rank.getNext().getPropertyData(ppm.getLevel()).getPropertyList() : null);
         this.writePlayerProperties(rank.getProperties(), rank.getNext() != null ? rank.getNext().getProperties() : null);
         GetItemStats.writeItem(((PetItemData)ppm.getRank().getRiseExpendItem().getLast()).getItem(), this);
         this.writeInt(ppm.getLuck());
         this.writeInt(ppm.getRank().getRiseLuckMaxLimit());
         player.writePacket(this);
      }
   }

   private void writePetProperties(LinkedHashMap propertiesList, LinkedHashMap nextPropertiesList) throws IOException {
      this.writeShort(propertiesList.size());
      Iterator it = propertiesList.keySet().iterator();

      while(it.hasNext()) {
         StatEnum key = (StatEnum)it.next();
         FinalModify fm = (FinalModify)propertiesList.get(key);
         PlayerAttributes.writeStat(fm.getStat(), (long)fm.getValue(), fm.isPercent(), this);
         this.writeBoolean(nextPropertiesList != null);
         if (nextPropertiesList != null) {
            FinalModify nextfm = (FinalModify)nextPropertiesList.get(key);
            PlayerAttributes.writeStat(nextfm.getStat(), (long)nextfm.getValue(), nextfm.isPercent(), this);
         }
      }

   }

   private void writePlayerProperties(LinkedHashMap propertiesList, LinkedHashMap nextPropertiesList) throws IOException {
      this.writeShort(propertiesList.size());
      Iterator it = propertiesList.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         PlayerAttributes.writeStat((StatEnum)entry.getKey(), (long)((Integer)entry.getValue()).intValue(), false, this);
         this.writeBoolean(nextPropertiesList != null);
         if (nextPropertiesList != null) {
            Integer nextValue = (Integer)nextPropertiesList.get(entry.getKey());
            PlayerAttributes.writeStat((StatEnum)entry.getKey(), (long)nextValue.intValue(), false, this);
         }
      }

   }
}
