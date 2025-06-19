package com.mu.game.model.unit.material;

import com.mu.config.MessageText;
import com.mu.game.CenterManager;
import com.mu.game.model.map.Map;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.unit.MapUnit;
import com.mu.game.model.unit.Unit;
import com.mu.game.model.unit.controller.CountdownObject;
import com.mu.game.model.unit.material.requirement.MaterialRequirement;
import com.mu.game.model.unit.material.reward.MaterialReward;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.imp.NoneEvent;
import com.mu.game.model.unit.unitevent.imp.RefreshEvent;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.io.game.packet.imp.material.AroundMaterial;
import com.mu.io.game.packet.imp.material.CollectResult;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class Material extends MapUnit implements CountdownObject {
   private int refreshTime = 5000;
   private String des = "";
   private int templateID;
   private int collectTime = 2000;
   private String title = "";
   protected boolean isDisappear = false;
   protected ConcurrentHashMap gathers = new ConcurrentHashMap(16, 0.75F, 2);
   protected ArrayList collectRequirements = new ArrayList();
   protected ArrayList clickRequirements = new ArrayList();
   protected ArrayList rewardList = new ArrayList();
   private boolean canDisappear = true;

   public Material(long id, Map map, int templateID, String name, int modelID) {
      super(id, map);
      this.templateID = templateID;
      this.setName(name);
      this.setModelId(modelID);
   }

   public int startGather(Player player) {
      if (this.isDisappear) {
         return 10001;
      } else {
         int result = this.checkCanClick(player);
         if (result == 1) {
            this.gathers.put(player.getID(), true);
         }

         return result;
      }
   }

   public void addClickRequirement(MaterialRequirement req) {
      this.clickRequirements.add(req);
   }

   public void addCollectRequirement(MaterialRequirement req) {
      this.collectRequirements.add(req);
   }

   public void addReward(MaterialReward re) {
      this.rewardList.add(re);
   }

   public void idle() {
      this.doStatusEvent(new NoneEvent(this));
   }

   public void refresh() {
      Map map = this.getMap();
      if (map != null) {
         this.isDisappear = false;
         ArrayList list = this.getMap().getAroundPlayers(this.getPosition());
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            Player player = (Player)var4.next();
            if (this.canSee(player)) {
               AroundMaterial am = new AroundMaterial(this, player);
               player.writePacket(am);
               am.destroy();
               am = null;
            }
         }

         list.clear();
         list = null;
      }

   }

   public MaterialTemplate getTemplate() {
      return MaterialTemplate.getTemplate(this.templateID);
   }

   public int checkCanClick(Player player) {
      Iterator var3 = this.clickRequirements.iterator();

      while(var3.hasNext()) {
         MaterialRequirement req = (MaterialRequirement)var3.next();
         int result = req.match(player);
         if (result != 1) {
            return result;
         }
      }

      return 1;
   }

   public int checkCanCollect(Player player) {
      Iterator var3 = this.collectRequirements.iterator();

      while(var3.hasNext()) {
         MaterialRequirement req = (MaterialRequirement)var3.next();
         int result = req.match(player);
         if (result != 1) {
            return result;
         }
      }

      return 1;
   }

   public int canReward(Player player) {
      return 1;
   }

   public boolean canClick(Player player) {
      return this.checkCanClick(player) == 1;
   }

   public boolean canCollect(Player player) {
      return this.checkCanCollect(player) == 1;
   }

   public void setTitle(String t) {
      this.title = t;
   }

   public boolean canSee(Player player) {
      return !this.isDisappear;
   }

   public int getRefreshTime() {
      return this.refreshTime;
   }

   public void setRefreshTime(int refreshTime) {
      this.refreshTime = refreshTime;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public int getTemplateID() {
      return this.templateID;
   }

   public int getCollectTime() {
      return this.collectTime;
   }

   public void setCollectTime(int gatherTime) {
      this.collectTime = gatherTime;
   }

   public boolean isDisappear() {
      return this.isDisappear;
   }

   public void setDisappear(boolean isDisappear) {
      this.isDisappear = isDisappear;
   }

   public void destroy() {
      if (!this.isDestroy()) {
         super.destroy();
         this.gathers.clear();
         this.gathers = null;
         this.des = null;
         if (this.clickRequirements != null) {
            this.clickRequirements.clear();
            this.clickRequirements = null;
         }

         if (this.collectRequirements != null) {
            this.collectRequirements.clear();
            this.collectRequirements = null;
         }

         if (this.rewardList != null) {
            this.rewardList.clear();
            this.rewardList = null;
         }
      }

   }

   public int getUnitType() {
      return 7;
   }

   public boolean isCanDisappear() {
      return this.canDisappear;
   }

   public void setCanDisappear(boolean canDisappear) {
      this.canDisappear = canDisappear;
   }

   public void endInit() {
   }

   public int doReward(Player player) {
      if (!this.isDisappear && !this.isDestroy()) {
         Iterator var3 = this.collectRequirements.iterator();

         MaterialRequirement req;
         int result;
         while(var3.hasNext()) {
            req = (MaterialRequirement)var3.next();
            result = req.match(player);
            if (result != 1) {
               return result;
            }
         }

         var3 = this.rewardList.iterator();

         while(var3.hasNext()) {
            MaterialReward re = (MaterialReward)var3.next();
            result = re.doReword(player);
            if (result != 1) {
               return result;
            }
         }

         var3 = this.collectRequirements.iterator();

         while(var3.hasNext()) {
            req = (MaterialRequirement)var3.next();
            req.endCollect(player);
         }

         return 1;
      } else {
         return 10001;
      }
   }

   public synchronized void countdownEnd(Player player) {
      int result = this.doReward(player);
      if (result == 1) {
         this.gathers.remove(player.getID());
         if (this.canDisappear) {
            this.isDisappear = true;
            Iterator roles = this.gathers.keySet().iterator();

            while(roles.hasNext()) {
               long roleId = ((Long)roles.next()).longValue();
               Player p = CenterManager.getPlayerByRoleID(roleId);
               if (p != null) {
                  p.stopCountDown();
               }
            }

            this.gathers.clear();
            RemoveUnit ru = new RemoveUnit(new Unit[]{this});
            this.getMap().sendPacketToAroundPlayer(ru, this.getPosition());
            ru.destroy();
            ru = null;
            if (this.getRefreshTime() > 0) {
               this.doStatusEvent(new RefreshEvent(this));
            }
         }

         Integer taskId = TaskConfigManager.getCJMappingTask(this.getTemplateID());
         boolean canContinue = false;
         if (taskId != null) {
            Task task = (Task)player.getTaskManager().getCurrentTaskMap().get(taskId);
            if (task != null && !task.isComplete()) {
               canContinue = true;
            }
         }

         CollectResult.sendResult(player, this.getTemplateID(), canContinue);
      } else {
         CollectResult.sendResult(player, this.getTemplateID(), false);
         SystemMessage.writeMessage(player, result);
      }

   }

   public void stopCountDown(Player player) {
      if (this.gathers != null) {
         this.gathers.remove(player.getID());
      }

   }

   public int getTimeLength() {
      return this.getCollectTime();
   }

   public int getEffectId() {
      return this.getTemplate().getEffectId();
   }

   public boolean occupateStatus() {
      return true;
   }

   public String getTitle() {
      return this.title;
   }

   public int getBreakType() {
      return 2;
   }

   public String getCountDownName() {
      return MessageText.getText(1026);
   }

   public void switchArea(Rectangle newArena, Rectangle oldArea) {
   }

   public WriteOnlyPacket createAroundSelfPacket(Player viewer) {
      return null;
   }

   public int getOrderType() {
      return 0;
   }

   public int getCountdownType() {
      return 4;
   }

   public int getType() {
      return this.getUnitType();
   }
}
