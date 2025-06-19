package com.mu.game.dungeon.imp.discovery;

import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.box.BoxItem;
import com.mu.game.model.item.box.BoxManager;
import com.mu.game.model.unit.material.Material;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dungeon.DungeonInfoUpdate;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.io.game.packet.imp.material.AroundMaterial;
import com.mu.io.game.packet.imp.material.ChangeMaterialClickStatus;
import com.mu.io.game.packet.imp.monster.AroundMonster;
import com.mu.io.game.packet.imp.player.hangset.StartHang;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.io.game.packet.imp.tanxian.ReceiveChestReward;
import com.mu.io.game.packet.imp.tanxian.ShowRewardItem;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DiscoveryMap extends DungeonMap {
   private boolean isInit = false;
   private int discoveryId = ((Discovery)this.getDungeon()).getInfo().getDiscoveyId();
   private int dunType = ((Discovery)this.getDungeon()).getInfo().getDunType();
   private int discoveryLevel = ((Discovery)this.getDungeon()).getDiscoveyLevel();
   private int maxNumber = 0;
   private int curNumber = 0;
   private ArrayList chestItemList = new ArrayList();
   private boolean chestItemBeReward = false;
   private boolean chestBeOpen = false;

   public DiscoveryMap(int referMapID, Discovery d) {
      super(referMapID, d);
   }

   public void init(Player player) {
      switch(this.dunType) {
      case 1:
         this.initMonster(player);
         break;
      default:
         this.initChest(player);
      }

   }

   public boolean canSwitchByLink() {
      return false;
   }

   private void initMonster(Player player) {
      ArrayList list = ((DiscoveryTemplate)((Discovery)this.getDungeon()).getTemplate()).getMonsterList(this.discoveryId, this.discoveryLevel);
      if (list != null) {
         ArrayList mList = new ArrayList();

         DiscoveyMonsterGroup group;
         int num;
         for(Iterator var5 = list.iterator(); var5.hasNext(); this.maxNumber += num) {
            group = (DiscoveyMonsterGroup)var5.next();
            num = group.getNum();

            for(int i = 0; i < num; ++i) {
               DiscoveryMonster dm = new DiscoveryMonster(group, this);
               this.addMonster(dm);
               mList.add(dm);
            }
         }

         AroundMonster am = new AroundMonster(mList);
         player.writePacket(am);
         am.destroy();
         group = null;
      }
   }

   private void initChest(Player player) {
      this.maxNumber = 1;
      Integer boxId = ((DiscoveryTemplate)((Discovery)this.getDungeon()).getTemplate()).getBoxId(this.discoveryId, this.discoveryLevel);
      ItemDataUnit unit;
      Iterator var6;
      if (boxId != null) {
         BoxItem boxItem = BoxManager.getBoxItem(boxId.intValue());
         List itemDataList = boxItem.getItemData();
         if (itemDataList != null) {
            var6 = itemDataList.iterator();

            while(var6.hasNext()) {
               unit = (ItemDataUnit)var6.next();
               Item item = ItemTools.createItem(2, unit);
               this.chestItemList.add(item);
            }
         }
      }

      ArrayList list = ((DiscoveryTemplate)((Discovery)this.getDungeon()).getTemplate()).getChestList();
      ArrayList mList = new ArrayList();
      var6 = list.iterator();

      while(var6.hasNext()) {
         ChestInfo info = (ChestInfo)var6.next();
         DiscoveryChest chest = new DiscoveryChest(this, info);
         this.addMaterial(chest);
         mList.add(chest);
      }

      AroundMaterial am = new AroundMaterial(mList, player);
      player.writePacket(am);
      am.destroy();
      unit = null;
   }

   public boolean isChestItemBeReward() {
      return this.chestItemBeReward;
   }

   public void pushSchedule() {
      DungeonInfoUpdate du = this.getUpdateInfo();
      this.broadcastPacket(du);
      du.destroy();
      du = null;
   }

   private DungeonInfoUpdate getUpdateInfo() {
      DungeonInfoUpdate du = new DungeonInfoUpdate();

      try {
         du.writeByte(12);
         String des = ((Discovery)this.getDungeon()).getInfo().getRightDes();
         du.writeUTF(des.replace("%c%", String.valueOf(this.curNumber)).replace("%m%", String.valueOf(this.maxNumber)));
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return du;
   }

   public void doEnterMapSpecil(Player player) {
      super.doEnterMapSpecil(player);
      if (!this.isInit) {
         this.isInit = true;
         this.init(player);
      }

      this.pushSchedule();
      if (this.dunType == 1) {
         ((Discovery)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
            public void run() {
               DiscoveryMap.this.startHang();
            }
         }, 3000L));
      }

   }

   private void startHang() {
      Iterator it = this.playerMap.values().iterator();

      while(it.hasNext()) {
         try {
            StartHang.start((Player)it.next());
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

   }

   public void chestBeCollect(DiscoveryChest chest, Player player) {
      ArrayList list = new ArrayList();
      Iterator ru = this.getMaterialMap().values().iterator();

      while(ru.hasNext()) {
         Material m = (Material)ru.next();
         if (m.getID() != chest.getID()) {
            list.add(m);
            m.setShouldDestroy(true);
            m.setDisappear(true);
         }
      }

      if (list.size() > 0) {
         RemoveUnit ru2 = new RemoveUnit(list);
         this.broadcastPacket(ru2);
         ru2.destroy();
         ru = null;
         list.clear();
      }

      ShowRewardItem.showItems(this.chestItemList, player, 1);
      this.chestBeOpen = true;
      if (this.curNumber < 1) {
         this.curNumber = 1;
      } else {
         this.pushSchedule();
      }

   }

   public void monsterBeKilled(DiscoveryMonster monster) {
      ++this.curNumber;
      this.pushSchedule();
      if (this.curNumber >= this.maxNumber) {
         ((Discovery)this.getDungeon()).setComplete(true);
         ((Discovery)this.getDungeon()).setSuccess(true);
         ((Discovery)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
            public void run() {
               ((Discovery)DiscoveryMap.this.getDungeon()).destroy();
            }
         }, 30000L));
      }

      monster.setShouldDestroy(true);
   }

   public synchronized void rewardChest(Player player) {
      if (!this.chestItemBeReward && this.chestBeOpen) {
         if (player.getBackpack().getVacantSize() < this.chestItemList.size()) {
            SystemMessage.writeMessage(player, 2003);
         } else {
            Iterator var3 = this.chestItemList.iterator();

            while(var3.hasNext()) {
               Item item = (Item)var3.next();
               player.getItemManager().addItem(item, 43);
            }

            this.chestItemBeReward = true;
            this.chestItemList.clear();
            ReceiveChestReward.receiveSuccess(player);

            ChangeMaterialClickStatus cs;
            for(Iterator it = this.materialMap.values().iterator(); it.hasNext(); cs = null) {
               Material m = (Material)it.next();
               cs = new ChangeMaterialClickStatus(m, false);
               player.writePacket(cs);
               cs.destroy();
            }

            ((Discovery)this.getDungeon()).setComplete(true);
            ((Discovery)this.getDungeon()).setSuccess(true);
            ((Discovery)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
               public void run() {
                  ((Discovery)DiscoveryMap.this.getDungeon()).destroy();
               }
            }, 30000L));
         }
      }
   }

   public synchronized void destroy() {
      super.destroy();
      this.chestItemList.clear();
   }
}
