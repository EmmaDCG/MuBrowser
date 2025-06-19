package com.mu.io.game.packet.imp.monster;

import com.mu.game.CenterManager;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.dungeon.imp.personalboss.BossInfo;
import com.mu.game.dungeon.imp.personalboss.PersonalBossTemplate;
import com.mu.game.model.unit.monster.worldboss.WorldBossData;
import com.mu.game.model.unit.monster.worldboss.WorldBossManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;

public class RefreshSingleBoss extends WriteOnlyPacket {
   public RefreshSingleBoss() {
      super(10304);
   }

   public static void broadcast(WorldBossData data) {
      try {
         Iterator it = CenterManager.getAllPlayerIterator();

         while(it.hasNext()) {
            Player player = (Player)it.next();
            if (!player.isNew()) {
               RefreshSingleBoss pb = new RefreshSingleBoss();
               pb.writeByte(data.getBossType());
               RequestBossInfo.writeWorldBossDetail(data, pb, player);
               player.writePacket(pb);
               pb.destroy();
               pb = null;
            }
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void refreshAllSingleBoss(Player player) {
      try {
         PersonalBossTemplate template = (PersonalBossTemplate)DungeonTemplateFactory.getTemplate(7);
         Iterator it = WorldBossManager.getBossDataMap().values().iterator();

         while(it.hasNext()) {
            WorldBossData data = (WorldBossData)it.next();
            if (data.getBossType() == 3) {
               BossInfo info = template.getBossInfo(data.getBossId());
               singleRefresh(info, player);
            }
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void singleRefresh(BossInfo info, Player player) {
      try {
         RefreshSingleBoss pb = new RefreshSingleBoss();
         pb.writeByte(info.getBossData().getBossType());
         RequestBossInfo.writePersonalBossData(info, pb, player);
         player.writePacket(pb);
         pb.destroy();
         pb = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
