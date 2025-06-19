package com.mu.game.model.unit.player.hang;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.model.SkillModel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HangSkill {
   private Player owner;
   private CopyOnWriteArrayList hangSkills = new CopyOnWriteArrayList();
   private HashSet attackSkills = new HashSet();
   private CopyOnWriteArrayList assistSkills = new CopyOnWriteArrayList();

   public HangSkill(Player owner) {
      this.owner = owner;
   }

   public void setHangSkills(List hangSkills) {
      this.hangSkills.clear();
      this.assistSkills.clear();
      this.attackSkills.clear();
      Iterator var3 = hangSkills.iterator();

      while(var3.hasNext()) {
         Integer skillID = (Integer)var3.next();
         this.setSkillAtom(skillID.intValue());
      }

      this.sortAssistSkill();
   }

   private void sortAssistSkill() {
      for(int i = 0; i < this.assistSkills.size(); ++i) {
         int curSkillID = ((Integer)this.assistSkills.get(i)).intValue();
         int curSkillPriority = SkillModel.getModel(curSkillID).getHangPriority();

         for(int k = i + 1; k < this.assistSkills.size(); ++k) {
            int kSkillID = ((Integer)this.assistSkills.get(k)).intValue();
            int kSkillPriority = SkillModel.getModel(kSkillID).getHangPriority();
            if (curSkillPriority < kSkillPriority) {
               this.assistSkills.set(i, kSkillID);
               this.assistSkills.set(k, curSkillID);
               curSkillPriority = kSkillPriority;
               curSkillID = kSkillID;
            }
         }
      }

   }

   private boolean setSkillAtom(int skillID) {
      if (this.hangSkills.size() >= 12) {
         return false;
      } else if (skillID == -1) {
         return false;
      } else {
         Skill skill = this.owner.getSkillManager().getSkill(skillID);
         if (skill != null && skill.getLevel() >= 1) {
            this.removeSkillAtom(skillID);
            this.hangSkills.add(skillID);
            if (skill.isDeBenefiesSkill()) {
               this.attackSkills.add(skillID);
            } else if (!this.assistSkills.contains(skillID)) {
               this.assistSkills.add(skillID);
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public boolean addSkillWhenLevelUp(int skillID) {
      Iterator var3 = this.hangSkills.iterator();

      while(var3.hasNext()) {
         Integer id = (Integer)var3.next();
         if (skillID == id.intValue()) {
            return false;
         }
      }

      boolean flag = this.setSkillAtom(skillID);
      return flag;
   }

   private boolean removeSkillAtom(int skillID) {
      int index = -1;

      for(int i = 0; i < this.hangSkills.size(); ++i) {
         if (skillID == ((Integer)this.hangSkills.get(i)).intValue()) {
            index = i;
            break;
         }
      }

      if (index != -1) {
         this.hangSkills.remove(index);
      }

      return index != -1;
   }

   public boolean removeHangSkill(int skillID) {
      boolean flag = this.removeSkillAtom(skillID);
      return flag;
   }

   public String getHangSkillStr() {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < this.hangSkills.size(); ++i) {
         sb.append(this.hangSkills.get(i));
         if (i < this.hangSkills.size() - 1) {
            sb.append(",");
         }
      }

      return sb.toString();
   }

   public void destroy() {
      if (this.hangSkills != null) {
         this.hangSkills.clear();
         this.hangSkills = null;
      }

      if (this.attackSkills != null) {
         this.attackSkills.clear();
         this.attackSkills = null;
      }

      if (this.assistSkills != null) {
         this.assistSkills.clear();
         this.assistSkills = null;
      }

   }

   public HashSet getAttackSkills() {
      return this.attackSkills;
   }

   public void setAttackSkills(HashSet attackSkills) {
      this.attackSkills = attackSkills;
   }

   public List getAssistSkills() {
      return this.assistSkills;
   }

   public List getHangSkills() {
      return this.hangSkills;
   }
}
