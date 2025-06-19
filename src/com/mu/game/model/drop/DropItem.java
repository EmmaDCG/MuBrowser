package com.mu.game.model.drop;

import com.mu.game.model.item.Item;
import com.mu.game.model.map.Map;
import com.mu.game.model.team.Team;
import com.mu.game.model.team.Teammate;
import com.mu.game.model.unit.MapUnit;
import com.mu.game.model.unit.Unit;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.drop.CancelProtected;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.utils.Rnd;
import com.mu.utils.geom.MathUtil;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DropItem extends MapUnit {
   private Item item = null;
   private long dropTime;
   private boolean removeProtect = false;
   private HashMap owners = null;
   private int protectedTime = 30000;

   public DropItem(Map map, Item item, int x, int y, Player owner, int dropRule) {
      super(item.getID(), map);
      this.setPosition(x, y);
      this.item = item;
      this.initOwners(owner, dropRule);
   }

   private void initOwners(Player owner, int dropRule) {
      this.owners = new HashMap();
      switch(dropRule) {
      case 2:
         Team team = owner.getCurrentTeam();
         if (team == null) {
            this.owners.put(owner.getID(), true);
         } else {
            Teammate mate;
            if (team.getPickModel() == 1) {
               Iterator var5 = team.getMateList().iterator();

               while(var5.hasNext()) {
                  mate = (Teammate)var5.next();
                  Player p = mate.getPlayer();
                  if (p != null) {
                     this.owners.put(p.getID(), true);
                  }
               }

               return;
            } else {
               List tmpOwners = new ArrayList();
               tmpOwners.add(owner.getID());
               Iterator var10 = team.getMateList().iterator();

               while(var10.hasNext()) {
                  mate = (Teammate)var10.next();
                  if (mate.getPlayer() != null) {
                     Player p = mate.getPlayer();
                     if (p.getID() != owner.getID() && p.getMap() != null && p.getMap().equals(owner.getMap()) && MathUtil.getDistance(p.getActualPosition(), owner.getActualPosition()) <= 5000) {
                        tmpOwners.add(p.getID());
                     }
                  }
               }

               this.owners.put((Long)tmpOwners.get(Rnd.get(0, tmpOwners.size() - 1)), true);
               tmpOwners.clear();
               mate = null;
            }
         }
         break;
      default:
         this.owners.put(owner.getID(), true);
      }

   }

   public void andToMapAndEffect() {
      this.getMap().addDropItem(this);
      this.dropTime = System.currentTimeMillis();
   }

   public void doWork(Map map, long now) {
      long tmpTime = now - this.dropTime;
      if (tmpTime >= 120000L) {
         this.disappear();
      } else {
         if (!this.isRemoveProtect() && tmpTime >= (long)this.getProtectedTime()) {
            this.setRemoveProtecte();
            CancelProtected.sendToClient(this);
         }

      }
   }

   public void disappear() {
      this.sendDisappear();
      this.destroy();
   }

   public void sendDisappear() {
      RemoveUnit ru = new RemoveUnit(new Unit[]{this});
      this.getMap().sendPacketToAroundPlayer(ru, this.getActualPosition());
      ru.destroy();
      ru = null;
   }

   public void setRemoveProtecte() {
      this.removeProtect = true;
      this.owners.clear();
   }

   public void setProtectedTime(int protectedTime) {
      this.protectedTime = protectedTime;
      if (this.protectedTime < 1) {
         this.setRemoveProtecte();
      }

   }

   public void pickout(Player player) {
      this.getMap().pickUpItem(this, player);
   }

   public int getEffectID() {
      return 0;
   }

   public int getRemainProtectedForShow(long roleID) {
      if (this.owners.containsKey(roleID)) {
         return 0;
      } else {
         long now = System.currentTimeMillis();
         long remainTime = now - this.dropTime;
         remainTime = (long)this.getProtectedTime() - remainTime;
         return remainTime < 0L ? 0 : (int)(remainTime % 1000L == 0L ? remainTime / 1000L : remainTime / 1000L + 1L);
      }
   }

   public void destroy() {
      this.item = null;
      if (this.owners != null) {
         this.owners.clear();
      }

      this.owners = null;
      super.destroy();
   }

   public void switchArea(Rectangle newArena, Rectangle oldArea) {
   }

   public WriteOnlyPacket createAroundSelfPacket(Player viewer) {
      return null;
   }

   public int getType() {
      return 5;
   }

   public HashMap getOwners() {
      return this.owners;
   }

   public void setOwners(HashMap owners) {
      this.owners = owners;
   }

   public int getProtectedTime() {
      return this.protectedTime;
   }

   public Item getItem() {
      return this.item;
   }

   public long getDropTime() {
      return this.dropTime;
   }

   public boolean isRemoveProtect() {
      return this.removeProtect;
   }
}
