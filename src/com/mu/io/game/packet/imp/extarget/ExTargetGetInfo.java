package com.mu.io.game.packet.imp.extarget;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.extarget.ExTarget;
import com.mu.game.model.unit.player.extarget.ExTargetElement;
import com.mu.game.model.unit.player.extarget.ExtargetManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.HashMap;
import java.util.Iterator;

public class ExTargetGetInfo extends ReadAndWritePacket {
   public ExTargetGetInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      ExtargetManager manager = player.getExtargetManager();
      HashMap map = ExtargetManager.getTargetMap();
      this.writeByte(map.size());
      Iterator it = map.values().iterator();

      while(it.hasNext()) {
         ExTarget target = (ExTarget)it.next();
         this.writeByte(target.getId());
         this.writeUTF(target.getName());
         HashMap elementMap = target.getElementMap(player.getProType());
         this.writeByte(elementMap.size());
         Iterator itElement = elementMap.values().iterator();

         while(itElement.hasNext()) {
            ExTargetElement element = (ExTargetElement)itElement.next();
            this.writeByte(element.getIndex());
            GetItemStats.writeItem(element.getItem(), this);
            this.writeBoolean(manager.hasCollected(target.getId(), element.getIndex()));
            this.writeByte(element.getZoom());
            this.writeUTF(element.getTips());
         }

         this.writeByte(target.getTitleId());
         this.writeByte(manager.getReceiveStatus(target.getId()));
      }

      player.writePacket(this);
   }
}
