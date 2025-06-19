package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.rune.RuneSet;
import com.mu.game.model.equip.rune.RuneSetModel;
import com.mu.game.model.item.container.imp.Equipment;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RuneSetDes extends WriteOnlyPacket {
   public RuneSetDes() {
      super(20221);
   }

   public static void sendToClient(Player player) {
      try {
         RuneSetDes rsd = new RuneSetDes();
         Equipment equipment = player.getEquipment();
         HashMap runeSets = equipment.getRuneSets();
         List runeList = RuneSetModel.getModelList();
         rsd.writeByte(runeList.size());

         StringBuffer sb;
         for(Iterator var6 = runeList.iterator(); var6.hasNext(); sb = null) {
            Integer modelID = (Integer)var6.next();
            RuneSetModel model = RuneSetModel.getModel(modelID.intValue());
            sb = new StringBuffer(model.getName());
            sb.append("(");
            RuneSet set = (RuneSet)runeSets.get(modelID);
            sb.append(set == null ? 0 : set.getCount());
            sb.append("/" + model.getEffectCount() + ")");
            sb.append("#b");
            int mutiple = set == null ? 0 : set.getEffectMultiple();
            String preStr = "";
            if (mutiple > 1) {
               preStr = "#f:{20}(" + mutiple + "倍)#f";
            }

            List modifies = model.getStats();
            if (set != null && mutiple > 1) {
               modifies = set.getStats();
            }

            boolean gray = mutiple <= 0;

            for(int i = 0; i < modifies.size(); ++i) {
               FinalModify modify = (FinalModify)modifies.get(i);
               sb.append(preStr);
               if (gray) {
                  sb.append("#f:{21}");
                  sb.append(modify.getStat().getName() + "+" + modify.getShowValue() + modify.getSuffix());
                  sb.append("#f");
               } else {
                  sb.append("#f:{2}");
                  sb.append(modify.getStat().getName());
                  sb.append("#f");
                  sb.append("#f:{20}");
                  sb.append("+" + modify.getShowValue() + modify.getSuffix());
                  sb.append("#f");
               }

               if (i < modifies.size() - 1) {
                  sb.append("#b");
               }
            }

            rsd.writeUTF(sb.toString());
         }

         player.writePacket(rsd);
         rsd.destroy();
         rsd = null;
      } catch (Exception var16) {
         var16.printStackTrace();
      }

   }
}
