package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.SkillDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.SkillManager;
import com.mu.game.model.unit.skill.manager.SkillFactory;
import com.mu.game.model.unit.skill.model.ProfessionSkills;
import com.mu.game.model.unit.skill.model.SkillDBEntry;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class InitSkills extends ReadAndWritePacket {
   public InitSkills(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static WriteOnlyPacket initSkill(long roleID) {
      List skillList = SkillDBManager.searchSkills(roleID);
      WriteOnlyPacket ii = new InitSkills(skillList);
      skillList.clear();
      return ii;
   }

   public InitSkills(List entries) {
      super(59002, (byte[])null);

      try {
         this.writeByte(entries.size());
         Iterator var3 = entries.iterator();

         while(var3.hasNext()) {
            SkillDBEntry entry = (SkillDBEntry)var3.next();
            this.writeInt(entry.getSkillID());
            this.writeByte(entry.getLevel());
            this.writeInt(entry.getRemainThawTime());
            this.writeBoolean(entry.isSelected());
            this.writeInt(entry.getPassiveConsume());
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      SkillManager manager = player.getSkillManager();
      int size = this.readByte();

      for(int i = 0; i < size; ++i) {
         SkillDBEntry entry = new SkillDBEntry();
         entry.setSkillID(this.readInt());
         entry.setLevel(this.readByte());
         entry.setRemainThawTime(this.readInt());
         entry.setSelected(this.readBoolean());
         entry.setPassiveConsume(this.readInt());
         manager.loadSkill(entry);
      }

      HashSet skills = ProfessionSkills.getProfessionSkill(player.getProType());
      Iterator var6 = skills.iterator();

      while(var6.hasNext()) {
         Integer skillID = (Integer)var6.next();
         if (!manager.hasSkill(skillID.intValue())) {
            int level = 0;
            if (ProfessionSkills.isCommonSkill(skillID.intValue())) {
               level = 1;
            }

            Skill skill = SkillFactory.createSkill(skillID.intValue(), level, player);
            if (skill != null) {
               manager.addSkill(skill);
               if (skill.getModel().isPassive()) {
                  skill.useSkill(true, player.getPosition(), player);
               }
            }
         }
      }

   }
}
