package com.mu.config;

public class Constant {
   public static final int RESULT_SUCCESS = 1;
   public static final int DefaultCheckRate = 20;
   public static final double Frame = 16.66666666667D;
   public static final int BornMapID = 10001;
   public static final int InterDefaultMapID = 10001;
   public static final int InterBackMapID = 10001;
   public static final int InterServerMaxPlayer = 2000;
   public static final int PercentNumber = 100000;
   public static final int ShowPercentNumber = 1000;
   public static final float Speed_Multiple = 100.0F;
   public static final int ATK_INTERVAL = 1500;
   public static final int Default_ATK_SPEED = 100;
   public static final int PLAYER_STEP = 400;
   public static final int OTHER_STEP = 400;
   public static final int Spint_Step = 2000;
   public static final int PkProtectedBuff = 80003;
   public static final int Min_Step = 10;
   public static final int Spint_Time = 1000;
   public static final int Role_MaxNumber = 2;
   public static final int DoWorkRate = 3;
   public static final int DoDropRate = 500;
   public static final int MaxPacketSize = 819200;
   public static final int MaxUploadPacketSize = 204800;
   public static final int MaxUploadSizePerMinute = 921600;
   public static final int DefaultTranspointEffect = 1180;
   public static final int CanReceivePacketSizePerSecond = 30;
   public static final int Destroy_Normal = 1;
   public static final int Destroy_ReLogin = 2;
   public static final int Destroy_InterReLogin = 3;
   public static final int Destroy_ToInterServer = 4;
   public static final int Destroy_BackOriginalServer = 5;
   public static final int PlayerMaxApplyGangSize = 5;
   public static final int GangAnnouncementSize = 68;
   public static final int MaxApplySizePerGang = 30;
   public static final int MaxGangNoticeSize = 50;
   public static final int ProtectionMaxLevelPerRank = 10;
   public static final int AttackDistanceAddition = 2400;
   public static final int PlayerFightingInterval = 10000;
   public static final int WorldLevelBegin = 150;
   public static final int WorldLevelEnd = 30;
   public static final int ClearRedNameItem = 3023;
   public static final long MonsterMinRefreshTime = 3000L;
   public static final int ShareTaskDistance = 10000;
   public static final int TeamExpBouns = 5000;
   public static final int SeeTopLevel = 100;
   public static final int Role_PublicCD = 800;
   public static final int Other_PublicCD = 2000;
   public static final int Shortcut_Max_Number = 10;
   public static final int Shortcut_Drug_Default = 0;
   public static final int Shortcut_SkillStart = 5;
   public static final int MailMaxItems = 8;
   public static final int Fcm_Rest_Time = 18000;
   public static final int Revival_Item = 2059;
   public static final int Revival_CountDown = 10;
   public static final int Team_Faraway = 36000;
   public static final String PerDayClearTime = "5:00";
   public static final int DungeonInviteTime = 30;
   public static final int Inspire_Type_Money = 1;
   public static final int Inspire_Type_Ingot = 2;
   public static final int Inspire_Rate = 45;
   public static final int Inspire_MaxLevel = 10;
   public static final int Inspire_MoneyMaxLevel = 5;
   public static final int MaxBlackListSize = 100;
   public static final int Max_Basic_Property = 3000;
   public static final int Max_Fruit_Property = 500;
   public static final int BOSS_None_Drop_Level = 100;
   public static final int NORMAL_None_Drop_Level = 50;
   public static final int RidingIntevalTime = 5000;
   public static final int MaxRaraListSize = 50;
   public static final int MaxTopSize = 100;
   public static final int MaxBigDevilTopSize = 20;
   public static final int BroadcastStrengthenLevel = 8;
   public static final int BroadcastAdditionalLevel = 4;

   public static int getPercent(int value, int maxValue) {
      float percent = 1.0F * (float)value / (float)maxValue * 100000.0F;
      return (int)percent;
   }

   public static int getPercentValue(int maxValue, int percent) {
      float value = (float)maxValue * 1.0F * (float)percent / 100000.0F;
      return (int)value;
   }
}
