package com.mu.io.game.packet.imp.attack;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;
import java.util.List;

public class CreatureHpChange extends WriteOnlyPacket {
   public CreatureHpChange() {
      super(32001);
   }

   public void setData(Player sawer, Creature effector, AttackResult result) {
      try {
         this.writeByte(effector.getType());
         this.writeDouble((double)effector.getID());
         writeFloat(sawer, effector, result, this);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendToClient(Creature effector, AttackResult result) {
      List players = effector.getMap().getAroundPlayers(effector.getPosition());

      CreatureHpChange chpc;
      for(Iterator var4 = players.iterator(); var4.hasNext(); chpc = null) {
         Player player = (Player)var4.next();
         chpc = new CreatureHpChange();
         chpc.setData(player, effector, result);
         player.writePacket(chpc);
         chpc.destroy();
      }

   }

   public static void writeFloat(Player sawer, Creature effector, AttackResult result, WriteOnlyPacket packet) {
      try {
         packet.writeByte(getShowType(sawer, effector, result));
         int value = result.getActualDamage();
         packet.writeInt(value);
         value = getRevise(effector, result.getType(), value);
         packet.writeInt(value);
         if (value > effector.getMaxHp()) {
            System.out.println(effector.getName() + "  修正值 = " + value + "," + result.getType());
         }

         value = effector.getMaxHp();
         packet.writeInt(value);
         int ct = result.isCaster(sawer);
         packet.writeByte(ct);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static int getShowType(Player sawer, Creature effector, AttackResult result) {
      if (result.getType() == 5) {
         if (effector.getUnitType() == 9 && result.getType() == 5) {
            return 1;
         }

         if (effector.getType() == 1 && sawer.getID() == effector.getID()) {
            return 2;
         }
      }

      return result.getType();
   }

   public static int getRevise(Creature effected, int type, int value) {
      int currentValue = 0;
      switch(type) {
      case 1:
      case 3:
      case 4:
      case 7:
      case 8:
      case 9:
         currentValue = effected.getHp();
         if (currentValue < 0) {
            currentValue = 0;
         }
         break;
      case 2:
      case 5:
         currentValue = effected.getHp();
         break;
      case 6:
         currentValue = effected.getHp();
      }

      if (currentValue < 0) {
         currentValue = 0;
      }

      return currentValue;
   }
}
