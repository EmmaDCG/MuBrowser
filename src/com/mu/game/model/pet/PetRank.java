package com.mu.game.model.pet;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.utils.CommonRegPattern;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PetRank {
   private static Logger logger = LoggerFactory.getLogger(PetRank.class);
   private int rank;
   private String name;
   private int color;
   private int model;
   private int icon;
   private int scale;
   private LinkedHashMap properties;
   private int riseIngotExpend;
   private int riseIngotLLL;
   private int riseIngotLUL;
   private LinkedList riseExpendItem;
   private int riseLuckLowerLimit;
   private int riseLuckUpperLimit;
   private int riseLuckProbability;
   private int riseLuckMaxLimit;
   private int riseLevel;
   private int skill;
   private int attack;
   private int attackDistance;
   private int templateId;
   private HashMap attributeLimitMap = new HashMap();
   private LinkedHashMap levelPropertyList = new LinkedHashMap();
   private int playerZDL;
   private PetRank next;

   public int getRank() {
      return this.rank;
   }

   public String getName() {
      return this.name;
   }

   public int getColor() {
      return this.color;
   }

   public int getModel() {
      return this.model;
   }

   public int getIcon() {
      return this.icon;
   }

   public PetPropertyData getPropertyData(int level) {
      return (PetPropertyData)this.levelPropertyList.get(level);
   }

   public void addPropertyData(PetPropertyData data) {
      this.levelPropertyList.put(data.getLevel(), data);
   }

   public void setRiseExpendStr(String riseExpendStr) {
      try {
         this.riseExpendItem = new LinkedList();
         Matcher m = CommonRegPattern.PATTERN_INT.matcher(riseExpendStr);
         m.find();
         this.riseIngotExpend = Integer.parseInt(m.group());
         m.find();
         this.riseIngotLLL = Integer.parseInt(m.group());
         m.find();
         this.riseIngotLUL = Integer.parseInt(m.group());

         while(m.find()) {
            int modelID = Integer.parseInt(m.group());
            m.find();
            int count = Integer.parseInt(m.group());
            m.find();
            int lll = Integer.parseInt(m.group());
            m.find();
            int lul = Integer.parseInt(m.group());
            PetItemData item = new PetItemData(modelID, count);
            item.setLll(lll);
            item.setLul(lul);
            this.riseExpendItem.add(item);
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public void setLuckyStr(String luckyStr) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(luckyStr);
      m.find();
      this.riseLuckLowerLimit = Integer.parseInt(m.group());
      m.find();
      this.riseLuckUpperLimit = Integer.parseInt(m.group());
      m.find();
      this.riseLuckProbability = Integer.parseInt(m.group());
      m.find();
      this.riseLuckMaxLimit = Integer.parseInt(m.group());
   }

   public int getRiseIngotExpend() {
      return this.riseIngotExpend;
   }

   public int getRiseIngotLLL() {
      return this.riseIngotLLL;
   }

   public int getRiseIngotLUL() {
      return this.riseIngotLUL;
   }

   public LinkedList getRiseExpendItem() {
      return this.riseExpendItem;
   }

   public int getRiseLuckLowerLimit() {
      return this.riseLuckLowerLimit;
   }

   public int getRiseLuckUpperLimit() {
      return this.riseLuckUpperLimit;
   }

   public int getRiseLuckProbability() {
      return this.riseLuckProbability;
   }

   public int getRiseLevel() {
      return this.riseLevel;
   }

   public int getSkill() {
      return this.skill;
   }

   public PetRank getNext() {
      return this.next;
   }

   public void setNext(PetRank next) {
      this.next = next;
   }

   public LinkedHashMap getProperties() {
      return this.properties;
   }

   public void setPropertiesStr(String propertiesStr) {
      this.properties = new LinkedHashMap();

      StatEnum stat;
      int value;
      for(Matcher m = CommonRegPattern.PATTERN_INT.matcher(propertiesStr); m.find(); this.properties.put(stat, value)) {
         stat = StatEnum.find(Integer.parseInt(m.group()));
         m.find();
         value = Integer.parseInt(m.group());
         if (stat == null) {
            logger.error("init PetRank[{}] config err, not found stat '{}'", this.rank, propertiesStr);
         }
      }

   }

   public void setRank(int rank) {
      this.rank = rank;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setColor(int color) {
      this.color = color;
   }

   public void setModel(int model) {
      this.model = model;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public void setRiseLevel(int riseLevel) {
      this.riseLevel = riseLevel;
   }

   public void setSkill(int skill) {
      this.skill = skill;
      if (!SkillModel.hasModel(skill)) {
         logger.error("init PetRank[{}] config err, not found skill '{}'", this.rank, skill);
      }

   }

   public int getScale() {
      return this.scale;
   }

   public void setScale(int scale) {
      this.scale = scale;
   }

   public int getAttack() {
      return this.attack;
   }

   public void setAttack(int attack) {
      this.attack = attack;
      if (!SkillModel.hasModel(attack)) {
         logger.error("init PetRank[{}] config err, not found normal skill '{}'", this.rank, attack);
      }

   }

   public int getTemplateId() {
      return this.templateId;
   }

   public void setTemplateId(int templateId) {
      this.templateId = templateId;
   }

   public int getRiseLuckMaxLimit() {
      return this.riseLuckMaxLimit;
   }

   public int getAttackDistance() {
      return this.attackDistance;
   }

   public void setAttackDistance(int attackDistance) {
      this.attackDistance = attackDistance;
   }

   public void addAttributeLimit(PetAttributeData attribute, int limit) {
      this.attributeLimitMap.put(attribute, limit);
   }

   public int getAttributeLimit(PetAttributeData attribute) {
      return ((Integer)this.attributeLimitMap.get(attribute)).intValue();
   }

   public int getPlayerZDL() {
      return this.playerZDL;
   }

   public void setPlayerZDL(int zdl) {
      this.playerZDL = zdl;
   }
}
