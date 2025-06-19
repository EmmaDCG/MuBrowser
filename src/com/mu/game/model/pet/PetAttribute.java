package com.mu.game.model.pet;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.pet.PetInform;

public class PetAttribute {
   private PetAttributeData data;
   private int level;
   private int value;

   public PetAttribute(PetAttributeData data, int level, int value) {
      this.data = data;
      this.level = level;
      this.value = value;
   }

   public PetAttributeData getData() {
      return this.data;
   }

   public int getLevel() {
      return this.level;
   }

   public int getValue() {
      return this.value;
   }

   public PetAttributeData.Level getLevelData() {
      return this.data.getLevel(this.level);
   }

   public StatEnum getStat() {
      return this.data.getStat();
   }

   public boolean isPercent() {
      PetAttributeData.Level lv = this.getLevelData();
      return lv.getAttributeType() == 3;
   }

   public int getAttributeValue() {
      return this.getLevelData().getAttributeValue();
   }

   public void increaseExp(Player player, int exp, boolean sendMsg) {
      int overExp = this.value + exp - this.getLevelData().getLimit();
      if (overExp >= 0) {
         this.levelUp(player, overExp, sendMsg);
      } else {
         this.value += exp;
         if (sendMsg) {
            PetInform.sendMsgAttribute(player, this, player.getPetManager().getRank());
         }
      }

   }

   public void levelUp(Player player, int overExp, boolean sendMsg) {
      PetAttributeData.Level lv = this.getLevelData();
      PetAttributeData.Level next = lv.getNext();
      if (next != null) {
         this.value = 0;
         this.level = next.getLevel();
         this.increaseExp(player, overExp, false);
         if (sendMsg) {
            player.getPetManager().computerProperties();
            PetInform.sendMsgAttribute(player, this, player.getPetManager().getRank());
            PetInform.sendPetProperties2(player, player.getPetManager().getJoinAttributeMap());
         }

      }
   }

   public void destroy() {
      this.data = null;
   }

   public int getId() {
      return this.data.getId();
   }
}
