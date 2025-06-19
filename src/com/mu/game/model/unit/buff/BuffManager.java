package com.mu.game.model.unit.buff;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.model.BuffDBEntry;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.buff.model.ClientPerformStatus;
import com.mu.game.model.unit.unitevent.OperationLimitManager;
import com.mu.io.game.packet.imp.buff.AddBuff;
import com.mu.io.game.packet.imp.buff.ClientShowStatus;
import com.mu.io.game.packet.imp.buff.DeleteBuff;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BuffManager {
   private Creature owner;
   private boolean destroy = false;
   private ConcurrentHashMap buffs = null;
   private ConcurrentHashMap performStatusMap = null;
   private List buffDBEntries = null;

   public BuffManager(Creature owner) {
      this.owner = owner;
      this.buffs = Tools.newConcurrentHashMap2();
      this.performStatusMap = Tools.newConcurrentHashMap2();
   }

   public void check(long now) {
      Iterator it = this.buffs.values().iterator();

      while(it.hasNext()) {
         ((Buff)it.next()).check(now);
      }

   }

   public void addBuff(Buff buff) {
      if (buff != null) {
         this.buffs.put(buff.getModelID(), buff);
         this.changeBuffEffect(buff.getModel().getServerStatusID(), buff.getModel().getClientStatusID(), true);
      }
   }

   public void changeBuffEffect(int debuffType, int clientStatusID, boolean add) {
      List limitEnums = OperationLimitManager.getOperation(debuffType);
      if (limitEnums != null) {
         if (add) {
            this.getOwner().addLimitOperatioin(1, limitEnums.toArray());
         } else {
            this.getOwner().removeLimitOperation(1, limitEnums.toArray());
         }
      }

      if (clientStatusID != -1) {
         boolean flag = this.addOrDelClientStatus(clientStatusID, add);
         if (flag) {
            ClientShowStatus.sendToClient(this.getOwner());
         }
      }

   }

   private boolean addOrDelClientStatus(int clientStatusID, boolean add) {
      ClientPerformStatus cps = ClientPerformStatus.getPerformStatus(clientStatusID);
      if (cps == null) {
         return false;
      } else {
         List performStatus = cps.getPerformStatus();
         boolean result = false;
         Iterator var7 = performStatus.iterator();

         while(var7.hasNext()) {
            Integer statusID = (Integer)var7.next();
            boolean flag = this.addOrDel(statusID.intValue(), add);
            if (flag) {
               result = true;
            }
         }

         return result;
      }
   }

   private boolean addOrDel(int statusID, boolean add) {
      Integer value = (Integer)this.performStatusMap.get(statusID);
      if (value == null) {
         if (add) {
            this.performStatusMap.put(statusID, Integer.valueOf(1));
            return true;
         }
      } else if (add) {
         this.performStatusMap.put(statusID, value.intValue() + 1);
      } else {
         if (value.intValue() <= 1) {
            this.performStatusMap.remove(statusID);
            return true;
         }

         this.performStatusMap.put(statusID, value.intValue() - 1);
      }

      return false;
   }

   public void removeBuff(int modelID) {
      Buff buff = (Buff)this.buffs.remove(modelID);
      if (buff != null) {
         this.changeBuffEffect(buff.getModel().getServerStatusID(), buff.getModel().getClientStatusID(), false);
      }

   }

   public void endBuff(int modelID, boolean notice) {
      Buff buff = this.getBuff(modelID);
      this.endBuff(buff, notice);
   }

   public void endBuff(Buff buff, boolean notice) {
      if (buff != null) {
         buff.end(notice);
         this.removeBuff(buff.getModelID());
      }

   }

   public void endBuffWhenDie() {
      ArrayList buffIDs = new ArrayList();

      try {
         Iterator it = this.buffs.values().iterator();

         while(it.hasNext()) {
            Buff buff = (Buff)it.next();
            if (this.getOwner().getType() == 2) {
               this.endBuff(buff, false);
            } else if (buff.getModel().isDieAway()) {
               this.endBuff(buff, false);
               if (buff.getModel().isShow()) {
                  if (BuffModel.showForOther(this.getOwner(), buff.getModelID())) {
                     buffIDs.add(buff.getModelID());
                  } else {
                     DeleteBuff.sendToClient(this.getOwner(), buff.getModelID());
                  }
               }
            }
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if (buffIDs.size() > 0) {
         DeleteBuff.sendToClient(this.getOwner(), buffIDs);
         buffIDs.clear();
      }

   }

   public List getShowBuffs(boolean self) {
      List buffList = null;
      long now = System.currentTimeMillis();
      Iterator var6 = this.buffs.values().iterator();

      while(true) {
         Buff buff;
         do {
            do {
               do {
                  if (!var6.hasNext()) {
                     return buffList;
                  }

                  buff = (Buff)var6.next();
               } while(!buff.getModel().isShow());
            } while(buff.isEnd(now));
         } while(!BuffModel.showForOther(this.getOwner(), buff.getModelID()) && !self);

         if (buffList == null) {
            buffList = new ArrayList();
         }

         buffList.add(buff);
      }
   }

   public void destroy() {
      if (!this.isDestroy()) {
         this.setDestroy(true);
         Iterator it = this.buffs.values().iterator();

         while(it.hasNext()) {
            ((Buff)it.next()).destroy();
         }

         this.buffs.clear();
         this.buffs = null;
         if (this.performStatusMap != null) {
            this.performStatusMap.clear();
            this.performStatusMap = null;
         }

         this.owner = null;
      }
   }

   public Buff getBuff(int modelID) {
      return (Buff)this.buffs.get(modelID);
   }

   public boolean hasBuff(int modelID) {
      return this.buffs.containsKey(modelID);
   }

   public ConcurrentHashMap getPerformStatusMap() {
      return this.performStatusMap;
   }

   public void setPerformStatusMap(ConcurrentHashMap performStatusMap) {
      this.performStatusMap = performStatusMap;
   }

   public Creature getOwner() {
      return this.owner;
   }

   public boolean isDestroy() {
      return this.destroy;
   }

   public void setDestroy(boolean destroy) {
      this.destroy = destroy;
   }

   public ConcurrentHashMap getBuffs() {
      return this.buffs;
   }

   public void setBuffs(ConcurrentHashMap buffs) {
      this.buffs = buffs;
   }

   public void setOwner(Creature owner) {
      this.owner = owner;
   }

   public Buff createAndStartBuff(Creature castor, int buffModelId, int level, boolean send, long... duration) {
      if (duration == null) {
         return this.createAndStartBuff(castor, buffModelId, level, send, 0L, (List)null);
      } else {
         Buff buff = this.createBuff(castor, buffModelId, level, 0L, (List)null, duration);
         if (buff != null) {
            this.addAndSendBuff(buff, send);
         }

         return buff;
      }
   }

   public Buff createAndStartBuff(Creature castor, int buffModelId, int level, boolean send, long addTime, List params) {
      Buff buff = this.createBuff(castor, buffModelId, level, addTime, params);
      if (buff != null) {
         this.addAndSendBuff(buff, send);
      }

      return buff;
   }

   private Buff createBuff(Creature castor, int buffModelId, int level, long addTime, List params, long... durations) {
      Buff buff = BuffFactory.creatBuff(buffModelId, level, this.getOwner(), castor);
      if (buff != null) {
         buff.setCaster(castor);
         buff.loadData(params);
         if (addTime != 0L) {
            buff.addDuration(addTime, false);
         }

         if (durations != null && durations.length > 0) {
            long duration = durations[0];
            if (duration > 0L) {
               buff.resetDuration(duration);
            }
         }

         buff.start(false);
      }

      return buff;
   }

   private void addAndSendBuff(Buff buff, boolean send) {
      if (!buff.isDestory()) {
         this.addBuff(buff);
         if (send && this.getOwner().getMap() != null) {
            AddBuff.sendToClient(this.getOwner(), buff);
         }
      }

   }

   public void loadBuffFromDB(BuffDBEntry entry) {
      if (this.buffDBEntries == null) {
         this.buffDBEntries = new ArrayList();
      }

      this.buffDBEntries.add(entry);
   }

   public void startLoadBuff() {
      List hasBuffs = this.getShowBuffs(true);
      if (hasBuffs != null) {
         AddBuff.sendToSelf(this.getOwner(), hasBuffs);
         hasBuffs.clear();
      }

      List buffList = new ArrayList();
      if (this.buffDBEntries != null) {
         Iterator var4 = this.buffDBEntries.iterator();

         while(var4.hasNext()) {
            BuffDBEntry entry = (BuffDBEntry)var4.next();
            Buff buff = BuffFactory.creatBuff(entry.getBuffModelID(), entry.getLevel(), this.getOwner(), this.getOwner());
            if (buff != null) {
               buff.loadFromDB(entry.getDuration(), entry.getPropStr());
               buff.start(true);
               if (!buff.isDestory()) {
                  this.addBuff(buff);
                  buffList.add(buff);
               }
            }
         }

         this.buffDBEntries.clear();
      }

      this.buffDBEntries = null;
      if (buffList.size() > 0) {
         AddBuff.sendToSelf(this.getOwner(), buffList);
         buffList.clear();
      }

      buffList.clear();
      buffList = null;
   }
}
