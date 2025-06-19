package com.mu.executor.imp.skill;

import com.mu.db.manager.SkillDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.SkillManager;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SaveSkillWhenOffLineExecutor extends Executable {
   public SaveSkillWhenOffLineExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      SkillDBManager.saveWhenOffLine(packet);
   }

   public static boolean needSaveSkills(Player player) {
      SkillManager manager = player.getSkillManager();
      Iterator var3 = manager.getSkillMap().values().iterator();

      Skill skill;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         skill = (Skill)var3.next();
      } while(skill.isCommonSkill() || skill.getLevel() < 1 || skill.getModel().getType() == 4);

      return true;
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      try {
         Player player = (Player)obj[0];
         SkillManager manager = player.getSkillManager();
         List skillList = new ArrayList();
         Iterator var7 = manager.getSkillMap().values().iterator();

         Skill skill;
         while(var7.hasNext()) {
            skill = (Skill)var7.next();
            if (!skill.isCommonSkill() && skill.getLevel() >= 1 && skill.getModel().getType() != 4) {
               skillList.add(skill);
            }
         }

         packet.writeLong(player.getID());
         packet.writeByte(skillList.size());
         var7 = skillList.iterator();

         while(var7.hasNext()) {
            skill = (Skill)var7.next();
            packet.writeInt(skill.getSkillID());
            packet.writeInt(skill.getRemainTime());
         }

         skillList.clear();
         skillList = null;
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }
}
