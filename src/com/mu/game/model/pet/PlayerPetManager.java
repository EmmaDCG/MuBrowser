package com.mu.game.model.pet;

import com.mu.config.MessageText;
import com.mu.db.log.IngotChangeType;
import com.mu.executor.Executor;
import com.mu.game.IDFactory;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.map.Map;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatChange;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.game.model.stats.statId.StatIdCreator;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.Unit;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.monster.MonsterStar;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.skill.manager.SkillFactory;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.game.model.unit.unitevent.imp.PetRiseEvent;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.io.game.packet.imp.pet.InitPet;
import com.mu.io.game.packet.imp.pet.PetInform;
import com.mu.io.game.packet.imp.pet.PetOpen;
import com.mu.io.game.packet.imp.sys.RightMessage;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Rnd;
import com.mu.utils.Tools;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerPetManager {
   private static Logger logger = LoggerFactory.getLogger(PlayerPetManager.class);
   private Player owner;
   private boolean open;
   private PetRank rank;
   private int level;
   private long exp;
   private Pet pet;
   private int luck;
   private ConcurrentHashMap attributeMap = Tools.newConcurrentHashMap2();
   private boolean show;
   private long diedTime;
   private long rankTime;
   private List sjAttributeList;
   private List pyAttributeList;
   private ConcurrentHashMap joinAttributeMap = Tools.newConcurrentHashMap2();
   public static final int FOLLOW_LEFT = -1;
   public static final int FOLLOW_MIDDLE = 0;
   public static final int FOLLOW_RIGHT = 1;
   public static final int FOLLOW_DISTANCE = 2000;

   public PlayerPetManager(Player player) {
      this.owner = player;
      this.open = false;
      this.rank = PetConfigManager.getRankHead();
      this.level = 1;
      this.pet = new Pet(IDFactory.getTemporaryID(), (Map)null, player);
   }

   public void init(InitPet packet) {
      try {
         boolean hasPet = packet.readBoolean();
         this.open = hasPet;
         int value;
         if (hasPet) {
            PetRank rank = PetConfigManager.getRank(packet.readByte());
            int level = packet.readShort();
            long exp = packet.readLong();
            value = packet.readInt();
            boolean show = packet.readBoolean();
            long diedTime = packet.readLong();
            long rankTime = packet.readLong();
            this.rank = rank == null ? PetConfigManager.getRankHead() : rank;
            this.level = Math.max(1, Math.min(PetConfigManager.MAX_LEVEL, level));
            this.exp = Math.max(0L, Math.min(PetConfigManager.getExp(this.level), exp));
            this.luck = value;
            this.show = show;
            this.diedTime = diedTime;
            this.rankTime = rankTime;
         }

         int count = packet.readByte();

         for(int i = 0; i < count; ++i) {
            int id = packet.readShort();
            int level = packet.readInt();
            value = packet.readInt();
            PetAttributeData data = PetConfigManager.getAttribute(id);
            if (data != null) {
               PetAttribute attr = new PetAttribute(data, level, value);
               this.attributeMap.put(data.getStat(), attr);
            }
         }

         Iterator it = PetConfigManager.getAttributeIterator();

         while(it.hasNext()) {
            PetAttributeData data = (PetAttributeData)it.next();
            if (data.getOpenRank() <= this.rank.getRank() && !this.attributeMap.containsKey(data.getStat())) {
               this.attributeMap.put(data.getStat(), new PetAttribute(data, data.getFirstLevel().getLevel(), 0));
            }
         }

         this.computerProperties();
         if (this.isShow()) {
            this.setShow(true);
            this.pet.setAlreadyDie(false);
            this.pet.setLastAttackTarget((Creature)null);
            this.pet.setHp(this.pet.getMaxHp());
         }
      } catch (Exception var13) {
         logger.error("role init pet data error!");
         var13.printStackTrace();
      }

   }

   public void onLogin() {
      PetInform.sendMsgConfig(this.owner, this.isOpen(), this.rank.getRank());
      if (!this.isOpen() && this.owner.getLevel() >= FunctionOpenManager.getPetOpenLevel()) {
         this.openByServer(false);
         PetOpen.sendPetOpen(this.owner);
      }

      PetInform.sendMsgState(this.owner, this.isShow(), this.pet);
      PetInform.sendMsgAttributeOnLogin(this.owner, this.attributeMap, this.rank);
      PetInform.sendPetProperties(this.owner, this.pet, PetConfigManager.ALL_PROPERTIES);
      PetInform.sendPetProperties2(this.owner, this.joinAttributeMap);
      PetInform.sendPetLevel(this.owner, this.level, this.exp, PetConfigManager.getExp(this.level));
      if (this.isShow()) {
         PetInform.sendMsgState(this.owner, this.isShow(), this.pet);
      }

   }

   public void computerProperties() {
      boolean rankRise = this.pet.getRank() != this.rank.getRank();
      this.pet.setRank(this.rank.getRank());
      this.pet.setName(this.rank.getName());
      this.pet.setAttackDistance(this.rank.getAttackDistance());
      PetPropertyData propertyData = this.rank.getPropertyData(this.level);
      MonsterStar monsterStar = MonsterStar.getMonsterStar(propertyData.getMonsterStar(), propertyData.getMonsterLevel());
      this.pet.getProperty().inits(monsterStar.getHp(), monsterStar.getMp(), monsterStar.getMinAtt(), monsterStar.getMaxAtt(), monsterStar.getDef(), monsterStar.getHit(), monsterStar.getAvd(), (int)this.owner.getSpeed(), monsterStar.getOtherStats());
      this.pet.setTemplateId(this.rank.getTemplateId());
      this.pet.setMp(monsterStar.getMp());
      LinkedHashMap propertiesList = propertyData.getPropertyList();
      Iterator it = propertiesList.values().iterator();

      while(it.hasNext()) {
         FinalModify fm = (FinalModify)it.next();
         int value = fm.getValue();
         this.pet.getProperty().initStat(fm.getStat(), value);
      }

      this.pet.getProperty().initStat(StatEnum.DOMINEERING, propertyData.getPetZDL());
      this.pet.getProperty().initStat(StatEnum.SPEED, this.owner.getStatValue(StatEnum.SPEED));
      this.pet.getProperty().processStats();
      ArrayList _sjAttributeList = new ArrayList();
      ArrayList _pyAttributeList = new ArrayList();
      ConcurrentHashMap _joinAttributeMap = Tools.newConcurrentHashMap2();
      int zdl = this.rank.getPlayerZDL();
      Iterator it2 = this.rank.getProperties().keySet().iterator();

      StatEnum se;
      while(it2.hasNext()) {
         se = (StatEnum)it2.next();
         _sjAttributeList.add(new FinalModify(se, ((Integer)this.rank.getProperties().get(se)).intValue(), StatModifyPriority.ADD));
         _joinAttributeMap.put(se, (Integer)this.rank.getProperties().get(se));
      }

      it = this.attributeMap.keySet().iterator();

      while(it.hasNext()) {
         se = (StatEnum)it.next();
         PetAttribute at = (PetAttribute)this.attributeMap.get(se);
         zdl += at.getLevelData().getZDL();
         _pyAttributeList.add(new FinalModify(se, at.getAttributeValue(), StatModifyPriority.ADD));
         int v = _joinAttributeMap.get(se) == null ? 0 : ((Integer)_joinAttributeMap.get(se)).intValue();
         _joinAttributeMap.put(se, at.getAttributeValue() + v);
      }

      _joinAttributeMap.put(StatEnum.DOMINEERING, zdl);
      _sjAttributeList.add(new FinalModify(StatEnum.DOMINEERING, zdl, StatModifyPriority.ADD));
      this.sjAttributeList = _sjAttributeList;
      this.pyAttributeList = _pyAttributeList;
      this.joinAttributeMap = _joinAttributeMap;
      if (this.isOpen()) {
         StatChange.addStat(this.owner, StatIdCreator.createPetId(1), this.sjAttributeList, StatChange.isSendStat(this.owner));
         StatChange.addStat(this.owner, StatIdCreator.createPetId(2), this.pyAttributeList, StatChange.isSendStat(this.owner));
      }

      if (rankRise) {
         this.pet.getSkillManager().removeAllPetSkill();
         this.pet.getSkillManager().addSkill(SkillFactory.createSkill(this.rank.getAttack(), 1, this.pet));
         this.pet.getSkillManager().addSkill(SkillFactory.createSkill(this.rank.getSkill(), 1, this.pet));
      }

   }

   public boolean openByServer(boolean isShow) {
      if (this.isOpen()) {
         return false;
      } else {
         this.open = true;
         StatChange.addStat(this.owner, StatIdCreator.createPetId(1), this.sjAttributeList, StatChange.isSendStat(this.owner));
         StatChange.addStat(this.owner, StatIdCreator.createPetId(2), this.pyAttributeList, StatChange.isSendStat(this.owner));
         if (isShow) {
            this.show();
         }

         this.owner.getTaskManager().onEventCheckValue(TargetType.ValueType.Pet_Rank);
         return true;
      }
   }

   public int settingPetRank(int rankId) {
      this.openByServer(true);
      if (rankId <= this.rank.getRank()) {
         return 22013;
      } else {
         this.rank = PetConfigManager.getRank(rankId);
         this.luck = 0;
         Iterator it = PetConfigManager.getAttributeIterator();

         while(it.hasNext()) {
            PetAttributeData data = (PetAttributeData)it.next();
            if (data.getOpenRank() <= this.rank.getRank() && !this.attributeMap.containsKey(data.getStat())) {
               this.attributeMap.put(data.getStat(), new PetAttribute(data, data.getFirstLevel().getLevel(), 0));
               PetInform.sendMsgAttribute(this.owner, (PetAttribute)this.attributeMap.get(data.getStat()), this.rank);
            }
         }

         this.computerProperties();
         if (this.isShow()) {
            this.levelMap();
            this.owner.getMap().addPet(this.pet);
            this.owner.getMap().petEnterMapSuccess(this.pet);
         }

         this.rankTime = System.currentTimeMillis();
         this.dbReplacePet();
         PetInform.sendMsgRiseResult(this.owner, false, true);
         PetInform.sendMsgState(this.owner, this.isShow(), this.pet);
         PetInform.sendPetProperties(this.owner, this.pet, PetConfigManager.ALL_PROPERTIES);
         PetInform.sendPetProperties2(this.owner, this.joinAttributeMap);
         ActivityManager.refreshDynamicMenu(4, this.owner);
         this.owner.getTaskManager().onEventCheckValue(TargetType.ValueType.Pet_Rank);
         return 1;
      }
   }

   public boolean canRise() {
      return this.owner.getLevel() >= this.rank.getRiseLevel();
   }

   public void rise(boolean autoBuy, boolean autoRise) {
      if (this.isOpen()) {
         if (!this.canRise()) {
            SystemMessage.writeMessage(this.owner, 22002);
         } else {
            this.owner.addMomentEvent(new PetRiseEvent(this.owner, autoBuy, autoRise));
         }
      }
   }

   public boolean riseOperate(boolean autoBuy, boolean autoRise) {
      if (!this.canRise()) {
         SystemMessage.writeMessage(this.owner, 22002);
         PetInform.sendMsgRiseResult(this.owner, false, false);
         return false;
      } else if (this.rank.getNext() == null) {
         SystemMessage.writeMessage(this.owner, 22009);
         PetInform.sendMsgRiseResult(this.owner, false, false);
         return false;
      } else {
         PetItemData expendItem = null;
         int lower = 0;
         int upper = 0;
         LinkedList itemList = this.rank.getRiseExpendItem();
         Iterator it = itemList.iterator();

         while(it.hasNext()) {
            PetItemData wid = (PetItemData)it.next();
            if (this.owner.getItemManager().deleteItemByModel(wid.getModelID(), wid.getCount(), 29).isOk()) {
               expendItem = wid;
               lower = wid.getLll();
               upper = wid.getLul();
               break;
            }
         }

         if (expendItem == null) {
            if (!autoBuy) {
               SystemMessage.writeMessage(this.owner, 21005);
               PetInform.sendMsgRiseResult(this.owner, false, false);
               return false;
            }

            if (PlayerManager.reduceIngot(this.owner, this.rank.getRiseIngotExpend(), IngotChangeType.PET, this.rank.getRank() + "(" + this.rank.getName() + ")") != 1) {
               SystemMessage.writeMessage(this.owner, 21006);
               PetInform.sendMsgRiseResult(this.owner, false, false);
               return false;
            }

            lower = this.rank.getRiseIngotLLL();
            upper = this.rank.getRiseIngotLUL();
         }

         int addValue = Rnd.get(lower, upper);
         SystemMessage.writeMessage(this.owner, MessageText.getText(22011).replace("%s%", String.valueOf(addValue)), 22011);
         this.luck += addValue;
         if (this.luck < this.rank.getRiseLuckUpperLimit() && (this.luck <= this.rank.getRiseLuckLowerLimit() || Rnd.get(100000) >= this.rank.getRiseLuckProbability())) {
            this.dbReplacePet();
            PetInform.sendMsgRiseResult(this.owner, autoRise, false);
            return true;
         } else {
            this.rank = this.rank.getNext() != null ? this.rank.getNext() : this.rank;
            this.luck = 0;
            Iterator it2 = PetConfigManager.getAttributeIterator();

            while(it2.hasNext()) {
               PetAttributeData data = (PetAttributeData)it2.next();
               if (data.getOpenRank() == this.rank.getRank() && !this.attributeMap.containsKey(data.getStat())) {
                  this.attributeMap.put(data.getStat(), new PetAttribute(data, data.getFirstLevel().getLevel(), 0));
                  PetInform.sendMsgAttribute(this.owner, (PetAttribute)this.attributeMap.get(data.getStat()), this.rank);
               }
            }

            this.computerProperties();
            if (this.isShow()) {
               this.levelMap();
               this.owner.getMap().addPet(this.pet);
               this.owner.getMap().petEnterMapSuccess(this.pet);
            }

            this.rankTime = System.currentTimeMillis();
            this.dbReplacePet();
            RightMessage.pushRightMessage(this.owner, 20008, addValue);
            PetInform.sendMsgRiseResult(this.owner, false, true);
            PetInform.sendMsgState(this.owner, this.isShow(), this.pet);
            PetInform.sendPetProperties(this.owner, this.pet, PetConfigManager.ALL_PROPERTIES);
            PetInform.sendPetProperties2(this.owner, this.joinAttributeMap);
            ActivityManager.refreshDynamicMenu(4, this.owner);
            this.owner.getTaskManager().onEventCheckValue(TargetType.ValueType.Pet_Rank);
            return false;
         }
      }
   }

   public void increaseExp(long exp, boolean sendMsg) {
      if (this.isOpen()) {
         long overExp = this.exp + exp - PetConfigManager.getExp(this.level);
         if (overExp >= 0L && this.level < this.owner.getLevel()) {
            this.levelUp(overExp, sendMsg);
         } else {
            this.exp = Math.min(this.exp + exp, PetConfigManager.getExp(this.level) - 1L);
            if (sendMsg) {
               PetInform.sendPetLevel(this.owner, this.level, this.exp, PetConfigManager.getExp(this.level));
            }
         }

      }
   }

   public void levelUp(long overExp, boolean sendMsg) {
      int nextLevel = this.level + 1;
      if (nextLevel <= PetConfigManager.MAX_LEVEL) {
         this.exp = 0L;
         this.level = nextLevel;
         this.computerProperties();
         this.pet.setHp(this.pet.getMaxHp());
         this.increaseExp(overExp, false);
         if (sendMsg) {
            PetInform.sendPetProperties(this.owner, this.pet, PetConfigManager.ALL_PROPERTIES);
            this.dbReplacePet();
            PetInform.sendPetLevel(this.owner, this.level, this.exp, PetConfigManager.getExp(this.level));
            PetInform.sendPetLevelEffect(this.owner);
            PetInform.sendPetLevelBroadcast(this.owner, this.pet);
         }

      }
   }

   public void levelMap() {
      RemoveUnit leaveMap = null;
      Map map = this.pet.getMap();
      if (map != null) {
         this.pet.setLastAttackTarget((Creature)null);
         this.pet.idle();
         map.removePet(this.pet);
         leaveMap = new RemoveUnit(new Unit[]{this.pet});
         map.sendPacketToAroundPlayer(leaveMap, this.owner, true);
         leaveMap.destroy();
         leaveMap = null;
      }

   }

   public void stopRise() {
      this.owner.endMomentEvent(Status.PET);
   }

   public boolean riseAttribute(int statId) {
      StatEnum stat = StatEnum.find(statId);
      if (stat == null) {
         SystemMessage.writeMessage(this.owner, 22004);
         return false;
      } else {
         PetAttribute attribute = (PetAttribute)this.attributeMap.get(stat);
         if (attribute == null) {
            SystemMessage.writeMessage(this.owner, 22004);
            return false;
         } else {
            PetAttributeData.Level lv = attribute.getLevelData();
            if (lv.getNext() == null) {
               SystemMessage.writeMessage(this.owner, 22005);
               return false;
            } else {
               int limit = this.rank.getAttributeLimit(attribute.getData());
               if (lv.getLevel() >= limit) {
                  SystemMessage.writeMessage(this.owner, 22008);
                  return false;
               } else {
                  PetItemData item = lv.getExpendItem();
                  if (!this.owner.getItemManager().deleteItemByModel(item.getModelID(), item.getCount(), 29).isOk()) {
                     SystemMessage.writeMessage(this.owner, 22006);
                     return false;
                  } else {
                     int v = item.getValue();
                     attribute.increaseExp(this.owner, v, true);
                     this.dbReplaceAttribute(attribute);
                     return false;
                  }
               }
            }
         }
      }
   }

   public void beKilled() {
      this.diedTime = System.currentTimeMillis();
      this.hide();
   }

   public void control() {
      if (this.isOpen()) {
         if (this.isShow()) {
            this.hide();
         } else {
            Map map = this.getOwner().getMap();
            if (!map.petCanShow()) {
               SystemMessage.writeMessage(this.getOwner(), 22010);
               return;
            }

            this.show();
         }

      }
   }

   public void show() {
      if (this.isOpen()) {
         this.setShow(true);
         this.pet.setAlreadyDie(false);
         this.pet.setLastAttackTarget((Creature)null);
         this.pet.setHp(this.pet.getMaxHp());
         this.owner.getMap().addPet(this.pet);
         this.owner.getMap().petEnterMapSuccess(this.pet);
         PetInform.sendMsgState(this.owner, this.isShow(), this.pet);
         this.dbReplacePet();
      }
   }

   public void hide() {
      if (this.isOpen()) {
         Map map = this.pet.getMap();
         if (map != null) {
            RemoveUnit leaveMap = new RemoveUnit(new Unit[]{this.pet});
            map.removePet(this.pet);
            map.sendPacketToAroundPlayer(leaveMap, this.owner, true);
            leaveMap.destroy();
            leaveMap = null;
            this.setShow(false);
            PetInform.sendMsgState(this.owner, this.isShow(), this.pet);
            this.dbReplacePet();
            this.pet.getAggroList().clear();
         }

      }
   }

   public Player getOwner() {
      return this.owner;
   }

   public boolean canSee() {
      return this.isOpen() && this.isShow() || this.pet.getMap() != null;
   }

   public boolean isOpen() {
      return this.open;
   }

   public PetRank getRank() {
      return this.rank;
   }

   public int getLuck() {
      return this.luck;
   }

   public void dbReplacePet() {
      try {
         WriteOnlyPacket packet = Executor.ReplacePet.toPacket(this);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void dbReplaceAttribute(PetAttribute attribute) {
      try {
         WriteOnlyPacket packet = Executor.ReplacePetAttribute.toPacket(this.owner, attribute);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static Point[] followPath(Point[] path, Pet self, int index, int lmr, boolean startSelf) {
      Map map = self.getMap();
      int pathLength = path.length;
      Point[] followMovePath = new Point[pathLength];
      double interval = (double)(2000 * index);
      double displacement = 1000.0D;
      Point result_s = new Point();
      Point result_e = new Point();
      computerFollowPoints(map, interval, lmr, displacement, path[pathLength - 2], path[pathLength - 1], result_s, result_e);
      followMovePath[pathLength - 2] = result_s;
      followMovePath[pathLength - 1] = result_e;
      if (pathLength > 2) {
         System.arraycopy(path, 1, followMovePath, 1, pathLength - 3);
         result_s = new Point();
         result_e = new Point();
         computerFollowPoints(map, interval, lmr, displacement, path[0], path[1], result_s, result_e);
         followMovePath[0] = result_s;
      }

      Point position = new Point(self.getActualPosition());
      if (startSelf) {
         Point[] temp = new Point[pathLength + 1];
         System.arraycopy(followMovePath, 0, temp, 1, pathLength);
         temp[0] = position;
         followMovePath = null;
         followMovePath = temp;
      }

      return followMovePath;
   }

   public static void printPoint(String s, Point... points) {
      System.out.printf(s);

      for(int i = 0; i < points.length; ++i) {
         System.out.printf("(%s, %s)", points[i].x, points[i].y);
      }

   }

   public static void computerFollowPoints(Map map, double interval, int lmr, double displacement, Point original_s, Point original_e, Point result_s, Point result_e) {
      double pathVectorX = (double)(original_e.x - original_s.x);
      double pathVectorY = (double)(original_e.y - original_s.y);
      double pathVectorModule = Math.sqrt(pathVectorX * pathVectorX + pathVectorY * pathVectorY);
      if (pathVectorModule == 0.0D) {
         result_s.setLocation(original_e);
         result_e.setLocation(original_s);
      } else {
         double sin = pathVectorY / pathVectorModule;
         double cos = pathVectorX / pathVectorModule;
         double intervalGapVectorX = -interval * cos;
         double intervalGapVectorY = -interval * sin;
         double displacementGapVectorX = (double)(-lmr) * displacement * sin;
         double displacementGapVectorY = (double)lmr * displacement * cos;
         result_e.setLocation(original_e.x + (int)(displacementGapVectorX + intervalGapVectorX), original_e.y + (int)(displacementGapVectorY + intervalGapVectorY));
         result_s.setLocation((double)result_e.x - 50.0D * cos, (double)result_e.y - 50.0D * sin);
         result_e.x = Math.max(map.getLeft(), Math.min(result_e.x, map.getRight()));
         result_e.y = Math.max(map.getBottom(), Math.min(result_e.y, map.getTop()));
         result_s.x = Math.max(map.getLeft(), Math.min(result_s.x, map.getRight()));
         result_s.y = Math.max(map.getBottom(), Math.min(result_s.y, map.getTop()));
      }
   }

   public void playerMove(Point[] path, long moveTime) {
   }

   public void playerBeAttacked(Creature attacker, AttackResult result) {
      Pet pet = this.getActivePet();
      if (pet != null) {
         if (!this.getOwner().isMoving()) {
            pet.setLastAttackTarget(attacker);
         }

      }
   }

   public Pet getPet() {
      return this.pet;
   }

   public Pet getActivePet() {
      return this.isOpen() && this.pet.getMap() != null && !this.pet.isDestroy() ? this.pet : null;
   }

   public boolean isShow() {
      return this.show;
   }

   public void setShow(boolean show) {
      this.show = show;
   }

   public long getDiedTime() {
      return this.diedTime;
   }

   public long getRankTime() {
      return this.rankTime;
   }

   public void destroy() {
      this.owner = null;
      this.rank = null;
      if (this.pet != null) {
         this.pet.destroy();
         this.pet = null;
      }

      if (this.attributeMap != null) {
         Iterator iterator = this.attributeMap.values().iterator();

         while(iterator.hasNext()) {
            ((PetAttribute)iterator.next()).destroy();
         }

         this.attributeMap.clear();
         this.attributeMap = null;
      }

      if (this.sjAttributeList != null) {
         this.sjAttributeList.clear();
         this.sjAttributeList = null;
      }

      if (this.pyAttributeList != null) {
         this.pyAttributeList.clear();
         this.pyAttributeList = null;
      }

   }

   public ConcurrentHashMap getJoinAttributeMap() {
      return this.joinAttributeMap;
   }

   public int getLevel() {
      return this.level;
   }

   public long getExp() {
      return this.exp;
   }
}
