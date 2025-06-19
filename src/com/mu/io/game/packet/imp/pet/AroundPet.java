package com.mu.io.game.packet.imp.pet;

import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatList2Client;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.player.AroundPlayer;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AroundPet extends WriteOnlyPacket {
   public AroundPet(Player player, Pet pet) {
      super(43000);

      try {
         this.writeShort(1);
         this.writePetDetail(player, pet);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public AroundPet(Player player, List list) {
      super(43000);

      try {
         if (list == null || list.isEmpty()) {
            this.writeShort(0);
            return;
         }

         this.writeShort(list.size());
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            Pet pet = (Pet)var4.next();
            this.writePetDetail(player, pet);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void writePetDetail(Player player, Pet pet) throws Exception {
      this.writeDouble((double)pet.getID());
      this.writeUTF(String.format("%s[%s]", pet.getName(), pet.getOwner().getName()));
      this.writeInt(pet.getX());
      this.writeInt(pet.getY());
      this.writeShort(pet.getTemplate().getModelId());
      this.writeShort(pet.getTemplate().getScale());
      this.writeDouble((double)pet.getOwner().getID());
      ArrayList list = pet.getTemplate().getEquipEntry();
      this.writeByte(list.size());
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
         ExternalEntry entry = (ExternalEntry)var5.next();
         this.writeByte(entry.getType());
         this.writeShort(entry.getModelID());
         this.writeShort(entry.getEffectID());
      }

      List stats = StatList2Client.getPetStats();
      this.writeShort(stats.size());
      Iterator var6 = stats.iterator();

      while(var6.hasNext()) {
         StatEnum stat = (StatEnum)var6.next();
         long value = pet.getArributeValue(stat);
         PlayerAttributes.writeStat(stat, value, this);
      }

      AroundPlayer.writeBuffs(pet, true, this);
   }
}
