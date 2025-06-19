package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.equip.external.WeaponEntry;
import com.mu.game.model.unit.player.Profession;
import com.mu.game.model.unit.player.ProfessionType;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AdvanceClientConfig extends WriteOnlyPacket {
   public AdvanceClientConfig() {
      super(113);
   }

   public static AdvanceClientConfig getConfig() {
      AdvanceClientConfig ac = new AdvanceClientConfig();

      try {
         writePlayerProfession(ac);
         writeWeaponEntry(ac);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      return ac;
   }

   private static void writePlayerProfessionType(AdvanceClientConfig pc) throws Exception {
      HashMap types = ProfessionType.getTypeMap();
      pc.writeByte(types.size());
      Iterator var3 = types.values().iterator();

      while(var3.hasNext()) {
         ProfessionType pro = (ProfessionType)var3.next();
         pc.writeByte(pro.getTypeID());
         pc.writeUTF(pro.getName());
         pc.writeUTF(pro.getDes());
         pc.writeShort(pro.getModel3D());
         List proList = pro.getPros();
         pc.writeByte(proList.size());
         Iterator var6 = proList.iterator();

         while(var6.hasNext()) {
            Integer data = (Integer)var6.next();
            pc.writeInt(data.intValue());
         }
      }

   }

   private static void writePlayerProfession(AdvanceClientConfig pc) throws Exception {
      HashMap professions = Profession.getProfessions();
      pc.writeByte(professions.size());
      Iterator var3 = professions.values().iterator();

      while(var3.hasNext()) {
         Profession pro = (Profession)var3.next();
         pc.writeByte(pro.getProID());
         pc.writeByte(pro.getProType());
         pc.writeUTF(pro.getProName());
      }

   }

   private static void writeWeaponEntry(AdvanceClientConfig pc) throws Exception {
      HashMap entries = WeaponEntry.getEntries();
      pc.writeShort(entries.size());
      Iterator var3 = entries.values().iterator();

      while(var3.hasNext()) {
         WeaponEntry entry = (WeaponEntry)var3.next();
         pc.writeShort(entry.getID());
         pc.writeShort(entry.getModelID());
         pc.writeByte(entry.getMoveType());
         pc.writeShort(entry.getTrajectoryEffectID());
         pc.writeShort(entry.getKnifeShadowEffect());
      }

   }
}
