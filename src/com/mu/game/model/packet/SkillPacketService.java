package com.mu.game.model.packet;

import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.io.game.packet.WriteOnlyPacket;

public class SkillPacketService {
   public static void noticeGatewayUpdateSkill(Player player, Skill skill, int type) {
      WriteOnlyPacket packet = Executor.UpdateSkill.toPacket(player, skill, type);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
