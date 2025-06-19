package com.mu.io.game.packet.imp.drop;

import com.mu.game.model.drop.DropItem;
import com.mu.game.model.equip.forging.ForgingRuleDes;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AroundDropItem extends WriteOnlyPacket {
   public AroundDropItem(List drops, Player player) {
      super(20304);

      try {
         this.writeShort(drops.size());
         Iterator var4 = drops.iterator();

         while(var4.hasNext()) {
            DropItem drop = (DropItem)var4.next();
            this.setData(drop, player);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public AroundDropItem(DropItem drop, Player player) {
      super(20304);

      try {
         this.writeShort(1);
         this.setData(drop, player);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void setData(DropItem drop, Player player) {
      try {
         this.writeDouble((double)drop.getID());
         this.writeInt(drop.getItem().getModelID());
         this.writeInt(drop.getX());
         this.writeInt(drop.getY());
         this.writeShort(drop.getItem().getModel().getDropIcon());
         this.writeUTF(ForgingRuleDes.getDropItemShowName(drop.getItem()));
         this.writeByte(drop.getItem().getQuality());
         this.writeInt(drop.getRemainProtectedForShow(player.getID()));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendToMap(List drops, Player player, boolean self) {
      if (player != null && drops != null) {
         List plist = null;
         if (self) {
            plist = new ArrayList();
            plist.add(player);
         } else {
            plist = player.getMap().getAroundPlayers(player.getActualPosition());
         }

         AroundDropItem ad;
         for(Iterator var5 = plist.iterator(); var5.hasNext(); ad = null) {
            Player p = (Player)var5.next();
            ad = new AroundDropItem(drops, p);
            p.writePacket(ad);
            ad.destroy();
         }

         plist.clear();
         plist = null;
      }
   }
}
