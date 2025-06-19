package com.mu.game.model.activity.imp.firstpay;

import com.mu.config.BroadcastManager;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;
import java.util.HashMap;

public class FirstPayElement extends ActivityElement {
   private HashMap itemMap = new HashMap();
   private HashMap unitMap = new HashMap();

   public FirstPayElement(int id, FirstPay father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
   }

   public ArrayList getItemList(int pro) {
      return (ArrayList)this.itemMap.get(pro);
   }

   public ArrayList getItemDataList(int pro) {
      return (ArrayList)this.unitMap.get(pro);
   }

   public void addItemList(int pro, ArrayList list) {
      this.itemMap.put(pro, list);
   }

   public void addItemUnitList(int pro, ArrayList list) {
      this.unitMap.put(pro, list);
   }

   public ArrayList getItemUnitList(Player player) {
      int pro = player.getProType();
      return this.getItemDataList(pro);
   }

   public synchronized boolean receive(Player player) {
      if (this.getReceiveStatus(player) == 0) {
         return false;
      } else {
         boolean b = super.receive(player);
         if (b) {
            BroadcastManager.broadcastFirstPay(player);
         }

         return b;
      }
   }

   public boolean canReceive(Player player, boolean notice) {
      if (player.getUser().getPay(((FirstPay)this.getFather()).getOpenDate().getTime(), ((FirstPay)this.getFather()).getCloseDate().getTime()) < ((FirstPay)this.getFather()).getPayIngot()) {
         if (notice) {
            SystemMessage.writeMessage(player, 25102);
         }

         return false;
      } else {
         return super.canReceive(player, notice);
      }
   }

   public int getReceiveStatus(Player player) {
      if (this.receiveOverload(player)) {
         return 2;
      } else {
         return player.getUser().getPay(((FirstPay)this.getFather()).getOpenDate().getTime(), ((FirstPay)this.getFather()).getCloseDate().getTime()) < ((FirstPay)this.getFather()).getPayIngot() ? 0 : 1;
      }
   }
}
