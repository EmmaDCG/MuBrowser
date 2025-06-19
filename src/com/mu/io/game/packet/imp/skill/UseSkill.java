package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.game.model.unit.unitevent.imp.UseSkillEvent;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.attack.AttackCreature;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.awt.Point;

public class UseSkill extends ReadAndWritePacket {
   public UseSkill(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public UseSkill(int result, int skillID) {
      super(30008, (byte[])null);

      try {
         this.writeBoolean(result == 1);
         this.writeInt(skillID);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int skillID = this.readInt();
      Skill skill = player.getSkillManager().getSkill(skillID);
      if (skill == null) {
         SystemMessage.writeMessage(player, 8007);
      } else {
         int x = player.getX();
         int y = player.getY();
         int type = 1;
         long targetID = -1L;
         Creature target = null;
         SkillModel model = skill.getModel();
         switch(model.getDatum()) {
         case 1:
            type = this.readByte();
            targetID = (long)this.readDouble();
            target = AttackCreature.getAttackTarget(player.getMap(), type, targetID);
         case 2:
         default:
            break;
         case 3:
            x = this.readInt();
            y = this.readInt();
            break;
         case 4:
            type = this.readByte();
            if (type != -1) {
               targetID = (long)this.readDouble();
               target = AttackCreature.getAttackTarget(player.getMap(), type, targetID);
            } else if (model.isGain()) {
               target = player;
            }
         }

         Point point = new Point(x, y);
         if (UseSkillEvent.canDo(player, skill, System.currentTimeMillis())) {
            if (player.isMoving()) {
               player.idle();
               StopMoveWhenUseSkill.sendToClient(player);
            }

            int result = skill.preCastCheck(point, (Creature)target);
            if (result == 1) {
               player.useSkill(skillID, point, (Creature)target, true);
            } else {
               UseSkillEvent.sendToClient(player, skillID, result, true);
            }

         }
      }
   }

   public static void sendToClient(Player player, int skillID, int result) {
      UseSkill us = new UseSkill(result, skillID);
      player.writePacket(us);
      us.destroy();
      us = null;
   }
}
