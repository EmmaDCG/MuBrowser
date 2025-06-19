package com.mu.game.dungeon.imp.luolan;

import com.mu.game.CenterManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.material.CollectResult;
import java.awt.Point;
import java.util.Iterator;

public class Crown extends RevivalStatue {
   public Crown(long id, LuolanMap map, int templateID, String name, int modelID, int x, int y, int[] face, Point findwayPoint) {
      super(id, map, templateID, name, modelID, x, y, face, findwayPoint);
   }

   public synchronized void countdownEnd(Player player) {
      this.gathers.remove(player.getID());
      CollectResult.sendResult(player, this.getTemplateID(), false);
      Gang gang = player.getGang();
      if (gang != null) {
         ((Luolan)this.getLuolanMap().getDungeon()).addMark(gang.getId());
         Iterator it = this.gathers.keySet().iterator();

         while(it.hasNext()) {
            Player p = CenterManager.getPlayerByRoleID(((Long)it.next()).longValue());
            if (p != null) {
               p.stopCountDown();
            }
         }

         this.gathers.clear();
         this.getLuolanMap().setMarkMan((Player)null);
         this.getLuolanMap().broadcastCenterAndSystem(((LuolanTemplate)((Luolan)this.getLuolanMap().getDungeon()).getTemplate()).getMarkStr().replace("%g%", gang.getName()).replace("%s%", player.getName()));
         if (((Luolan)this.getLuolanMap().getDungeon()).getMarkTimes(gang.getId()) >= ((LuolanTemplate)((Luolan)this.getLuolanMap().getDungeon()).getTemplate()).getMarktimes()) {
            this.getLuolanMap().battleVictory(gang);
         } else {
            this.getLuolanMap().refreshBattleInfo();
         }

      }
   }

   public int doStartGather(Player player) {
      Gang gang = player.getGang();
      if (gang == null) {
         return 9034;
      } else if (this.getLuolanMap().isEnd()) {
         return 9038;
      } else if (gang.getId() != this.getLuolanMap().getGear().getOccupyGang()) {
         return 9040;
      } else if (gang.getMasterId() != player.getID()) {
         return 9041;
      } else if (this.getLuolanMap().getGearMan() != null && this.getLuolanMap().getGearMan().getID() == player.getID()) {
         return 9042;
      } else if (!this.getLuolanMap().isBegin()) {
         return 9060;
      } else {
         this.getLuolanMap().setMarkMan(player);
         this.getLuolanMap().refreshBattleInfo();
         return 1;
      }
   }

   public void stopCountDown(Player player) {
      super.stopCountDown(player);
      this.getLuolanMap().setMarkMan((Player)null);
      this.getLuolanMap().refreshBattleInfo();
   }
}
