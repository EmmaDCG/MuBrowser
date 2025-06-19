package com.mu.io.game.packet.imp.composite;

import com.mu.game.model.equip.compositenew.CompositeModel;
import com.mu.game.model.equip.compositenew.CompositeRate;
import com.mu.game.model.equip.compositenew.MaterialModel;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class RequestComPreview extends ReadAndWritePacket {
   public RequestComPreview(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public RequestComPreview() {
      super(20224, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int comID = this.readShort();
      HashMap chooseMap = new HashMap();
      int size = this.readByte();

      int result;
      for(result = 0; result < size; ++result) {
         int materialID = this.readInt();
         long itemID = (long)this.readDouble();
         chooseMap.put(materialID, itemID);
      }

      result = check(player, comID, chooseMap);
      if (result == 1) {
         this.writeData(player, comID, chooseMap);
         player.writePacket(this);
      } else {
         SystemMessage.writeMessage(player, 5018);
      }

      chooseMap.clear();
      chooseMap = null;
   }

   public static int check(Player player, int comID, HashMap chooseMap) {
      CompositeModel model = CompositeModel.getModel(comID);
      if (model == null) {
         return 5018;
      } else {
         List delList = null;
         Iterator it = chooseMap.entrySet().iterator();

         while(true) {
            int materialID;
            Item item;
            do {
               if (!it.hasNext()) {
                  if (delList != null) {
                     Iterator var10 = delList.iterator();

                     while(var10.hasNext()) {
                        Integer materialID2 = (Integer)var10.next();
                        chooseMap.remove(materialID2);
                     }
                  }

                  return 1;
               }

               Entry entry = (Entry)it.next();
               materialID = ((Integer)entry.getKey()).intValue();
               item = player.getBackpack().getItemByID(((Long)entry.getValue()).longValue());
            } while(model.hasChooseMaterial(materialID) && item != null);

            if (delList == null) {
               delList = new ArrayList();
            }

            delList.add(materialID);
         }
      }
   }

   public void writeData(Player player, int comID, HashMap chooseMap) throws Exception {
      CompositeModel model = CompositeModel.getModel(comID);
      int rate = model.getBasicRate();
      this.writeShort(comID);
      this.writeUTF(model.getTitle());
      this.writeUTF(model.getMaterialDes());
      List materialList = model.getMaterialList();
      this.writeByte(materialList.size());
      Iterator var8 = materialList.iterator();

      while(var8.hasNext()) {
         MaterialModel material = (MaterialModel)var8.next();
         this.writeInt(material.getMaterialID());
         this.writeByte(material.isAuto() ? 1 : 2);
         if (material.isAuto()) {
            GetItemStats.writeItem(material.getShowItem(), this);
         } else {
            long itemID = -1L;
            if (chooseMap.containsKey(material.getMaterialID())) {
               itemID = ((Long)chooseMap.get(material.getMaterialID())).longValue();
               Item item = player.getBackpack().getItemByID(itemID);
               rate += CompositeRate.getRate(item, material.getRateType());
            }

            this.writeDouble((double)itemID);
            this.writeUTF(material.getGridTitle());
            this.writeUTF(material.getGridContent());
         }
      }

      List targetItemList = model.getShowItemList();
      this.writeByte(targetItemList.size());
      Iterator var14 = targetItemList.iterator();

      while(var14.hasNext()) {
         Item item = (Item)var14.next();
         GetItemStats.writeItem(item, this);
      }

      rate = Math.min(model.getMaxRate(), rate);
      this.writeByte(rate / 1000);
      this.writeInt(model.getMoney());
      this.writeInt(model.getIngot());
   }

   public static void sendToClient(Player player, int comID, HashMap chooseMap) {
      try {
         RequestComPreview rcp = new RequestComPreview();
         rcp.writeData(player, comID, chooseMap);
         player.writePacket(rcp);
         rcp.destroy();
         rcp = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
