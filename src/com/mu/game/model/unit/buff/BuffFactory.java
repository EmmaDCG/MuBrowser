package com.mu.game.model.unit.buff;

import com.mu.game.model.pet.Pet;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.imp.BuffCapacity;
import com.mu.game.model.unit.buff.imp.BuffCommon;
import com.mu.game.model.unit.buff.imp.BuffIntermittent;
import com.mu.game.model.unit.buff.imp.BuffSprint;
import com.mu.game.model.unit.buff.imp.role.BuffPkColor;
import com.mu.game.model.unit.buff.imp.role.BuffWorldLevel;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuffFactory {
   private static Logger logger = LoggerFactory.getLogger(BuffFactory.class);

   public static Buff getBuff(Creature caster, Creature effected, int modelID, int level) {
      Buff buff = null;
      BuffModel model = BuffModel.getModel(modelID);
      if (model == null) {
         if (buff == null) {
            logger.debug("buff 模板为空 " + modelID);
         }

         return null;
      } else {
         if (model.isDebuff()) {
            if (effected.getType() == 4) {
               logger.debug("宠物不受deBuff影响 " + modelID);
               return null;
            }

            if (effected.isDamageImmunity()) {
               return null;
            }
         }

         if (model.isLevelByCreator()) {
            level = caster.getLevel();
         }

         buff = creatBuff(modelID, level, effected, caster);
         if (buff == null) {
            logger.debug("buff 为空 " + modelID);
         }

         return buff;
      }
   }

   protected static Buff creatBuff(int modelID, int level, Creature owner, Creature caster) {
      BuffModel model = BuffModel.getModel(modelID);
      if (model == null) {
         return null;
      } else {
         Buff buff = null;
         if (owner != null) {
            switch(owner.getType()) {
            case 1:
               buff = getPlayerBuff(modelID, level, (Player)owner, caster);
               break;
            case 2:
               buff = getMonsterBuff(modelID, level, (Monster)owner);
            case 3:
            default:
               break;
            case 4:
               buff = getPetBuff(modelID, level, (Pet)owner);
            }
         }

         if (buff != null) {
            return buff;
         } else {
            switch(model.getBuffType()) {
            case 1:
               return new BuffCommon(modelID, level, owner, caster);
            case 2:
               return new BuffIntermittent(modelID, level, owner, caster);
            case 3:
               return new BuffCapacity(modelID, level, owner, caster);
            case 4:
               return new BuffSprint(modelID, level, owner, caster);
            default:
               return buff;
            }
         }
      }
   }

   private static Buff getPlayerBuff(int modelID, int level, Player owner, Creature caster) {
      Buff buff = null;
      BuffModel model = BuffModel.getModel(modelID);
      switch(modelID) {
      case 80001:
      case 80002:
         return new BuffPkColor(modelID, level, owner, owner);
      default:
         switch(model.getBuffType()) {
         case 5:
            return new BuffWorldLevel(modelID, level, owner, caster);
         default:
            return (Buff)buff;
         }
      }
   }

   private static Buff getPetBuff(int modelID, int level, Pet owner) {
      Buff buff = null;
      BuffModel model = BuffModel.getModel(modelID);
      model.getBuffType();
      return (Buff)buff;
   }

   private static Buff getMonsterBuff(int modelId, int level, Monster owner) {
      Buff buff = null;
      return (Buff)buff;
   }
}
