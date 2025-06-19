package com.mu.game.dungeon.imp.luolan;

import com.mu.game.CenterManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.material.Material;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.io.game.packet.imp.material.CollectResult;
import java.awt.Point;
import java.util.Iterator;

public class RevivalStatue extends Material {
   private long occupyGang = -1L;
   private String collectText = "";
   private Point findwayPoint = null;

   public RevivalStatue(long id, LuolanMap map, int templateID, String name, int modelID, int x, int y, int[] face, Point fwPoint) {
      super(id, map, templateID, name, modelID);
      this.setCanDisappear(false);
      this.setCollectTime(this.getTemplate().getCollectTime());
      this.setX(x);
      this.setY(y);
      this.setFace(face[0], face[1]);
      this.setFindwayPoint(fwPoint);
   }

   public LuolanMap getLuolanMap() {
      return (LuolanMap)this.getMap();
   }

   public int startGather(Player player) {
      int result = this.doStartGather(player);
      if (result != 1) {
         return result;
      } else {
         return player.getStatusEvent().getStatus() == Status.MOVE ? 10003 : super.startGather(player);
      }
   }

   public int doStartGather(Player player) {
      Gang gang = player.getGang();
      if (gang == null) {
         return 9034;
      } else if (gang.getId() == this.occupyGang) {
         return 9036;
      } else if (this.getLuolanMap().isEnd()) {
         return 9038;
      } else {
         return !this.getLuolanMap().isBegin() ? 9060 : 1;
      }
   }

   public int checkCanClick(Player player) {
      return this.getLuolanMap().isEnd() ? 9038 : 1;
   }

   public String getCollectText() {
      return this.collectText;
   }

   public void setCollectText(String collectText) {
      this.collectText = collectText;
   }

   public String getCountDownName() {
      return this.getCollectText();
   }

   public long getOccupyGang() {
      return this.occupyGang;
   }

   public Point getFindwayPoint() {
      return this.findwayPoint;
   }

   public void setFindwayPoint(Point findwayPoint) {
      this.findwayPoint = findwayPoint;
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
         this.getLuolanMap().refreshBattleInfo();
         this.getLuolanMap().broadcastCenterAndSystem(((LuolanTemplate)((Luolan)this.getLuolanMap().getDungeon()).getTemplate()).getOccupyRevivalStatue().replace("%g%", gang.getName()).replace("%s%", player.getName()));
      }
   }
}
