package com.mu.game.model.unit.buff.requirement;

import com.mu.game.model.unit.buff.requirement.impl.BaiduMemberRequirement;
import com.mu.game.model.unit.buff.requirement.impl.MicroClientRequirement;
import com.mu.game.model.unit.buff.requirement.impl.VipLevelRequirement;

public class EffectReCreator {
   public static final int EffectType_Vip = 1;
   public static final int EffectType_MicroClient = 2;
   public static final int EffectType_BaiduMemeber = 3;

   public static EffectRequirement createRequirement(String typeStr, String values) throws Exception {
      if (typeStr != null && typeStr.length() >= 1) {
         int type = Integer.parseInt(typeStr);
         switch(type) {
         case 1:
            return createVipLevelRe(type, values);
         case 2:
            return createMicroClientRe(type);
         case 3:
            return createBaiduMemberClientRe(type, values);
         default:
            return null;
         }
      } else {
         return null;
      }
   }

   private static VipLevelRequirement createVipLevelRe(int type, String values) throws Exception {
      int vipLevel = Integer.parseInt(values);
      VipLevelRequirement vlr = new VipLevelRequirement(type, vipLevel);
      return vlr;
   }

   private static MicroClientRequirement createMicroClientRe(int type) throws Exception {
      MicroClientRequirement mcr = new MicroClientRequirement(type);
      return mcr;
   }

   private static BaiduMemberRequirement createBaiduMemberClientRe(int type, String values) throws Exception {
      int memberLevel = Integer.parseInt(values);
      BaiduMemberRequirement mcr = new BaiduMemberRequirement(type, memberLevel);
      return mcr;
   }
}
