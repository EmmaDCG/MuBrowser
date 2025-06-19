package com.mu.game.dungeon.imp.bigdevil;

import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.task.specified.day.DailyTask;

public class BigDevilTask extends DailyTask {
   public BigDevilTask(int hour, int minute, int second) {
      super(hour, minute, second, 1);
   }

   public void doTask() throws Exception {
      BigDevilSquareTemplate template = (BigDevilSquareTemplate)DungeonTemplateFactory.getTemplate(6);
      template.getBigDevilManager().createBigDevil();
   }
}
