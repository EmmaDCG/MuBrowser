package com.mu.io.game.packet.imp.market;

import com.mu.game.model.market.MarketConstant;
import com.mu.game.model.market.MarketSort;
import com.mu.game.model.market.condition.ConditionAtom;
import com.mu.game.model.market.condition.MarketCondition;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MarketConfig extends ReadAndWritePacket {
   public MarketConfig(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
   }

   private void writeData(Player player) throws Exception {
      HashMap mconMap = MarketCondition.getConditonMap();
      this.writeByte(mconMap.size());
      Iterator var4 = mconMap.values().iterator();

      while(var4.hasNext()) {
         MarketCondition condition = (MarketCondition)var4.next();
         this.writeByte(condition.getConditionID());
         List conList = condition.getConAtoms();
         this.writeByte(conList.size());
         Iterator var7 = conList.iterator();

         while(var7.hasNext()) {
            ConditionAtom atom = (ConditionAtom)var7.next();
            this.writeUTF(atom.getName());
         }
      }

      HashMap sortMap = MarketSort.getFirstSortMap();
      this.writeByte(sortMap.size());
      Iterator var14 = sortMap.values().iterator();

      while(var14.hasNext()) {
         MarketSort fatherSort = (MarketSort)var14.next();
         writeMarketSort(fatherSort, this);
         List fatherCondition = fatherSort.getConditions();
         this.writeByte(fatherCondition.size());
         Iterator var8 = fatherCondition.iterator();

         while(var8.hasNext()) {
            MarketCondition fc = (MarketCondition)var8.next();
            this.writeByte(fc.getConditionID());
         }

         List sonSortIDList = fatherSort.getSonSortIDList();
         this.writeByte(sonSortIDList.size());
         Iterator var9 = sonSortIDList.iterator();

         while(var9.hasNext()) {
            Integer sonSortID = (Integer)var9.next();
            MarketSort sonSort = MarketSort.getSort(sonSortID.intValue());
            writeMarketSort(sonSort, this);
         }
      }

      int taxRate = MarketConstant.getPersonTaxRate(player);
      this.writeByte(taxRate / 1000);
   }

   public static void sendToClient(Player player) {
      try {
         MarketConfig mc = new MarketConfig(20401, (byte[])null);
         mc.writeData(player);
         player.writePacket(mc);
         mc.destroy();
         mc = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private static void writeMarketSort(MarketSort sort, WriteOnlyPacket packet) throws Exception {
      packet.writeShort(sort.getSortID());
      packet.writeUTF(sort.getName());
      packet.writeShort(sort.getIndex());
   }
}
