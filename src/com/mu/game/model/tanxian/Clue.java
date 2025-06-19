package com.mu.game.model.tanxian;

import com.mu.game.IDFactory;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.map.Map;
import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskState;
import com.mu.game.model.unit.material.Material;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.material.CollectResult;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.io.game.packet.imp.tanxian.ShowRewardItem;
import com.mu.utils.Rnd;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Clue extends Material {
   private ClueInfo clueInfo = null;

   public Clue(ClueInfo info, Map map) {
      super(IDFactory.getTemporaryID(), map, info.getTemplateId(), info.getName(), info.getModelId());
      this.clueInfo = info;
      this.setCollectTime(info.getTemplate().getCollectTime());
      this.setX(info.getX());
      this.setY(info.getY());
   }

   public int getRefreshTime() {
      return -1;
   }

   public boolean isCanDisappear() {
      return false;
   }

   public int startGather(Player player) {
      if (this.isDisappear) {
         return 10001;
      } else if (!this.checkTask(player)) {
         return 10003;
      } else {
         this.gathers.put(player.getID(), true);
         return 1;
      }
   }

   public int doReward(Player player) {
      if (!this.checkTask(player)) {
         return 10003;
      } else {
         ArrayList rList = new ArrayList();
         Iterator var4 = this.clueInfo.getUnitList().iterator();

         while(var4.hasNext()) {
            ItemDataUnit unit = (ItemDataUnit)var4.next();
            rList.add(unit.cloneUnit());
         }

         OperationResult or = player.getItemManager().addItem((List)rList);
         return or.isOk() ? 1 : or.getResult();
      }
   }

   private boolean checkTask(Player player) {
      PlayerTaskManager manager = player.getTaskManager();
      Task task = manager.getCurTXTask();
      return task != null && task.getId() == this.getClueInfo().getTaskId() && task.is(TaskState.RUN);
   }

   public boolean canClick(Player player) {
      return this.checkTask(player);
   }

   public synchronized void countdownEnd(Player player) {
      int tmp = Rnd.get(100);
      int result = 1;
      boolean enterDun = false;
      if (tmp > this.clueInfo.getDunRate()) {
         result = this.doReward(player);
      } else {
         enterDun = true;
      }

      if (result == 1) {
         this.gathers.remove(player.getID());
         PlayerTaskManager manager = player.getTaskManager();
         Task task = manager.getCurTXTask();
         if (task != null) {
            task.forceComplete();
         }

         CollectResult.sendResult(player, this.getTemplateID(), false);
         if (enterDun) {
            DungeonManager.enterDiscovery(player, this.clueInfo.getTanxianLevel());
         } else if (this.clueInfo.getItemList().size() > 0) {
            ShowRewardItem.showItems(this.clueInfo.getItemList(), player, 0);
         }
      } else {
         CollectResult.sendResult(player, this.getTemplateID(), false);
         SystemMessage.writeMessage(player, result);
      }

   }

   public ClueInfo getClueInfo() {
      return this.clueInfo;
   }
}
