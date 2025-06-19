package com.mu.game.dungeon.imp.redfort;

import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.task.specified.day.DailyTask;

public class RedFortTask extends DailyTask {
   public RedFortTask(int hour, int minute, int second) {
      super(hour, minute, second, 1);
   }

   public void doTask() throws Exception {
      RedFortTemplate template = (RedFortTemplate)DungeonTemplateFactory.getTemplate(5);
      template.getRedFortManager().createRedFort();
   }
}
