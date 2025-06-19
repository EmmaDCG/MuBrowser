package com.mu.executor.imp.skill;

import com.mu.db.manager.SkillDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class UpdateSkillExector extends Executable {
   public static final int UpdateType_Learn = 1;
   public static final int UpdateType_Selected = 2;
   public static final int UpdateType_PassiveConsume = 3;

   public UpdateSkillExector(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      SkillDBManager.updateSkill(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      try {
         Player player = (Player)obj[0];
         Skill skill = (Skill)obj[1];
         int type = ((Integer)obj[2]).intValue();
         packet.writeLong(player.getID());
         packet.writeInt(skill.getSkillID());
         packet.writeByte(type);
         switch(type) {
         case 1:
            packet.writeByte(skill.getLevel());
            packet.writeInt(skill.getPassiveConsume());
            break;
         case 2:
            packet.writeBoolean(skill.isSelected());
            break;
         case 3:
            packet.writeInt(skill.getPassiveConsume());
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
