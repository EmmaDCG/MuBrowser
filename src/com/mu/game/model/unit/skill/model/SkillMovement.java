package com.mu.game.model.unit.skill.model;

import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class SkillMovement {
   public static HashMap movements = new HashMap();
   private int profession;
   private int skillID;
   private int weaponMoveType;
   private int skillMoveID;

   private static int createKey(int profession, int skillID, int weaponmoveType) {
      return (profession + 1) * 10000000 + (weaponmoveType + 1) * 1000000 + skillID;
   }

   public static int getSkillMoveID(int profession, int skillID, int weaponmoveType) {
      int key = createKey(profession, skillID, weaponmoveType);
      SkillMovement movement = (SkillMovement)movements.get(key);
      return movement != null ? movement.getSkillMoveID() : -1;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int profession = Tools.getCellIntValue(sheet.getCell("A" + i));
         int skillID = Tools.getCellIntValue(sheet.getCell("B" + i));
         int weaponMoveType = Tools.getCellIntValue(sheet.getCell("C" + i));
         int skillMoveID = Tools.getCellIntValue(sheet.getCell("D" + i));
         SkillMovement movement = new SkillMovement();
         movement.setProfession(profession);
         movement.setSkillID(skillID);
         movement.setSkillMoveID(skillMoveID);
         movement.setWeaponMoveType(weaponMoveType);
         addMovement(movement);
      }

   }

   public static void addMovement(SkillMovement movement) {
      if (movement != null) {
         int key = createKey(movement.getProfession(), movement.getSkillID(), movement.getWeaponMoveType());
         movements.put(key, movement);
      }

   }

   public int getProfession() {
      return this.profession;
   }

   public void setProfession(int profession) {
      this.profession = profession;
   }

   public int getSkillID() {
      return this.skillID;
   }

   public void setSkillID(int skillID) {
      this.skillID = skillID;
   }

   public int getWeaponMoveType() {
      return this.weaponMoveType;
   }

   public void setWeaponMoveType(int weaponMoveType) {
      this.weaponMoveType = weaponMoveType;
   }

   public int getSkillMoveID() {
      return this.skillMoveID;
   }

   public void setSkillMoveID(int skillMoveID) {
      this.skillMoveID = skillMoveID;
   }
}
