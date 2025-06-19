package com.mu.game.model.map.event;

import com.mu.game.model.map.Map;
import com.mu.game.model.map.event.req.MapEventRequirement;
import com.mu.game.model.unit.action.Action;
import com.mu.game.model.unit.player.Player;
import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.Element;

public abstract class MapEvent {
   public static int Evevt_AreaCheck = 1;
   public static int Action_Jump = 1;
   public static int Action_EnterDup = 2;
   private ArrayList requirements = new ArrayList();
   protected Map map = null;
   protected long checkRate = 100L;
   protected long lastCheckTime = System.currentTimeMillis();
   protected Action action = null;

   public MapEvent(Map map) {
      this.map = map;
   }

   public abstract int getEventType();

   public abstract void initEvent(Element var1);

   public abstract void check(long var1);

   public void setCheckRate(long checkRate) {
      this.checkRate = checkRate;
   }

   public void setAction(Action action) {
      this.action = action;
   }

   public void doAction(Player player) {
      if (this.action != null) {
         this.action.doAction(player);
      }

   }

   public void addRequirement(MapEventRequirement req) {
      this.requirements.add(req);
   }

   public Map getMap() {
      return this.map;
   }

   public boolean math(Player player) {
      Iterator var3 = this.requirements.iterator();

      while(var3.hasNext()) {
         MapEventRequirement req = (MapEventRequirement)var3.next();
         if (!req.math(player)) {
            return false;
         }
      }

      return true;
   }

   public void destroy() {
      if (this.requirements != null) {
         this.requirements.clear();
         this.requirements = null;
      }

      this.map = null;
      this.action = null;
   }
}
