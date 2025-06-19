package com.mu.game.dungeon.imp.bloodcastle;

import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.map.NpcInfo;
import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.player.Player;
import java.util.Iterator;
import java.util.List;

public class Archangel extends Npc {
   public Archangel(long id, NpcInfo info, BloodCastleMap map) {
      super(id, map);
      this.setName(info.getName());
      this.setLevel(400);
      this.setX(info.getX());
      this.setY(info.getY());
      this.setTemplateId(info.getTemplateId());
      this.setFace(info.getFace()[0], info.getFace()[1]);
   }

   public BloodCastleMap getBloodCastleMap() {
      return (BloodCastleMap)this.getMap();
   }

   public void onDialogRequest(Player player) {
      BloodCastleMap map = this.getBloodCastleMap();
      if (!((BloodCastle)map.getDungeon()).isSuccess() && !((BloodCastle)map.getDungeon()).isComplete()) {
         if (map.getSchedule() == 7 && !((BloodCastle)map.getDungeon()).isSuccess()) {
            List list = map.getAngelWeapoinList();
            if (list != null) {
               Iterator var5 = list.iterator();

               while(var5.hasNext()) {
                  ItemDataUnit unit = (ItemDataUnit)var5.next();
                  if (player.getBackpack().hasEnoughItem(unit.getModelID(), 1)) {
                     map.doSuccess();
                     return;
                  }
               }
            }
         }

      }
   }
}
