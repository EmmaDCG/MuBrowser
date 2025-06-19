package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.packet.SkillPacketService;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.SkillManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class SelectSkill extends ReadAndWritePacket {
   public SelectSkill(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public SelectSkill(int skillID) {
      super(30006, (byte[])null);

      try {
         this.writeInt(skillID);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int skillID = this.readInt();
      SkillManager manager = player.getSkillManager();
      Skill skill = manager.getSkill(skillID);
      int result = this.canSelect(player, skill);
      if (result == 1) {
         manager.changeAutoSkill(skillID);
         sendToClient(player, skillID);
         SkillPacketService.noticeGatewayUpdateSkill(player, skill, 2);
      } else if (result != 8029) {
         SystemMessage.writeMessage(player, result);
      }

   }

   private int canSelect(Player player, Skill skill) {
      if (skill == null) {
         return 8007;
      } else if (!player.getShortcut().hasShortCut(2, skill.getSkillID())) {
         return 8030;
      } else if (player.getSkillManager().getAutoSkill() != null && player.getSkillManager().getAutoSkill().getSkillID() == skill.getSkillID()) {
         return 8029;
      } else {
         return !skill.isDeBenefiesSkill() ? 8031 : 1;
      }
   }

   public static void sendToClient(Player player, int skillID) {
      SelectSkill ss = new SelectSkill(skillID);
      player.writePacket(ss);
      ss.destroy();
      ss = null;
   }
}
