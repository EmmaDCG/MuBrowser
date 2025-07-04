package com.mu.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class MessageText {
   private static HashMap allMessages = new HashMap();
   public static final int OK = 1;
   public static final int Server_Error = 2;
   public static final int Server_Suffix_Name = 3;
   public static final int Version_Mismatch = 4;
   public static final int Inter_TooManyUsers = 5;
   public static final int Inter_Illegal = 6;
   public static final int Illegal_Operation_Ininterserver = 7;
   public static final int Server_Item_Error = 8;
   public static final int DisconnectOtherLogin = 9;
   public static final int DisconnectNetError = 10;
   public static final int Role_Number_Limit = 1001;
   public static final int Role_Name_Repeat = 1002;
   public static final int Role_Name_Illegal = 1003;
   public static final int Role_ID_Illegal = 1004;
   public static final int Role_Potential_NoEn = 1005;
   public static final int Role_Potential_DataError = 1006;
   public static final int Role_Potential_Max = 1007;
   public static final int Role_Potential_NoAllocated = 1008;
   public static final int Role_Hp_Full = 1009;
   public static final int Role_Mp_Full = 1010;
   public static final int Money_NoEn = 1011;
   public static final int Shortcut_Position_Err = 1012;
   public static final int Shortcut_None = 1013;
   public static final int Shortcut_Item_Imp = 1014;
   public static final int Ingot_NoEnough = 1015;
   public static final int RedeemPoints_NoEn = 1016;
   public static final int Role_Level_Max = 1017;
   public static final int Role_Exp_Cannt_Add = 1018;
   public static final int Role_Exp_NoEnFor_Levelup = 1019;
   public static final int Role_Other = 1020;
   public static final int Role_Offline = 1021;
   public static final int Role_PreCalPotential_Err = 1022;
   public static final int Role_BeKilledByOther = 1023;
   public static final int Role_Map_Cannt_Hangset = 1024;
   public static final int Role_VipFlyItemNotEnough = 1025;
   public static final int Role_GatherNow = 1026;
   public static final int Role_CannotSwitchLineInDun = 1027;
   public static final int Role_MustQuitDun = 1028;
   public static final int Role_EnterMapLevelLimit = 1029;
   public static final int Role_EnterMapMasterLevelLimit = 1030;
   public static final int Role_MustEquipWing = 1031;
   public static final int Role_In_Cool = 1032;
   public static final int Role_Wrong_Map_Point = 1033;
   public static final int Role_Potential_CantWash = 1034;
   public static final int Role_FunctionIsNotOpen = 1035;
   public static final int WorldLevel_addition = 1036;
   public static final int WorldLevel_None = 1037;
   public static final int MasterName = 1038;
   public static final int NoNe = 1039;
   public static final int Role_Name_Long = 1040;
   public static final int Role_Boss_Tel_Cuntdown = 1041;
   public static final int Storage_Expansion_Limit = 2001;
   public static final int Storage_Expansion_Fail = 2002;
   public static final int Storage_NoEn = 2003;
   public static final int Storage_Full = 2004;
   public static final int Storage_Type_NoFound = 2005;
   public static final int Storage_Depot_Full = 2006;
   public static final int Storage_TreasureHunt_MoveFromOtherContainer = 2007;
   public static final int Storage_Move_Forbid = 2008;
   public static final int Storage_Update = 2009;
   public static final int Storage_DeportNotOpenByVip = 2010;
   public static final int Storage_TreasureNoEn = 2011;
   public static final int Storage_TreasureMoveSuccess = 2012;
   public static final int Input_Count_Err = 2013;
   public static final int Item_NoEnough = 3001;
   public static final int Item_None = 3002;
   public static final int Item_Cannot_Use_Direct = 3003;
   public static final int Item_UserLevel_NoEn = 3004;
   public static final int Item_In_Transaction = 3005;
   public static final int Item_Profession_NoPe = 3006;
   public static final int Item_Cannot_Batch = 3007;
   public static final int Item_In_Cool = 3008;
   public static final int Item_Exchange_Position_Err = 3009;
   public static final int Item_Split_Count_Err = 3010;
   public static final int Item_UnSuitable_Number = 3011;
   public static final int Item_Cannot_Split = 3012;
   public static final int Item_Cannot_MoveTo_Depot = 3013;
   public static final int Item_Cannot_SellTo_Npc = 3014;
   public static final int Item_NoRepurchaseItem = 3015;
   public static final int Item_Gender_NoPe = 3016;
   public static final int Item_ExpiredMessage = 3017;
   public static final int Item_Goods_None = 3018;
   public static final int Item_Goods_Isnot_Vip = 3019;
   public static final int Item_Limit_Over = 3020;
   public static final int Item_Goods_RedEvil = 3022;
   public static final int Item_Luck_Des = 3023;
   public static final int Item_CumSkill_Des = 3024;
   public static final int Item_Set_Des = 3025;
   public static final int Item_Random_Luck = 3026;
   public static final int Item_Random_Excellent = 3027;
   public static final int Item_Random_Special = 3028;
   public static final int Item_Random_Master = 3029;
   public static final int Item_Random_Zhuijia = 3030;
   public static final int Item_Strength_Des_1 = 3031;
   public static final int Item_Strength_Des_2 = 3032;
   public static final int Item_Revival_NoEn = 3033;
   public static final int Item_BasicPro_NoEn = 3034;
   public static final int Item_BasicPro_Max = 3035;
   public static final int Item_Active_Des = 3036;
   public static final int Item_Zhuijia_Des = 3037;
   public static final int Item_Buff_Has = 3038;
   public static final int Item_Buff_HasMaxOverlap = 3039;
   public static final int Item_OpenBox_none = 3040;
   public static final int Item_Durability_Imp = 3041;
   public static final int Item_Durability_Max = 3042;
   public static final int Item_No_Definite = 3043;
   public static final int Item_ExpireTime = 3044;
   public static final int Item_Evil_None = 3045;
   public static final int Item_Magic_NumberErr = 3046;
   public static final int Item_Basic_Str_NoEn = 3047;
   public static final int Item_Basic_Dex_NoEn = 3048;
   public static final int Item_Basic_Int_NoEn = 3049;
   public static final int Item_Basic_Con_NoEn = 3050;
   public static final int Item_WellDrop = 3051;
   public static final int Item_WellOpenBox = 3052;
   public static final int Item_MagicBoxContent = 3053;
   public static final int Item_OtherError = 3054;
   public static final int Item_RoleLevelUp_Max = 3055;
   public static final int Item_ExcellentDes = 3056;
   public static final int Item_MagicGoods_None = 3060;
   public static final int Item_Magic_MoneyNoEn = 3061;
   public static final int Item_Magic_GetPoint = 3062;
   public static final int Equip_Position_Improper = 5001;
   public static final int Equip_Cannt_Strength = 5002;
   public static final int Equip_MaxStarLevel = 5003;
   public static final int Equip_Lucky_Err = 5004;
   public static final int Equip_Zhuijia_Type_Imp = 5005;
   public static final int Equip_Zhuijia_MaxValue = 5006;
   public static final int Equip_Inherit_Type_Imp1 = 5007;
   public static final int Equip_Inherit_Type_Imp2 = 5008;
   public static final int Equip_Inherit_HasStone = 5009;
   public static final int Equip_Inherit_InEquip = 5010;
   public static final int Equip_Inherit_LevelLimit = 5011;
   public static final int Equip_Inherit_MinLevel = 5012;
   public static final int Equip_Rune_Type_Imp1 = 5013;
   public static final int Equip_Rune_Type_Imp2 = 5014;
   public static final int Equip_Rune_NoCount = 5015;
   public static final int Equip_Rune_Position = 5016;
   public static final int Equip_Rune_Same = 5017;
   public static final int Equip_Stone_Max = 5019;
   public static final int Equip_Stone_Type_Imp1 = 5020;
   public static final int Equip_Stone_Type_Imp2 = 5021;
   public static final int Equip_Stone_NoSocket = 5022;
   public static final int Equip_Stone_NoStoneByIndex = 5023;
   public static final int Equip_Conver_None = 5026;
   public static final int Equip_Repair_ContainerErr = 5028;
   public static final int Equip_Repair_None = 5029;
   public static final int Equip_Repair_Dura_Max = 5030;
   public static final int Equip_Inherit_Same = 5031;
   public static final int Equip_Rune_NoneByIndex = 5032;
   public static final int Equip_Rune_Max = 5033;
   public static final int Equip_Conver_NoData = 5034;
   public static final int Equip_Com_Rate_Fail = 5035;
   public static final int Equip_Redeem_NoExcellent = 5036;
   public static final int Equip_Rate_Basic_Des = 5037;
   public static final int Equip_Rate_Lucky_Des = 5038;
   public static final int Equip_Rate_Item_Des = 5039;
   public static final int Equip_Rune_Inherit_Type_Imp1 = 5041;
   public static final int Equip_Rune_Inherit_Type_Imp2 = 5042;
   public static final int Equip_Rune_Inher_Star_NoEn = 5043;
   public static final int Equip_Rune_Inher_Mater_NoRune = 5044;
   public static final int Equip_Rune_Inher_Popup_Des = 5045;
   public static final int Equip_Rune_Inher_TarHasRune = 5046;
   public static final int Equip_Rune_Inher_ConsumeErr = 5047;
   public static final int Equip_Stone_Type_Imp3 = 5048;
   public static final int Equip_Stone_HasSameType = 5049;
   public static final int Equip_Rune_Mosaic_Fail = 5050;
   public static final int Equip_Stone_Mosaic_Fail = 5051;
   public static final int Item_Com_NoModel = 5018;
   public static final int Item_Com_ChooseItemNone = 5052;
   public static final int Item_Com_MaterialID_Imp = 5053;
   public static final int Item_Com_matItem_None = 5054;
   public static final int Item_Com_Material_NoImp = 5055;
   public static final int Item_Com_Material_Repeat = 5056;
   public static final int Item_Com_Material_NoEn = 5057;
   public static final int Item_Com_Fail = 5058;
   public static final int Horse_Fusion_Same = 5059;
   public static final int Horse_Fusion_StarLevelLow = 5060;
   public static final int Horse_Fusion_TargetNone = 5061;
   public static final int Horse_Fusion_MaterialNone = 5062;
   public static final int Horse_Fusion_ProtecteItem = 5063;
   public static final int Horse_Fusion_TargetTypeImp = 5064;
   public static final int Horse_Fusion_MaterTypeImp = 5065;
   public static final int Item_Upgrade_None = 5066;
   public static final int Item_Upgrade_Target_Impl = 5067;
   public static final int Item_TimeLimited = 5068;
   public static final int Item_Update_Fail = 5069;
   public static final int Item_Repair_success = 5070;
   public static final int Pop_Common_Confirm = 4001;
   public static final int Pop_Common_Cancel = 4002;
   public static final int Pop_ExPackage_Content = 4003;
   public static final int Pop_ExPackage_Title = 4004;
   public static final int Pop_ExDeport_Title = 4005;
   public static final int Pop_ExDeport_Content = 4006;
   public static final int Pop_Rune_RemoveTitle = 4007;
   public static final int Pop_Rune_RemoveContent = 4008;
   public static final int Pop_Star_Strength_Title = 4009;
   public static final int Pop_BasicPropertyNoEn_Title = 4010;
   public static final int Pop_BasicPropertyNoEn_Content = 4011;
   public static final int Pop_BasicPropertyNoEn_Count = 4012;
   public static final int Pop_AfterAllocatEquip_title = 4013;
   public static final int Pop_AfterAllocateEquip_Content = 4014;
   public static final int Pop_RedNameTitle = 4015;
   public static final int Pop_RedNameContent = 4016;
   public static final int Pop_RedNameClear = 4017;
   public static final int Pop_RenewTitle = 4018;
   public static final int Pop_UnMosaicStone_Title = 4019;
   public static final int Pop_UnMosaicStone_Content = 4020;
   public static final int Pop_Financing_Title = 4021;
   public static final int Pop_Financing_Content = 4022;
   public static final int Pop_Transfer_Title = 4023;
   public static final int Pop_Transfer_Content = 4024;
   public static final int Pop_Composite_Bind = 4025;
   public static final int Pop_Composite_Title = 4026;
   public static final int Pop_Composite_Content = 4027;
   public static final int Pop_Angel_Title = 4028;
   public static final int Pop_Angel_Content = 4029;
   public static final int Pop_Spirit_Title = 4036;
   public static final int Pop_Spirit_Content = 4037;
   public static final int Skill_Hp_NoEn = 8001;
   public static final int Skill_Mp_NoEn = 8002;
   public static final int Skill_AG_NoEn = 8003;
   public static final int Skill_Front_NoSuit = 8004;
   public static final int Skill_UserLevel_NoEn = 8005;
   public static final int Skill_Proficiency_NoEn = 8006;
   public static final int Skill_None = 8007;
   public static final int Skill_NoLearn = 8008;
   public static final int Skill_In_CoolTime = 8009;
   public static final int Skill_Learn_Leapfrog = 8010;
   public static final int Skill_Max_Level = 8011;
   public static final int Skill_NoEn_Level = 8012;
   public static final int Skill_Effected_Improper = 8013;
   public static final int Out_Range = 8014;
   public static final int Skill_Cannot_AttackSelf = 8015;
   public static final int Map_Cannot_Pk = 8016;
   public static final int PK_IN_SAFE_DE = 8017;
   public static final int Not_In_Same_Map = 8018;
   public static final int Pk_MIN_LEVEL = 8019;
   public static final int PK_Attacked_Fresher = 8020;
   public static final int PK_CANNOT_SELF_IN_PEACE = 8021;
   public static final int PK_CANONT_TARGET_IN_PEACE = 8022;
   public static final int PK_CANNOT_TEAMER = 8024;
   public static final int PK_IN_SAME_GANG = 8025;
   public static final int TARGET_INVALID = 8026;
   public static final int Skill_Owner_ActionLimit = 8027;
   public static final int Die = 8028;
   public static final int Skill_Has_Seleted = 8029;
   public static final int Skill_NoIn_Shortcut = 8030;
   public static final int Skill_Cannt_Seleted = 8031;
   public static final int Pk_Mode_Same = 8032;
   public static final int PK_Mode_Map_Stint = 8033;
   public static final int Pk_Mode_Level_Limit = 8034;
   public static final int PK_Evil_LimitAttack = 8035;
   public static final int PK_IN_Protect = 8036;
   public static final int Skill_Has_Activation = 8037;
   public static final int Skill_TelePoint_Block = 8038;
   public static final int Skill_TelePoint_OutRange = 8039;
   public static final int Skill_Target_Die_Treat = 8040;
   public static final int Skill_Sprint_TooClose = 8041;
   public static final int Skill_Sprint_Block = 8042;
   public static final int Skill_Essence_NoEn = 8043;
   public static final int Skill_NoPassive = 8044;
   public static final int AlreadyInGang = 9001;
   public static final int AlreadyApplyThisGang = 9002;
   public static final int ApplyTooMuch = 9003;
   public static final int TooManyApplyInThisGang = 9004;
   public static final int GangNameRepeated = 9005;
   public static final int Gang_Permissions = 9006;
   public static final int Gang_Name_OutOfRage = 9007;
   public static final int Gang_Name_Illegal = 9008;
   public static final int Gang_Other_Aready_In = 9009;
   public static final int Gang_Member_Full = 9010;
   public static final int Gang_Master_Should_Transfer = 9011;
   public static final int Gang_Quit_Title = 9012;
   public static final int Gang_Quit_Content = 9013;
   public static final int Gang_Other_NotIn = 9014;
   public static final int Gang_Cant_Do_Self = 9015;
   public static final int Gang_Announce_Illegal = 9016;
   public static final int Gang_Just_leave = 9017;
   public static final int Gang_Leave_Minutes = 9018;
   public static final int Gang_Leave_Hours = 9019;
   public static final int Gang_Leave_Days = 9020;
   public static final int Gang_Leave_Weeks = 9021;
   public static final int Gang_Already_Receive_Welfare = 9022;
   public static final int Gang_Create_Money_Not_Enough = 9023;
   public static final int Gang_LevelUp_Money_Not_Enough = 9024;
   public static final int Gang_LevelUp_Ingot_Not_Enough = 9025;
   public static final int Gang_LevelUp_Post_Error = 9026;
   public static final int Gang_LevelUp_Full = 9027;
   public static final int Gang_Member_Position = 9028;
   public static final int Gang_Member_IsViceMaster = 9029;
   public static final int Gang_Member_IsNotViceMaster = 9030;
   public static final int Gang_Request_Join_Link = 9031;
   public static final int Gang_Apply_Notice = 9032;
   public static final int Gang_NotOpen = 9033;
   public static final int Materail_Disappear = 10001;
   public static final int Material_No_Tools = 10002;
   public static final int Drop_None = 11001;
   public static final int Drop_InProtected = 11002;
   public static final int Drop_RaraBoss = 11003;
   public static final int Protection_Skill_None = 12001;
   public static final int Protection_Skill_Full = 12002;
   public static final int Protection_AP_Not_Enough = 12003;
   public static final int Protection_Level_Not_Enough = 12004;
   public static final int Mail_Default_Title = 13001;
   public static final int MailCantDelForItem = 13002;
   public static final int MailNoItem = 13003;
   public static final int Dungeon_Not_Open = 14001;
   public static final int Dungeon_AreadyIn = 14002;
   public static final int Dungeon_Invite = 14003;
   public static final int Dungeon_NotInTeam = 14004;
   public static final int Dungeon_NotTeamLeader = 14005;
   public static final int Dungeon_AlreadyInvite = 14006;
   public static final int Dungeon_InviteTitle = 14007;
   public static final int Dungeon_InviteOutOf = 14008;
   public static final int Dungeon_TicketNotEnough = 14009;
   public static final int Dungeon_DunLevelNotEnough = 14010;
   public static final int Dungeon_OutOfTimes = 14011;
   public static final int Dungeon_InCD = 14012;
   public static final int Dungeon_CannotInspire = 14013;
   public static final int Dungeon_MoneyInspireFull = 14014;
   public static final int Dungeon_InspireFull = 14015;
   public static final int Dungeon_InspireFail = 14016;
   public static final int Dungeon_LifeInspireSuccess = 14017;
   public static final int Dungeon_HurtInspireSuccess = 14018;
   public static final int Dungeon_InspireMoneyNotEnough = 14019;
   public static final int Dungeon_InspireIngotNotEnough = 14020;
   public static final int Dungeon_TeamNoticeTitle = 14021;
   public static final int Dungeon_TeamNoticeContent = 14022;
   public static final int Dungeon_InspireLifeAddition = 14023;
   public static final int Dungeon_InspireHurtAddition = 14024;
   public static final int Dungeon_ClearCdContent = 14025;
   public static final int Dungeon_ClearCdTitle = 14026;
   public static final int Dungeon_ClearCdNoNeed = 14027;
   public static final int Dungeon_TeamForceIn = 14028;
   public static final int Dungeon_MustTrial = 14029;
   public static final int Dungeon_TrialReceiveTitle = 14030;
   public static final int Dungeon_TrialReceiveContent = 14031;
   public static final int Dungeon_TrialLevelMax = 14032;
   public static final int Dungeon_MailTitle = 14033;
   public static final int Dungeon_MailContent = 14034;
   public static final int Dungeon_QuitTitle = 14035;
   public static final int Dungeon_QuitContent = 14036;
   public static final int Dungeon_TaskError = 14037;
   public static final int Dungeon_HasParticipate = 14038;
   public static final int Dungeon_isBegin = 14039;
   public static final int Dungeon_RedFortFaild = 14040;
   public static final int Dungeon_RedFortTheOne = 14041;
   public static final int Dungeon_RedFortNoOne = 14042;
   public static final int Dungeon_NotReceived = 14043;
   public static final int Dungeon_LevelLow = 14044;
   public static final int Dungeon_PlayerFull = 14045;
   public static final int Dungeon_VipLNR = 14046;
   public static final int Transaction_IsLocked = 15001;
   public static final int Transaction_NeedLock = 15002;
   public static final int Transaction_WaitOtherLock = 15003;
   public static final int Transaction_ItemPositionError = 15004;
   public static final int Transaction_PutTheSameItem = 15005;
   public static final int TransAction_ItemError = 15006;
   public static final int TransAction_ItemIsBind = 15007;
   public static final int ITEM_CANNOT_EXCHANGE = 15008;
   public static final int Transaction_NoTransNow = 15009;
   public static final int Transaction_TimeOut = 15010;
   public static final int Transaction_TooFar = 15011;
   public static final int Transaction_OtherAgree = 15012;
   public static final int Transaction_SelfAgree = 15013;
   public static final int Transaction_AgreeMessage = 15014;
   public static final int Transaction_SelfUnLock = 15015;
   public static final int Transaction_OtherUnLock = 15016;
   public static final int Transaction_AlreadyInTrans = 15017;
   public static final int Transaction_OtherInTrans = 15018;
   public static final int Transaction_WithSelf = 15019;
   public static final int Transaction_WaitOtherResponse = 15020;
   public static final int Transaction_DataError = 15021;
   public static final int Transaction_Success = 15022;
   public static final int Transaction_OtherPackageFull = 15023;
   public static final int Transaction_SelfPackageFull = 15024;
   public static final int Transaction_SelfCancel = 15025;
   public static final int Transaction_SelfConfirm = 15026;
   public static final int Transaction_Cancel = 15027;
   public static final int Transaction_Confirm = 15028;
   public static final int Transaction_MoneyOut = 15029;
   public static final int Transaction_IsFull = 15030;
   public static final int Transaction_IsRedName = 15031;
   public static final int Transaction_OtherIsRedName = 15032;
   public static final int Transaction_ItemNull = 15033;
   public static final int Buff_None = 16001;
   public static final int Buff_Hasnt_clickFunc = 16002;
   public static final int Preview_Level_Low = 16101;
   public static final int Market_NoInLocalServer = 16601;
   public static final int Market_SortImp = 16602;
   public static final int Market_ConditionIndex_err = 16603;
   public static final int Market_Count_Err = 16604;
   public static final int Market_ItemBind = 16605;
   public static final int Market_ItemType_Err = 16606;
   public static final int Market_Max_Count = 16607;
   public static final int Market_Price_min = 16608;
   public static final int Market_Item_None = 16609;
   public static final int Market_Item_Self = 16610;
   public static final int Market_Item_Off_NoSelf = 16611;
   public static final int Market_Mail_offShelveTitle = 16612;
   public static final int Market_Mail_offshelve_offLine_Content = 16613;
   public static final int Market_Mail_offShelve_onLine_content = 16614;
   public static final int Market_Call = 16615;
   public static final int Market_Buy_Text = 16616;
   public static final int Market_Sell_Record = 16617;
   public static final int Market_Buy_Record = 16618;
   public static final int Market_Up_TimeLimit = 16619;
   public static final int Market_Item_SelfUserName = 16621;
   public static final int Redeem_NoItem = 16701;
   public static final int Redeem_Item_Imp = 16702;
   public static final int Redeem_MarketItem_None = 16703;
   public static final int Redeem_Market_LimitCount_NoEn = 16704;
   public static final int Redeem_Market_Vip_NoEn = 16705;
   public static final int Redeem_MarketLabelNone = 16706;
   public static final int Redeem_Point_NoEn = 16707;
   public static final int Redeem_Count_Err = 16708;
   public static final int Chat_Vip = 17001;
   public static final int Chat_SelfTitle = 17002;
   public static final int Chat_System = 17003;
   public static final int Chat_BeBan = 17004;
   public static final int Team_AreadyIn = 19001;
   public static final int Team_Des = 19002;
   public static final int Team_NotLeader = 19003;
   public static final int Team_Full = 19004;
   public static final int Team_OtherHasTeam = 19005;
   public static final int Team_NotInSameTeam = 19006;
   public static final int Team_InSameTeam = 19007;
   public static final int Team_Disbind = 19009;
   public static final int Team_InviteLeft = 19010;
   public static final int Team_InviteRight = 19011;
   public static final int Team_InviteText = 19012;
   public static final int Team_InviteTitle = 19013;
   public static final int Team_RefuseInvite = 19014;
   public static final int Team_refuseApply = 19015;
   public static final int Team_CreateTeam = 19016;
   public static final int Team_SelfJoinTeam = 19017;
   public static final int Team_OtherJoin = 19018;
   public static final int Team_SelfOutMsg = 19019;
   public static final int Team_SelfOutAndDisbindMsg = 19020;
   public static final int Team_OtherOutMsg = 19021;
   public static final int Team_OtherOutAndDisbindMsg = 19022;
   public static final int Team_BeKickOutMsg = 19023;
   public static final int Team_KickOutMsg = 19024;
   public static final int Team_SelfBeNewLeader = 19025;
   public static final int Team_OtherBeNewLeader = 19026;
   public static final int Team_CannotThisMap = 19027;
   public static final int Prompt_Exp = 20001;
   public static final int Prompt_MasterExp = 20002;
   public static final int Prompt_Money = 20003;
   public static final int Prompt_Item = 20004;
   public static final int Prompt_Skill = 20005;
   public static final int PetCannotShow = 22010;
   public static final int PetFunctionNoOpen = 22012;
   public static final int PetLevelMoreThanDemand = 22013;
   public static final int Broadcast_GangCreate = 24001;
   public static final int Broadcast_GangCreateChannel = 24002;
   public static final int Broadcast_BossRefresh = 24003;
   public static final int Broadcast_GangMasterBeKilled = 24004;
   public static final int Broadcast_GangViceMasterBeKilled = 24005;
   public static final int Broadcast_Strengthen = 24006;
   public static final int Broadcast_Strengthen_Content = 24007;
   public static final int Broadcast_Additional = 24008;
   public static final int Broadcast_Additional_Content = 24009;
   public static final int Broadcast_Composite_Content = 24010;
   public static final int Broadcast_Composite_Link = 24011;
   public static final int License_Error = 25001;
   public static final int License_SystemBusy = 25002;
   public static final int License_BeUsed = 25003;
   public static final int License_BeReceived = 25004;
   public static final int MALL1 = 23001;
   public static final int MALL3 = 23003;
   public static final int MALL4 = 23004;
   public static final int MALL5 = 23005;
   public static final int MALL6 = 23006;
   public static final int MALL7 = 23007;
   public static final int MALL8 = 23008;
   public static final int MALL9 = 23009;
   public static final int MAll_Ticket_Buy = 23010;
   public static final int Activity_ItemOver = 25101;
   public static final int Activity_PayLow = 25102;
   public static final int Activity_TeHuiTitle = 25103;
   public static final int Activity_TeHuiContent = 25104;
   public static final int Title_Has = 25201;
   public static final int Title_NotExist = 25202;
   public static final int VIP_ACTIVE = 23100;
   public static final int BlueVip_MailTitle = 23101;
   public static final int BlueVip_MailContent = 23102;
   public static final int SevenDayHasFound = 23201;
   public static final int SevenDayCountZero = 23202;
   public static final int Spirit_MaxLevel = 23301;
   public static final int Spirit_FunNoOpen = 23302;
   public static final int Spirit_ItemCountMax = 23303;
   public static final int Spirit_RankNoEn = 23304;
   public static final int Spirit_Item_TypeImp = 23305;
   public static final int Spirit_Item_NoExcellent = 23306;
   public static final int Spirit_NoItem = 23307;
   public static final int Spirit_IngotRefineMaxCount = 23308;
   public static final int Spirit_Refine_ConsumeDes = 23309;
   public static final int Spirit_RefineNoComfirm = 23310;
   public static final int Hallow_MaxRank = 23401;
   public static final int Hallow_LevelNoEn = 23402;
   public static final int Hallow_TreasureLevelNoEn = 23403;
   public static final int Hallow_LevelMax = 23404;
   public static final int Hallow_FunNoOpen = 23405;
   public static final int Hallow_TreasureDes = 23406;
   public static final int LuckyTurnTable_CountNoEn = 23408;
   public static final int LuckyTurnTable_TimeOver = 23409;
   public static final int LuckyTurn_MaxCount = 23410;
   public static final int LuckyTurnTable_Vip = 23411;
   public static final int LuckyTurnTable_Ingot = 23412;

   public static String getText(int messageId) {
      String msg = (String)allMessages.get(messageId);
      return msg != null ? msg : "";
   }

   public static void init(InputStream in) throws Exception {
      SAXBuilder sb = new SAXBuilder();
      Document doc = sb.build(in);
      Element root = doc.getRootElement();
      List childrenSort = root.getChildren("sort");

      for(int i = 0; i < childrenSort.size(); ++i) {
         Element child = (Element)childrenSort.get(i);
         List childrenMessage = child.getChildren("type");
         Iterator var9 = childrenMessage.iterator();

         while(var9.hasNext()) {
            Element typeElement = (Element)var9.next();
            int id = typeElement.getAttribute("id").getIntValue();
            String des = typeElement.getAttributeValue("des");
            allMessages.put(id, des);
         }
      }

   }
}
