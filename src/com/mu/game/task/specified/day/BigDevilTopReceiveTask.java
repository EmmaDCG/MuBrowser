package com.mu.game.task.specified.day;

import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.dungeon.imp.bigdevil.BigDevilSquareTemplate;
import com.mu.game.dungeon.imp.bigdevil.BigDevilTopReward;
import com.mu.game.model.mail.SendMailTask;
import com.mu.game.model.unit.player.Player;
import com.mu.game.top.BigDevilTopInfo;
import com.mu.game.top.DungeonTopManager;
import java.util.concurrent.CopyOnWriteArrayList;

public class BigDevilTopReceiveTask extends DailyTask {
   public BigDevilTopReceiveTask() {
      super(22, 30, 0, 1);
   }

   public void doTask() throws Exception {
      this.doBigDevilReward();
   }

   public static void aa() {
      CopyOnWriteArrayList list = DungeonTopManager.getBigDevilList();
      BigDevilSquareTemplate template = (BigDevilSquareTemplate)DungeonTemplateFactory.getTemplate(6);

      for(int i = 0; i < list.size(); ++i) {
         BigDevilTopInfo info = (BigDevilTopInfo)list.get(i);
         BigDevilTopReward reward = template.getTopReward(i + 1);
         if (reward != null) {
            SendMailTask.sendMail((Player)null, info.getRid(), template.getTopRewardTitle(), template.getTopRewardContent().replace("%s%", String.valueOf(i + 1)), reward.getRewardItemList());
         } else {
            System.out.println("reward is null");
         }

         System.out.println("i = " + i);
      }

   }

   public void doBigDevilReward() {
      CopyOnWriteArrayList list = DungeonTopManager.getBigDevilList();
      BigDevilSquareTemplate template = (BigDevilSquareTemplate)DungeonTemplateFactory.getTemplate(6);

      for(int i = 0; i < list.size(); ++i) {
         BigDevilTopInfo info = (BigDevilTopInfo)list.get(i);
         BigDevilTopReward reward = template.getTopReward(i + 1);
         if (reward != null) {
            SendMailTask.sendMail((Player)null, info.getRid(), template.getTopRewardTitle(), template.getTopRewardContent().replace("%s%", String.valueOf(i + 1)), reward.getRewardItemList());
         }
      }

   }
}
