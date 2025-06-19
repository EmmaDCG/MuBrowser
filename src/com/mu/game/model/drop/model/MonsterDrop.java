package com.mu.game.model.drop.model;

import com.mu.game.model.drop.weight.DropWeightElement;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Rnd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonsterDrop extends DropWeightElement {
   private static Logger logger = LoggerFactory.getLogger(MonsterDrop.class);
   private int type = 1;
   private int loopCount = 1;
   private List rateAtoms = null;

   public MonsterDrop(int loopCount, String dropStr, String dropSign) throws Exception {
      this.loopCount = loopCount;
      this.rateAtoms = new ArrayList();
      boolean c = this.initDropAtoms(dropStr, dropSign);
      if (!c) {
         throw new Exception("怪物掉落 有错  ，标志 = " + dropSign);
      }
   }

   private boolean initDropAtoms(String dropString, String dropSign) {
      if (dropString != null && dropString.trim().length() >= 1) {
         String[] groupSplits = dropString.split(";");
         String[] var7 = groupSplits;
         int var6 = groupSplits.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            String groupStr = var7[var5];
            String[] idcounts = groupStr.trim().split(",");
            if (idcounts.length < 5) {
               logger.error("掉落配置数据有误  标记  = {},字符串 = {}", dropSign, dropString);
               return false;
            }

            int groupID = Integer.parseInt(idcounts[0]);
            int statRuleID = Integer.parseInt(idcounts[1]);
            long weight = Long.parseLong(idcounts[2]);
            boolean isBind = Integer.parseInt(idcounts[3]) == 1;
            boolean isRate = Integer.parseInt(idcounts[4]) == 1;
            int controlID = -1;
            if (idcounts.length > 5) {
               controlID = Integer.parseInt(idcounts[5]);
            }

            if (!DropGroup.hasGroup(groupID)) {
               logger.error("掉落组不存在 掉落ID = {},  标记  = {},字符串 = {}", new Object[]{groupID, dropSign, dropString});
               return false;
            }

            if (weight <= 0L) {
               logger.error("掉落权重有误， 标记  = {},字符串 = {}", dropSign, dropString);
               return false;
            }

            MDropAtom atom = new MDropAtom(groupID, weight, isBind, isRate, controlID, statRuleID);
            if (isRate) {
               this.rateAtoms.add(atom);
            } else {
               this.addAtom(atom);
            }
         }

         if (this.getAtoms().size() > 0) {
            this.sortByWeight("掉落-" + dropSign);
         }

         if (!this.checkWeight()) {
            logger.error("掉落总权重有误，  标记  =(" + dropSign + "),字符串 = (" + dropString + ")");
            return false;
         } else if (this.getAtoms().size() < 1 && this.rateAtoms.size() < 1) {
            logger.error("掉落组没有数据，  标记  = {},字符串 = {}", dropSign, dropString);
            return false;
         } else if (this.getAtoms().size() > 0 && this.loopCount < 1) {
            logger.error("掉落轮询次数有误，  标记  = {},字符串 = {}", dropSign, dropString);
            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public void getDropItems(Player player, HashMap itemList, int templateID) {
      MDropAtom atom;
      Iterator var5;
      label42:
      switch(this.type) {
      case 2:
         var5 = this.getAtoms().iterator();

         while(true) {
            if (!var5.hasNext()) {
               break label42;
            }

            atom = (MDropAtom)var5.next();
            this.createDropItem(player, itemList, this.type, atom, templateID);
         }
      default:
         if (this.getAtoms().size() > 0) {
            for(int tmpPollCount = this.loopCount; tmpPollCount > 0; --tmpPollCount) {
               this.createDropItem(player, itemList, this.type, this.getRndAtom(), templateID);
            }
         }
      }

      if (this.rateAtoms.size() > 0) {
         var5 = this.rateAtoms.iterator();

         while(var5.hasNext()) {
            atom = (MDropAtom)var5.next();
            long weight = atom.getWeight();
            if (DropControlManager.isInRateControl(atom.getGroupID())) {
               weight = weight * (long)(100000 + player.getStatValue(StatEnum.DROPRATE)) / 100000L;
            }

            if (Rnd.get(0L, 1000000000L) < weight) {
               this.createDropItem(player, itemList, this.type, atom, templateID);
            }
         }
      }

   }

   private void createDropItem(Player player, HashMap itemMap, int type, MDropAtom atom, int templateID) {
      if (atom.isRate()) {
         boolean canDrop = DropControlManager.canDrop(atom.getControlID(), player, templateID);
         if (!canDrop) {
            return;
         }
      }

      DropGroup group = DropGroup.getGroup(atom.getGroupID());
      group.createDropItems(itemMap, player, type, atom.isBind(), atom.getProtectedTime(), atom.getStatRuleID(), templateID);
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }
}
