package com.mu.game.model.unit.action.imp;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.skill.LearnSkill;
import java.util.HashMap;

public class LearnSkillAction extends XmlAction {
   private HashMap skillMap = new HashMap();

   public LearnSkillAction(int id) {
      super(id);
   }

   public void doAction(Player player) {
      Integer skill = (Integer)this.skillMap.get(player.getProType());
      if (skill != null) {
         LearnSkill.learnSkill(player, skill.intValue());
      }

   }

   public void initAction(String value) {
      String[] pro = value.split(";");

      for(int i = 0; i < pro.length; ++i) {
         String[] skills = pro[i].split(",");
         this.skillMap.put(Integer.parseInt(skills[0]), Integer.parseInt(skills[1]));
      }

   }

   public void destroy() {
      if (this.skillMap != null) {
         this.skillMap.clear();
         this.skillMap = null;
      }

   }
}
