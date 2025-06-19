package com.mu.game.dungeon.imp.molian;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Tools;
import java.util.concurrent.ConcurrentHashMap;

public class MoLianManager {
   private MoLianTemplate template = null;
   private ConcurrentHashMap molianMap = Tools.newConcurrentHashMap2();
   private ConcurrentHashMap infoMap = Tools.newConcurrentHashMap2();

   public static MoLianManager getManager() {
      MoLianTemplate template = (MoLianTemplate)DungeonTemplateFactory.getTemplate(11);
      return template.getManager();
   }

   public MoLianManager(MoLianTemplate template) {
      this.template = template;
   }

   public MoLian getOrCreateMoLian(Player player) {
      Gang gang = player.getGang();
      if (gang == null) {
         return null;
      } else {
         MoLianleLevel ml = this.template.getPlayerFitLevel(player);
         if (ml == null) {
            return null;
         } else {
            ConcurrentHashMap map = (ConcurrentHashMap)this.molianMap.get(gang.getId());
            if (map == null) {
               map = Tools.newConcurrentHashMap2();
               this.molianMap.put(gang.getId(), map);
            }

            MoLian molian = (MoLian)map.get(ml.getLevel());
            if (molian == null) {
               int id = DungeonManager.getID();
               molian = new MoLian(id, this.template, ml);
               molian.init();
               map.put(ml.getLevel(), molian);
               MoLianManager.MoLianInfo mi = new MoLianManager.MoLianInfo(id, gang.getId(), ml.getLevel());
               this.infoMap.put(id, mi);
               DungeonManager.addDungeon(molian);
            }

            return molian;
         }
      }
   }

   public MoLian getMolian(Player player) {
      MoLianleLevel ml = this.template.getPlayerFitLevel(player);
      if (ml == null) {
         return null;
      } else {
         ConcurrentHashMap map = (ConcurrentHashMap)this.molianMap.get(player.getGangId());
         return map == null ? null : (MoLian)map.get(ml.getLevel());
      }
   }

   public void removeMolian(int dunId) {
      MoLianManager.MoLianInfo info = (MoLianManager.MoLianInfo)this.infoMap.remove(dunId);
      if (info != null) {
         ConcurrentHashMap map = (ConcurrentHashMap)this.molianMap.get(info.getGangId());
         if (map != null) {
            map.remove(info.getLevel());
         }
      }

   }

   class MoLianInfo {
      int dunId;
      long gangId;
      int level;

      public MoLianInfo(int dunId, long gangId, int level) {
         this.dunId = dunId;
         this.gangId = gangId;
         this.level = level;
      }

      public int getDunId() {
         return this.dunId;
      }

      public long getGangId() {
         return this.gangId;
      }

      public int getLevel() {
         return this.level;
      }
   }
}
