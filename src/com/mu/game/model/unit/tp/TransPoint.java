package com.mu.game.model.unit.tp;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.MapUnit;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.tp.req.TranspointRequirement;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.map.AroundTransPoint;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class TransPoint extends MapUnit {
   public static final int TP_RING = 5;
   public static final int CMD_SwitchMap = 1;
   private int cmd = 1;
   private ArrayList reqList = null;
   public static int transPointID = 10000;
   private Map targetMap = null;
   private int targetX;
   private int targetY;
   private int targetMapID = -1;
   private int worldX = 0;
   private int worldY = 0;

   public TransPoint(long id, Map currentMap, int targetMapID, int x, int y, int targetX, int targetY, String name, int worldX, int worldY) {
      super(id, currentMap);
      this.targetMapID = targetMapID;
      this.setPosition(x, y);
      this.targetX = targetX;
      this.targetY = targetY;
      this.setName(name);
      this.worldX = worldX;
      this.worldY = worldY;
   }

   public TransPoint(long id, Map currentMap, Map targetMap, int x, int y, int targetX, int targetY, String name, int worldX, int worldY) {
      super(id, currentMap);
      this.targetMap = targetMap;
      this.setPosition(x, y);
      this.targetX = targetX;
      this.targetY = targetY;
      this.setName(name);
      this.worldX = worldX;
      this.worldY = worldY;
      this.targetMapID = this.targetMap.getID();
   }

   public int excute(Player p) {
      int result = this.canTrans(p);
      if (result != 1) {
         return result;
      } else {
         switch(this.cmd) {
         case 1:
            return this.toAnotherMap(p);
         default:
            return -1;
         }
      }
   }

   private int toAnotherMap(Player p) {
      if (this.targetMap == null) {
         p.switchMap(this.targetMapID, new Point(this.targetX, this.targetY));
         return 1;
      } else {
         p.switchMap(this.targetMap, new Point(this.targetX, this.targetY));
         return 1;
      }
   }

   public void initRequirement(String reqStr, int targetMapID) {
   }

   public int canTrans(Player p) {
      return 1;
   }

   public void addRequirement(TranspointRequirement req) {
      if (this.reqList == null) {
         this.reqList = new ArrayList();
      }

      this.reqList.add(req);
   }

   public int getCmd() {
      return this.cmd;
   }

   public Map getTargetMap() {
      return this.targetMap;
   }

   public int getTargetX() {
      return this.targetX;
   }

   public int getTargetY() {
      return this.targetY;
   }

   public void setCmd(int cmd) {
      this.cmd = cmd;
   }

   public void destroy() {
      if (!this.isDestroy()) {
         if (this.reqList != null) {
            this.reqList.clear();
            this.reqList = null;
         }

         super.destroy();
      }
   }

   public int getWorldX() {
      return this.worldX;
   }

   public int getWorldY() {
      return this.worldY;
   }

   public void switchArea(Rectangle newArena, Rectangle oldArea) {
   }

   public WriteOnlyPacket createAroundSelfPacket(Player viewer) {
      return new AroundTransPoint(this);
   }

   public int getType() {
      return 6;
   }
}
