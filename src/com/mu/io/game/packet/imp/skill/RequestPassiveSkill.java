package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.passive.PassiveSkillData;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class RequestPassiveSkill extends ReadAndWritePacket {
   public RequestPassiveSkill(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public RequestPassiveSkill() {
      super(30012, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int skillID = this.readInt();
      Skill skill = player.getSkillManager().getSkill(skillID);
      if (skill == null) {
         SystemMessage.writeMessage(player, 8007);
      } else if (skill.getLevel() < 1) {
         SystemMessage.writeMessage(player, 8008);
      } else if (!skill.getModel().isPassive()) {
         SystemMessage.writeMessage(player, 8044);
      } else {
         sendClient(player, skill);
      }
   }

   public static void sendClient(Player player, Skill skill) {
      try {
         PassiveSkillData data = PassiveSkillData.getData(skill.getSkillID());
         if (data == null) {
            return;
         }

         RequestPassiveSkill rps = new RequestPassiveSkill();
         rps.writeInt(skill.getSkillID());
         rps.writeShort(data.getStatID().getStatId());
         rps.writeDouble((double)skill.getMaxPassiveConsume());
         rps.writeDouble((double)skill.getPassiveConsume());
         rps.writeUTF(data.getDes());
         player.writePacket(rps);
         rps.destroy();
         rps = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
