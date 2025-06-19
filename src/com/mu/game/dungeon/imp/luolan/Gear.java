package com.mu.game.dungeon.imp.luolan;

import com.mu.game.CenterManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.material.CollectResult;
import java.awt.Point;
import java.util.Iterator;

public class Gear extends RevivalStatue {
   private long occupyGang = -1L;

   public Gear(long id, LuolanMap map, int templateID, String name, int modelID, int x, int y, int[] face, Point findwayPoint) {
      super(id, map, templateID, name, modelID, x, y, face, findwayPoint);
   }

   public synchronized void countdownEnd(Player player) {
      this.gathers.remove(player.getID());
      CollectResult.sendResult(player, this.getTemplateID(), false);
      Gang gang = player.getGang();
      if (gang != null) {
         if (gang.getId() != this.occupyGang) {
            this.occupyGang = gang.getId();
         }

         Iterator it = this.gathers.keySet().iterator();

         while(it.hasNext()) {
            Player p = CenterManager.getPlayerByRoleID(((Long)it.next()).longValue());
            if (p != null) {
               p.stopCountDown();
            }
         }

         this.gathers.clear();
         this.getLuolanMap().setGearMan(player);
         this.getLuolanMap().refreshBattleInfo();
         this.getLuolanMap().broadcastCenterAndSystem(((LuolanTemplate)((Luolan)this.getLuolanMap().getDungeon()).getTemplate()).getOccupyGear().replace("%g%", gang.getName()).replace("%s%", player.getName()));
      }
   }

   public long getOccupyGang() {
      return this.occupyGang;
   }

   public void setOccupyGang(long occupyGang) {
      this.occupyGang = occupyGang;
   }

   public int doStartGather(Player player) {
      Gang gang = player.getGang();
      if (gang == null) {
         return 9034;
      } else if (gang.getId() == this.occupyGang) {
         return 9037;
      } else if (this.getLuolanMap().getGearMan() != null) {
         return 9039;
      } else if (this.getLuolanMap().isEnd()) {
         return 9038;
      } else {
         return !this.getLuolanMap().isBegin() ? 9060 : 1;
      }
   }

   public void stopCountDown(Player player) {
      super.stopCountDown(player);
   }
}
