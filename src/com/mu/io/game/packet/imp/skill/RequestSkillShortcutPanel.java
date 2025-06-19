package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.passive.PassiveSkillData;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.mall.OpenShortcutBuyAndUse;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class RequestSkillShortcutPanel extends ReadAndWritePacket {
   public RequestSkillShortcutPanel(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int skillID = this.readInt();
      int key = this.readInt();
      Skill skill = player.getSkillManager().getSkill(skillID);
      if (skill == null) {
         SystemMessage.writeMessage(player, 8007);
      } else if (skill.getLevel() < 1) {
         SystemMessage.writeMessage(player, 8008);
      } else if (!skill.getModel().isPassive()) {
         SystemMessage.writeMessage(player, 8044);
      } else {
         PassiveSkillData data = PassiveSkillData.getData(skillID);
         if (data != null) {
            OpenShortcutBuyAndUse.sendToClient(player, key, data.getShortcutPanelID());
         }
      }
   }
}
