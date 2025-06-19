package com.mu.io.game.packet.imp.composite;

import com.mu.game.model.equip.compositenew.CompositLabel;
import com.mu.game.model.equip.compositenew.CompositeModel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.Map.Entry;

public class RequestComConfigure extends ReadAndWritePacket {
   public RequestComConfigure(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      SortedMap labelMap = CompositeModel.getLabelMap();
      this.writeByte(labelMap.size());
      Iterator var4 = labelMap.entrySet().iterator();

      while(var4.hasNext()) {
         Entry entry = (Entry)var4.next();
         int fLabelID = ((Integer)entry.getKey()).intValue();
         SortedMap sMap = (SortedMap)entry.getValue();
         this.writeUTF(CompositLabel.getLableName(fLabelID));
         this.writeByte(sMap.size());

         ArrayList actualList;
         for(Iterator var8 = sMap.entrySet().iterator(); var8.hasNext(); actualList = null) {
            Entry secEntry = (Entry)var8.next();
            int secLabelID = ((Integer)secEntry.getKey()).intValue();
            this.writeUTF(CompositLabel.getLableName(secLabelID));
            List comList = (List)secEntry.getValue();
            actualList = new ArrayList();
            Iterator var13 = comList.iterator();

            Integer comID;
            CompositeModel model;
            while(var13.hasNext()) {
               comID = (Integer)var13.next();
               model = CompositeModel.getModel(comID.intValue());
               if (model.getOpenLevel() <= player.getLevel()) {
                  actualList.add(comID);
               }
            }

            this.writeByte(actualList.size());
            var13 = actualList.iterator();

            while(var13.hasNext()) {
               comID = (Integer)var13.next();
               model = CompositeModel.getModel(comID.intValue());
               this.writeShort(comID.intValue());
               this.writeUTF(model.getName());
            }

            actualList.clear();
         }
      }

      boolean flag = player.getArrowGuideManager().hasComposeGuide();
      this.writeBoolean(flag);
      if (flag) {
         this.writeByte(0);
         this.writeByte(0);
         this.writeShort(1001);
      }

      player.writePacket(this);
   }
}
