package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.attack.CreatureHpChange;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReleaseSkill extends WriteOnlyPacket {
   private static final Logger logger = LoggerFactory.getLogger(ReleaseSkill.class);

   public ReleaseSkill(Player sawer, Skill skill, HashMap results, Point facePoint, Creature seletedTarget) {
      super(30007);

      try {
         Creature owner = skill.getOwner();
         this.writeByte(owner.getType());
         this.writeDouble((double)owner.getID());
         this.writeInt(skill.getSkillID());
         switch(skill.getModel().getDatum()) {
         case 1:
            this.writeInt(seletedTarget.getX());
            this.writeInt(seletedTarget.getY());
            break;
         case 2:
         default:
            this.writeInt(owner.getX());
            this.writeInt(owner.getY());
            break;
         case 3:
            this.writeInt(facePoint.x);
            this.writeInt(facePoint.y);
         }

         if (skill.getSkillID() == 20007) {
            seletedTarget = null;
         }

         this.writeBoolean(seletedTarget != null);
         if (seletedTarget != null) {
            this.writeByte(seletedTarget.getType());
            this.writeDouble((double)seletedTarget.getID());
         }

         this.writeByte(skill.getSkillMoveID());
         this.writeShort(skill.getSkillStep());
         this.writeShort(results.size());
         Iterator var8 = results.entrySet().iterator();

         while(var8.hasNext()) {
            Entry entry = (Entry)var8.next();
            Creature target = (Creature)entry.getKey();
            AttackResult result = (AttackResult)entry.getValue();
            this.writeByte(target.getType());
            this.writeDouble((double)target.getID());
            CreatureHpChange.writeFloat(sawer, target, result, this);
         }
      } catch (Exception var11) {
         System.out.println("技能ID= " + skill.getSkillID());
         var11.printStackTrace();
      }

   }

   public static void sendToClient(Skill skill, HashMap results, Point facePoint, Creature target) {
      HashMap areaMap = new HashMap();
      ReleaseSkill rs = null;
      Creature owner = skill.getOwner();
      if (skill.getOwner() == null) {
         logger.error("owner is null, skill id is {} ,is destroy = {}", skill.getSkillID(), skill.isDestroy());
      } else {
         areaMap.put(owner.getArea(), true);
         Iterator it = results.keySet().iterator();

         Creature creature;
         while(it.hasNext()) {
            creature = (Creature)it.next();
            Rectangle area = creature.getArea();
            if (area != null) {
               areaMap.put(area, true);
            }
         }

         List players = new ArrayList();
         it = owner.getMap().getPlayerMap().values().iterator();

         while(true) {
            while(it.hasNext()) {
               Player player = (Player)it.next();
               Iterator var11 = areaMap.keySet().iterator();

               while(var11.hasNext()) {
                  Rectangle r = (Rectangle)var11.next();
                  if (player.isEnterMap() && r.contains(player.getPosition())) {
                     players.add(player);
                     break;
                  }
               }
            }

            for(Iterator var15 = players.iterator(); var15.hasNext(); rs = null) {
               Player sawer = (Player)var15.next();
               rs = new ReleaseSkill(sawer, skill, results, facePoint, target);
               sawer.writePacket(rs);
               rs.destroy();
            }

            players.clear();
            players = null;
            areaMap.clear();
            areaMap = null;
            return;
         }
      }
   }
}
