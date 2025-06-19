package com.mu.game.model.unit.attack;

import com.mu.game.model.pet.Pet;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;

public class AttackResult {
   public static final int Result_Skill_Type_Other = 0;
   public static final int Result_Skill_Type_POISIONING = -1;
   public static final int Result_Skill_Type_FanTan = -2;
   public static final int Result_Skill_Type_ShareDamage = -3;
   public static final int Result_Skill_Type_Absortb = -4;
   public static final int Result_Skill_Type_FanJi = -5;
   private int type;
   private int damage;
   private int skillId;
   private int effectId = -1;
   private int sdReduce = 0;
   private boolean isCrit = false;
   private long casterID = -1L;
   private int casterType = -1;

   public AttackResult(int type, int damage, int skillId, int effectId, Creature caster) {
      this.type = type;
      this.damage = damage;
      this.skillId = skillId;
      this.effectId = effectId;
      this.setCasterID(caster);
   }

   public static AttackResult createNoneResult(Creature effected, int skillID) {
      return new AttackResult(5, 0, skillID, -1, effected);
   }

   public int getActualDamage() {
      switch(this.type) {
      case 2:
      case 5:
         return 0;
      case 3:
      case 4:
      default:
         return this.damage;
      }
   }

   public void setCasterID(Creature creature) {
      if (creature != null) {
         this.setCasterID(creature.getType(), creature.getID());
      }
   }

   public void setCasterID(int type, long createID) {
      this.casterType = type;
      this.casterID = createID;
   }

   public int isCaster(Player sawer) {
      if (this.casterType == 4) {
         Pet pet = sawer.getPetManager().getActivePet();
         return pet != null && pet.getID() == this.casterID ? 2 : 0;
      } else {
         return this.casterType == 1 && sawer.getID() == this.casterID ? 1 : 0;
      }
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public int getDamage() {
      return this.damage;
   }

   public void setDamage(int damage) {
      this.damage = damage;
   }

   public int getSkillId() {
      return this.skillId;
   }

   public void setSkillId(int skillId) {
      this.skillId = skillId;
   }

   public int getEffectId() {
      return this.effectId;
   }

   public void setEffectId(int effectId) {
      this.effectId = effectId;
   }

   public int getSdReduce() {
      return this.sdReduce;
   }

   public void setSdReduce(int sdReduce) {
      this.sdReduce = sdReduce;
   }

   public boolean isCrit() {
      return this.isCrit;
   }

   public void setCrit(boolean isCrit) {
      this.isCrit = isCrit;
   }

   public long getCasterID() {
      return this.casterID;
   }

   public void setCasterID(long casterID) {
      this.casterID = casterID;
   }

   public int getCasterType() {
      return this.casterType;
   }

   public void setCasterType(int casterType) {
      this.casterType = casterType;
   }
}
