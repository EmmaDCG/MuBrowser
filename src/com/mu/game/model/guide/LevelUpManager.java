package com.mu.game.model.guide;

import com.mu.config.Global;
import com.mu.db.log.global.GlobalRoleLog;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.fp.FunctionPreview;
import com.mu.game.model.fp.FunctionPreviewManager;
import com.mu.game.model.top.WorldLevelManager;
import com.mu.game.model.ui.dm.DynamicMenuManager;
import com.mu.game.model.unit.action.Action;
import com.mu.game.model.unit.action.ActionFactory;
import com.mu.game.model.unit.action.DelayAction;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.schedule.log.GlobalRoleLogTask;
import com.mu.io.game.packet.imp.gang.UpdateGangMember;
import com.mu.io.game.packet.imp.guide.FunctionPreviewUpdate;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class LevelUpManager {
   private static ConcurrentHashMap levelUpActionMap = new ConcurrentHashMap(8, 0.75F, 2);

   public static void initAction(InputStream in) throws Exception {
      SAXBuilder sb = new SAXBuilder();
      Document doc = sb.build(in);
      Element root = doc.getRootElement();
      List completeList = root.getChildren("levelup");
      Iterator var6 = completeList.iterator();

      while(var6.hasNext()) {
         Element element = (Element)var6.next();
         int level = element.getAttribute("level").getIntValue();
         Action action = ActionFactory.createAction(level, element.getChild("action"));
         DelayAction ta = new DelayAction(action);
         ArrayList list = (ArrayList)levelUpActionMap.get(level);
         if (list == null) {
            list = new ArrayList();
            levelUpActionMap.put(level, list);
         }

         list.add(ta);
         Attribute delayAttr = element.getAttribute("delay");
         if (delayAttr != null) {
            ta.setDelayTime(delayAttr.getLongValue());
         }
      }

   }

   public static void levelUp(Player player, int oldLevel, int newLevel) {
      for(int i = oldLevel + 1; i <= newLevel; ++i) {
         ArrayList list = (ArrayList)levelUpActionMap.get(i);
         if (list != null) {
            Iterator var6 = list.iterator();

            while(var6.hasNext()) {
               DelayAction ta = (DelayAction)var6.next();
               ta.doAction(player);
            }
         }

         if (Global.getLoginParser().needPostWhenLevelUp(player)) {
            Global.getLoginParser().doRoleLevelUp(player);
         }
      }

      ActivityManager.refreshWhenLevelUp(player);
      WorldLevelManager.createWorldLevelBuff(player);
      FunctionPreview fp;
      if (player.getFinishPreview() < 1) {
         fp = FunctionPreviewManager.getFirstPrivew();
         if (fp.getOpenLevel() <= player.getLevel()) {
            FunctionPreviewUpdate.pushPreview(player, false);
         }
      } else {
         fp = FunctionPreviewManager.getLastPrivew();
         if (fp.getId() != player.getFinishPreview()) {
            FunctionPreviewUpdate.pushPreview(player, false);
         }
      }

      GlobalRoleLogTask.addRoleLog(GlobalRoleLog.createLog(player));
      UpdateGangMember.updateGangMember(player.getID(), player.getGang());
      DynamicMenuManager.checkWhenLevelUp(player, oldLevel, newLevel);
   }
}
