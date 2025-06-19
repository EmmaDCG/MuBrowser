package com.mu.io.game.packet.imp.player.hangset;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.hang.GameHang;
import com.mu.game.model.unit.player.hang.HangSale;
import com.mu.game.model.unit.player.hang.HangSkill;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HangSet extends ReadAndWritePacket {
   public HangSet(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      boolean hand = this.readBoolean();
      int hpPercent = this.readByte();
      int mpPercent = this.readByte();
      boolean autoBuyDrug = this.readBoolean();
      int hangMode = this.readByte();
      int range = this.readByte();
      int pickOtherSize = this.readByte();
      List pickOthers = new ArrayList();

      for(int i = 0; i < pickOtherSize; ++i) {
         pickOthers.add(Integer.valueOf(this.readByte()));
      }

      int pickEquipSize = this.readByte();
      List qualities = new ArrayList();

      for(int i = 0; i < pickEquipSize; ++i) {
         qualities.add(Integer.valueOf(this.readByte()));
      }

      boolean pickupJewelry = this.readBoolean();
      boolean pickupHorse = this.readBoolean();
      boolean autoMend = this.readBoolean();
      boolean autoSale = this.readBoolean();
      int starLevelIndex = this.readByte();
      int zhuijiaIndex = this.readByte();
      boolean saleJewelry = this.readBoolean();
      boolean saleLucky = this.readBoolean();
      boolean lowLevelTickets = this.readBoolean();
      List hangSkillList = new ArrayList();
      int hangSkillSize = this.readByte();

      for(int k = 0; k < hangSkillSize; ++k) {
         int skillSize = this.readByte();
         List hangSkills = new ArrayList();

         for(int i = 0; i < skillSize; ++i) {
            hangSkills.add(this.readInt());
         }

         hangSkillList.add(hangSkills);
      }

      int skillProgramIndex = this.readByte();
      GameHang hang = player.getGameHang();
      hang.setHpPercent(hpPercent);
      hang.setMpPercent(mpPercent);
      hang.setAutoBuyDrug(autoBuyDrug);
      hang.setHangRange(range);
      hang.setAutoMend(autoMend);
      hang.setPickupOtherSorts(pickOthers);
      hang.setEquipQuality(qualities);
      hang.setHangSkills(hangSkillList);
      hang.setAutoSale(autoSale);
      hang.setHangMode(hangMode);
      hang.setSkillProgramIndex(skillProgramIndex);
      hang.setPickupHorse(pickupHorse);
      hang.setPickupJewelry(pickupJewelry);
      HangSale sale = hang.getHangSale();
      sale.setStarLevelIndex(starLevelIndex);
      sale.setZhuijiaIndex(zhuijiaIndex);
      sale.setSaleJewelry(saleJewelry);
      sale.setSaleLucky(saleLucky);
      sale.setLowLevelTickets(lowLevelTickets);
      sendToClient(player, hand);
      hang.setUpdate(true);
   }

   public HangSet(GameHang hang, boolean hand) {
      super(10012, (byte[])null);

      try {
         this.writeBoolean(hand);
         this.writeByte(hang.getHpPercent());
         this.writeByte(hang.getMpPercent());
         this.writeBoolean(hang.isAutoBuyDrug());
         this.writeByte(hang.getHangMode());
         this.writeByte(hang.getHangRange());
         List others = hang.getPickupOtherSorts();
         this.writeByte(others.size());
         Iterator var5 = others.iterator();

         while(var5.hasNext()) {
            Integer otherOption = (Integer)var5.next();
            this.writeByte(otherOption.intValue());
         }

         List equipQualities = hang.getEquipQuality();
         this.writeByte(equipQualities.size());
         Iterator var6 = equipQualities.iterator();

         while(var6.hasNext()) {
            Integer quality = (Integer)var6.next();
            this.writeByte(quality.intValue());
         }

         this.writeBoolean(hang.isPickupJewelry());
         this.writeBoolean(hang.isPickupHorse());
         this.writeBoolean(hang.isAutoMend());
         this.writeBoolean(hang.isAutoSale());
         HangSale sale = hang.getHangSale();
         this.writeByte(sale.getStarLevelIndex());
         this.writeByte(sale.getZhuijiaIndex());
         this.writeBoolean(sale.isSaleJewelry());
         this.writeBoolean(sale.isSaleLucky());
         this.writeBoolean(sale.isLowLevelTickets());
         List hangSkillList = hang.getHangSkillList();
         this.writeByte(hangSkillList.size());

         for(int i = 0; i < hangSkillList.size(); ++i) {
            List skills = ((HangSkill)hangSkillList.get(i)).getHangSkills();
            this.writeByte(skills.size());
            Iterator var10 = skills.iterator();

            while(var10.hasNext()) {
               Integer skillID = (Integer)var10.next();
               this.writeInt(skillID.intValue());
            }
         }

         this.writeByte(hang.getSkillProgramIndex());
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public static void sendToClient(Player player, boolean hand) {
      GameHang gameHang = player.getGameHang();
      HangSet hangset = new HangSet(gameHang, hand);
      player.writePacket(hangset);
      hangset.destroy();
      hangset = null;
   }
}
