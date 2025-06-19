package com.mu.game.model.stats.statId;

public class StatIdCreator {
   private static int create(int statType, int selfType) {
      return statType * 10000000 + selfType + 10;
   }

   public static int getType(int statId) {
      return statId / 10000000;
   }

   public static int createItemStatId(int slotId) {
      return create(1, slotId);
   }

   public static int createHorseStatId(int type) {
      return create(3, type);
   }

   public static int createSkillStatId(int skillId) {
      return create(2, skillId);
   }

   public static int createProtectionId() {
      return create(4, 1);
   }

   public static int createProtectionSkillId() {
      return create(5, 1);
   }

   public static int createTalentID() {
      return create(6, 1);
   }

   public static int createEquipmentID() {
      return create(7, 1);
   }

   public static int createWingId(int type) {
      return create(8, type);
   }

   public static int createPetId(int type) {
      return create(11, type);
   }

   public static int createShieldId(int type) {
      return create(12, type);
   }

   public static int createBuffId(int buffModelID) {
      return create(9, buffModelID);
   }

   public static int createRuneId(int runeID) {
      return create(10, runeID);
   }

   public static int createTitleID() {
      return create(13, 1);
   }

   public static int createTopRewardID(int topId) {
      return create(14, topId);
   }

   public static int createSpiritID() {
      return create(15, 0);
   }

   public static int createHallowID() {
      return create(16, 0);
   }
}
