package com.mu.game.dungeon;

import com.mu.config.Global;
import com.mu.game.dungeon.imp.bigdevil.BigDevilSquareTemplate;
import com.mu.game.dungeon.imp.bloodcastle.BloodCastleTemplate;
import com.mu.game.dungeon.imp.devil.DevilSquareTemplate;
import com.mu.game.dungeon.imp.discovery.DiscoveryTemplate;
import com.mu.game.dungeon.imp.gangboss.GangBossTemplate;
import com.mu.game.dungeon.imp.luolan.LuolanTemplate;
import com.mu.game.dungeon.imp.molian.MoLianTemplate;
import com.mu.game.dungeon.imp.personalboss.PersonalBossTemplate;
import com.mu.game.dungeon.imp.plot.PlotTemplate;
import com.mu.game.dungeon.imp.redfort.RedFortTemplate;
import com.mu.game.dungeon.imp.temple.TempleTemplate;
import com.mu.game.dungeon.imp.trial.TrialTemplate;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import jxl.Workbook;

public class DungeonTemplateFactory {
   private static ConcurrentHashMap templateMap = new ConcurrentHashMap(16, 0.75F, 2);

   public static DungeonTemplate getTemplate(int id) {
      return (DungeonTemplate)templateMap.get(id);
   }

   public static void addTemplate(DungeonTemplate template) {
      templateMap.put(template.getTemplateID(), template);
   }

   public static ConcurrentHashMap getAllTemplate() {
      return templateMap;
   }

   public static void initDungeonTemplate(int id, InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      DungeonTemplate template = createDungeonTemplate(id, wb);
      if (template != null) {
         addTemplate(template);
      }

   }

   public static void startTimingDungeon() {
      if (!Global.isInterServiceServer()) {
         ((RedFortTemplate)getTemplate(5)).getRedFortManager().start();
         ((BigDevilSquareTemplate)getTemplate(6)).getBigDevilManager().start();
      }

   }

   private static DungeonTemplate createDungeonTemplate(int id, Workbook wb) {
      DungeonTemplate template = null;
      switch(id) {
      case 1:
         template = new BloodCastleTemplate(wb);
         break;
      case 2:
         template = new DevilSquareTemplate(wb);
         break;
      case 3:
         template = new TrialTemplate(wb);
         break;
      case 4:
         template = new TempleTemplate(wb);
         break;
      case 5:
         template = new RedFortTemplate(wb);
         break;
      case 6:
         template = new BigDevilSquareTemplate(wb);
         break;
      case 7:
         template = new PersonalBossTemplate(wb);
         break;
      case 8:
         template = new PlotTemplate(wb);
         break;
      case 9:
         template = new LuolanTemplate(wb);
         break;
      case 10:
         template = new GangBossTemplate(wb);
         break;
      case 11:
         template = new MoLianTemplate(wb);
         break;
      case 12:
         template = new DiscoveryTemplate(wb);
      }

      if (template != null) {
         try {
            ((DungeonTemplate)template).init();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return (DungeonTemplate)template;
   }
}
