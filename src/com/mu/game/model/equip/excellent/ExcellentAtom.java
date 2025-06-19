package com.mu.game.model.equip.excellent;

import com.mu.game.model.equip.equipStat.EquipStat;
import com.mu.game.model.weight.WeightAtom;

public class ExcellentAtom extends WeightAtom {
   private int equipStatID;

   public ExcellentAtom(int equipStatID, int weight) {
      super(weight);
      this.equipStatID = equipStatID;
   }

   public int getEquipStatID() {
      return this.equipStatID;
   }

   public void setEquipStatID(int equipStatID) {
      this.equipStatID = equipStatID;
   }

   public EquipStat createEquipStat() {
      EquipStat stat = EquipStat.getEquipStat(this.getEquipStatID());
      return stat.cloneStat();
   }
}
