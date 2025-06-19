package com.mu.game.dungeon.imp.luolan;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import java.awt.Point;
import java.util.ArrayList;

public class LuolanGate extends LuolanMonster {
   private GateData gd = null;
   private int gateId = -1;
   private Point findwayPoint = null;

   public LuolanGate(GateData md, LuolanMap map) {
      super(md, map);
      this.gd = md;
      this.gateId = md.getId();
      this.findwayPoint = md.getFindwayPoint();
      this.setCanBeAttacked(false);
   }

   public ArrayList getBlockList() {
      return this.gd.getBlockList();
   }

   public Point getFindwayPoint() {
      return this.findwayPoint;
   }

   public GateData getData() {
      return this.gd;
   }

   public int getGateId() {
      return this.gateId;
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      this.getLuoLanMap().gateBeKilled(this);
   }
}
