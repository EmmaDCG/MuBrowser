package com.mu.game.model.equip.master;

import com.mu.game.model.weight.WeightAtom;
import java.util.HashMap;

public class MasterSkillAtom extends WeightAtom {
   private static HashMap atomMap = new HashMap();
   private int atomID;
   private int skillID;
   private int addLevel;

   public MasterSkillAtom(int weight) {
      super(weight);
   }

   public static MasterSkillAtom getMasterAtom(int atomID) {
      return (MasterSkillAtom)atomMap.get(atomID);
   }

   public static boolean hasMasterAtom(int atomID) {
      return atomMap.containsKey(atomID);
   }

   public int getSkillID() {
      return this.skillID;
   }

   public void setSkillID(int skillID) {
      this.skillID = skillID;
   }

   public int getAddLevel() {
      return this.addLevel;
   }

   public void setAddLevel(int addLevel) {
      this.addLevel = addLevel;
   }

   public int getAtomID() {
      return this.atomID;
   }
}
