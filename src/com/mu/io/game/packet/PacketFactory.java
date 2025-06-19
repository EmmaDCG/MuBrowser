package com.mu.io.game.packet;

import com.mu.game.model.pet.PetShowHide;
import com.mu.io.game.packet.imp.account.CreateRole;
import com.mu.io.game.packet.imp.account.DeleteRole;
import com.mu.io.game.packet.imp.account.DisconnectByClient;
import com.mu.io.game.packet.imp.account.DisconnectByInterRelogin;
import com.mu.io.game.packet.imp.account.GetRandomName;
import com.mu.io.game.packet.imp.account.GetRoleList;
import com.mu.io.game.packet.imp.account.RequestCreateRoleInfo;
import com.mu.io.game.packet.imp.account.RoleClickStart;
import com.mu.io.game.packet.imp.achieve.InitAchiveement;
import com.mu.io.game.packet.imp.activity.ActivityBaseInfo;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.io.game.packet.imp.activity.ActivityReceive;
import com.mu.io.game.packet.imp.activity.BlueOneKeyReceive;
import com.mu.io.game.packet.imp.activity.InitActivityRoleLogs;
import com.mu.io.game.packet.imp.activity.InitActivityUserLogs;
import com.mu.io.game.packet.imp.activity.RequestBlueRenew;
import com.mu.io.game.packet.imp.attack.AttackCreature;
import com.mu.io.game.packet.imp.buff.ClickBuff;
import com.mu.io.game.packet.imp.chat.ReceiveNotPrivateMessage;
import com.mu.io.game.packet.imp.chat.ReceivePrivateMessage;
import com.mu.io.game.packet.imp.chat.RequestSiLiao;
import com.mu.io.game.packet.imp.collection.CollectionInfo;
import com.mu.io.game.packet.imp.collection.CollectionReceive;
import com.mu.io.game.packet.imp.composite.ComAKeyFilter;
import com.mu.io.game.packet.imp.composite.ComFilterMaterial;
import com.mu.io.game.packet.imp.composite.CompositeItem;
import com.mu.io.game.packet.imp.composite.RequestComConfigure;
import com.mu.io.game.packet.imp.composite.RequestComPreview;
import com.mu.io.game.packet.imp.dialog.ChooseDialogOption;
import com.mu.io.game.packet.imp.dialog.CloseDialog;
import com.mu.io.game.packet.imp.dialog.DialogEnd;
import com.mu.io.game.packet.imp.dm.MenuClick;
import com.mu.io.game.packet.imp.drop.PickoutItem;
import com.mu.io.game.packet.imp.drop.WellDrop;
import com.mu.io.game.packet.imp.dungeon.BigDevilTopRequest;
import com.mu.io.game.packet.imp.dungeon.DunEnterMoLian;
import com.mu.io.game.packet.imp.dungeon.DunRequestShortcutBuy;
import com.mu.io.game.packet.imp.dungeon.DunTimingPanel;
import com.mu.io.game.packet.imp.dungeon.DunTrialReceive;
import com.mu.io.game.packet.imp.dungeon.DungeonAutoNext;
import com.mu.io.game.packet.imp.dungeon.DungeonInitRecover;
import com.mu.io.game.packet.imp.dungeon.DungeonInspire;
import com.mu.io.game.packet.imp.dungeon.DungeonMutiReceive;
import com.mu.io.game.packet.imp.dungeon.DungeonReceiveBigDevilReceive;
import com.mu.io.game.packet.imp.dungeon.EnterDungeon;
import com.mu.io.game.packet.imp.dungeon.QuitDungeon;
import com.mu.io.game.packet.imp.dungeon.RedFortReceive;
import com.mu.io.game.packet.imp.dungeon.RequestDungeonInfo;
import com.mu.io.game.packet.imp.equip.ConversionStone;
import com.mu.io.game.packet.imp.equip.DuraRepair;
import com.mu.io.game.packet.imp.equip.EquipItem;
import com.mu.io.game.packet.imp.equip.HorseFusion;
import com.mu.io.game.packet.imp.equip.InheritEquipment;
import com.mu.io.game.packet.imp.equip.RequestConversionStone;
import com.mu.io.game.packet.imp.equip.RequestDuraRepairMoney;
import com.mu.io.game.packet.imp.equip.RequestForgingEquip;
import com.mu.io.game.packet.imp.equip.RequestHorseFuse;
import com.mu.io.game.packet.imp.equip.RequestInherit;
import com.mu.io.game.packet.imp.equip.RequestRuneInherit;
import com.mu.io.game.packet.imp.equip.RequestRuneMosaic;
import com.mu.io.game.packet.imp.equip.RequestStoneMosaic;
import com.mu.io.game.packet.imp.equip.RequestStoneStats;
import com.mu.io.game.packet.imp.equip.RequestStrength;
import com.mu.io.game.packet.imp.equip.RequestUpgrade;
import com.mu.io.game.packet.imp.equip.RequestZhuija;
import com.mu.io.game.packet.imp.equip.RuneInherit;
import com.mu.io.game.packet.imp.equip.RuneMosaic;
import com.mu.io.game.packet.imp.equip.RuneUnMosaic;
import com.mu.io.game.packet.imp.equip.StoneMosaic;
import com.mu.io.game.packet.imp.equip.StoneUnMosaic;
import com.mu.io.game.packet.imp.equip.StrengthEquipment;
import com.mu.io.game.packet.imp.equip.UnEquipItem;
import com.mu.io.game.packet.imp.equip.UpgradeItem;
import com.mu.io.game.packet.imp.equip.ZhuijiaEquipment;
import com.mu.io.game.packet.imp.extarget.ExTargetGetInfo;
import com.mu.io.game.packet.imp.extarget.ExTargetGetWay;
import com.mu.io.game.packet.imp.extarget.ExTargetInitCollected;
import com.mu.io.game.packet.imp.extarget.ExTargetInitReceived;
import com.mu.io.game.packet.imp.extarget.ExTargetReceive;
import com.mu.io.game.packet.imp.financing.FinancingBuy;
import com.mu.io.game.packet.imp.financing.FinancingInform;
import com.mu.io.game.packet.imp.financing.FinancingReceive;
import com.mu.io.game.packet.imp.financing.InitFinancing;
import com.mu.io.game.packet.imp.friend.AddBlack;
import com.mu.io.game.packet.imp.friend.AddFriend;
import com.mu.io.game.packet.imp.friend.DeleteFriend;
import com.mu.io.game.packet.imp.friend.FriendAgreeOrRefuseApply;
import com.mu.io.game.packet.imp.friend.FriendBless;
import com.mu.io.game.packet.imp.friend.FriendWish;
import com.mu.io.game.packet.imp.friend.GetBlessInfo;
import com.mu.io.game.packet.imp.friend.GetFriendApplyList;
import com.mu.io.game.packet.imp.friend.GetFriendTipsInfo;
import com.mu.io.game.packet.imp.friend.GetFriendsInfo;
import com.mu.io.game.packet.imp.friend.GetWishPoolInfo;
import com.mu.io.game.packet.imp.friend.InitFriend;
import com.mu.io.game.packet.imp.gang.AgreeOrRefuseInGang;
import com.mu.io.game.packet.imp.gang.AppointViceMaster;
import com.mu.io.game.packet.imp.gang.AppointWarPost;
import com.mu.io.game.packet.imp.gang.ChangeApplyStatus;
import com.mu.io.game.packet.imp.gang.CreateGang;
import com.mu.io.game.packet.imp.gang.EditGangAnnouncement;
import com.mu.io.game.packet.imp.gang.GangBaseInfo;
import com.mu.io.game.packet.imp.gang.GangDonate;
import com.mu.io.game.packet.imp.gang.GangDynamicInfo;
import com.mu.io.game.packet.imp.gang.GangFlagConfigs;
import com.mu.io.game.packet.imp.gang.GangLevelUp;
import com.mu.io.game.packet.imp.gang.GangQualification;
import com.mu.io.game.packet.imp.gang.GangRequestZml;
import com.mu.io.game.packet.imp.gang.GangWarDailyRewardInfo;
import com.mu.io.game.packet.imp.gang.GangWarPanelInfo;
import com.mu.io.game.packet.imp.gang.GetGangApply;
import com.mu.io.game.packet.imp.gang.GetGangList;
import com.mu.io.game.packet.imp.gang.GetGangRedPacket;
import com.mu.io.game.packet.imp.gang.KickOutFromGang;
import com.mu.io.game.packet.imp.gang.OpenRedPacket;
import com.mu.io.game.packet.imp.gang.QuitGang;
import com.mu.io.game.packet.imp.gang.ReceiveWarDaily;
import com.mu.io.game.packet.imp.gang.ReceiveWelfare;
import com.mu.io.game.packet.imp.gang.RequestWelfareInfo;
import com.mu.io.game.packet.imp.gang.RequstGangLevelupInfo;
import com.mu.io.game.packet.imp.gang.SelfRedPacket;
import com.mu.io.game.packet.imp.gang.SendRedPacket;
import com.mu.io.game.packet.imp.gang.SummonOrEnterBoss;
import com.mu.io.game.packet.imp.gang.TransferMaster;
import com.mu.io.game.packet.imp.guide.ClickComposeNotice;
import com.mu.io.game.packet.imp.guide.FunctionPreviewReceive;
import com.mu.io.game.packet.imp.guide.InitFunctionOpenTime;
import com.mu.io.game.packet.imp.hallows.HallowDetail;
import com.mu.io.game.packet.imp.hallows.RefineHallows;
import com.mu.io.game.packet.imp.hallows.ShowHallowStats;
import com.mu.io.game.packet.imp.init.InitArrowGuide;
import com.mu.io.game.packet.imp.init.InitBuffs;
import com.mu.io.game.packet.imp.init.InitDailyReceiveLogs;
import com.mu.io.game.packet.imp.init.InitDunLogs;
import com.mu.io.game.packet.imp.init.InitDunTotalLogs;
import com.mu.io.game.packet.imp.init.InitEnd;
import com.mu.io.game.packet.imp.init.InitFirstkill;
import com.mu.io.game.packet.imp.init.InitHallows;
import com.mu.io.game.packet.imp.init.InitHangset;
import com.mu.io.game.packet.imp.init.InitItemLimits;
import com.mu.io.game.packet.imp.init.InitItems;
import com.mu.io.game.packet.imp.init.InitLuckyTurnTable;
import com.mu.io.game.packet.imp.init.InitMail;
import com.mu.io.game.packet.imp.init.InitMailItem;
import com.mu.io.game.packet.imp.init.InitRedPacket;
import com.mu.io.game.packet.imp.init.InitRoleDropCounts;
import com.mu.io.game.packet.imp.init.InitShortcut;
import com.mu.io.game.packet.imp.init.InitSkills;
import com.mu.io.game.packet.imp.init.InitSnLogs;
import com.mu.io.game.packet.imp.init.InitSpirit;
import com.mu.io.game.packet.imp.init.InitStorage;
import com.mu.io.game.packet.imp.init.InitTrialReceiveLogs;
import com.mu.io.game.packet.imp.item.BuyNpcGood;
import com.mu.io.game.packet.imp.item.DiscardItem;
import com.mu.io.game.packet.imp.item.ExchangePosition;
import com.mu.io.game.packet.imp.item.GetItemWay;
import com.mu.io.game.packet.imp.item.MoveItemFromDeport;
import com.mu.io.game.packet.imp.item.MoveItemToDeport;
import com.mu.io.game.packet.imp.item.OpenVipShop;
import com.mu.io.game.packet.imp.item.QuickSale;
import com.mu.io.game.packet.imp.item.QuickSaleCalMoney;
import com.mu.io.game.packet.imp.item.RepurchaseItemFromNpc;
import com.mu.io.game.packet.imp.item.RequestQuickSale;
import com.mu.io.game.packet.imp.item.SeeShowItem;
import com.mu.io.game.packet.imp.item.SellItemToNPC;
import com.mu.io.game.packet.imp.item.SplitItem;
import com.mu.io.game.packet.imp.item.UseItem;
import com.mu.io.game.packet.imp.luckyTurnTabel.LuckyTurnTabelRecord;
import com.mu.io.game.packet.imp.luckyTurnTabel.RequestLuckyTurnTableInfo;
import com.mu.io.game.packet.imp.luckyTurnTabel.TurnLuckyTable;
import com.mu.io.game.packet.imp.magicItem.BuyMagicMarketItem;
import com.mu.io.game.packet.imp.magicItem.GetMagicItemRecord;
import com.mu.io.game.packet.imp.magicItem.MoveMagicToBackpack;
import com.mu.io.game.packet.imp.magicItem.OpenMagicPanel;
import com.mu.io.game.packet.imp.magicItem.OperateMagicItem;
import com.mu.io.game.packet.imp.magicItem.ShowMagicMarketItems;
import com.mu.io.game.packet.imp.mail.DeleteAllReadMail;
import com.mu.io.game.packet.imp.mail.DeleteMail;
import com.mu.io.game.packet.imp.mail.ReceiveAllMailItem;
import com.mu.io.game.packet.imp.mail.ReceiveMailItem;
import com.mu.io.game.packet.imp.mail.RequestMailDetail;
import com.mu.io.game.packet.imp.mail.RequestMailList;
import com.mu.io.game.packet.imp.mall.MallBuy;
import com.mu.io.game.packet.imp.mall.MallByItemModel;
import com.mu.io.game.packet.imp.mall.MallConfig;
import com.mu.io.game.packet.imp.mall.MallOpenLink;
import com.mu.io.game.packet.imp.mall.ShortcutBuyAndUse;
import com.mu.io.game.packet.imp.map.EnterMap;
import com.mu.io.game.packet.imp.map.EnterMapSuccess;
import com.mu.io.game.packet.imp.map.GetAllMapId;
import com.mu.io.game.packet.imp.map.GetSmallMapElement;
import com.mu.io.game.packet.imp.map.MapSwitchRequest;
import com.mu.io.game.packet.imp.map.MapTeleport;
import com.mu.io.game.packet.imp.map.RequestTransPoint;
import com.mu.io.game.packet.imp.map.RobotSwitchMap;
import com.mu.io.game.packet.imp.map.SwitchLine;
import com.mu.io.game.packet.imp.map.TransferMap;
import com.mu.io.game.packet.imp.market.BuyMarketItem;
import com.mu.io.game.packet.imp.market.MarketConfig;
import com.mu.io.game.packet.imp.market.MarketItemUpShelve;
import com.mu.io.game.packet.imp.market.MarketRecords;
import com.mu.io.game.packet.imp.market.OffShelveMarketItem;
import com.mu.io.game.packet.imp.market.PublicMarketItem;
import com.mu.io.game.packet.imp.market.RequestMarketItem;
import com.mu.io.game.packet.imp.market.RequestMarketItemAtom;
import com.mu.io.game.packet.imp.material.CollectionMaterial;
import com.mu.io.game.packet.imp.monster.BossTeleport;
import com.mu.io.game.packet.imp.monster.RequestBossInfo;
import com.mu.io.game.packet.imp.monster.RequestBossTelTime;
import com.mu.io.game.packet.imp.npc.ChatWithNpc;
import com.mu.io.game.packet.imp.pet.InitPet;
import com.mu.io.game.packet.imp.pet.PetAttributeRise;
import com.mu.io.game.packet.imp.pet.PetOpen;
import com.mu.io.game.packet.imp.pet.PetRise;
import com.mu.io.game.packet.imp.pet.PetRiseInform;
import com.mu.io.game.packet.imp.pet.PetRiseStop;
import com.mu.io.game.packet.imp.pkModel.SetPkMode;
import com.mu.io.game.packet.imp.player.AllocatePotential;
import com.mu.io.game.packet.imp.player.FrameCheck;
import com.mu.io.game.packet.imp.player.GMCommand;
import com.mu.io.game.packet.imp.player.GetTopList;
import com.mu.io.game.packet.imp.player.Heartbeat;
import com.mu.io.game.packet.imp.player.InitBossTelTime;
import com.mu.io.game.packet.imp.player.InitPay;
import com.mu.io.game.packet.imp.player.InitSevenDayTreasure;
import com.mu.io.game.packet.imp.player.Move;
import com.mu.io.game.packet.imp.player.OtherPlayerDetail;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import com.mu.io.game.packet.imp.player.PlayerRevival;
import com.mu.io.game.packet.imp.player.PlayerSystemSetup;
import com.mu.io.game.packet.imp.player.PlayerZening;
import com.mu.io.game.packet.imp.player.PotentialRocommoned;
import com.mu.io.game.packet.imp.player.PreCalPotential;
import com.mu.io.game.packet.imp.player.PrepareMoveToRemoteServer;
import com.mu.io.game.packet.imp.player.ReceiveSnGift;
import com.mu.io.game.packet.imp.player.RequestGameGuide;
import com.mu.io.game.packet.imp.player.RequestToInterServer;
import com.mu.io.game.packet.imp.player.SevenDayFindItem;
import com.mu.io.game.packet.imp.player.SevenDayPush;
import com.mu.io.game.packet.imp.player.StopMove;
import com.mu.io.game.packet.imp.player.UserSystemSetup;
import com.mu.io.game.packet.imp.player.WashPotential;
import com.mu.io.game.packet.imp.player.hangset.EndHang;
import com.mu.io.game.packet.imp.player.hangset.HangSet;
import com.mu.io.game.packet.imp.player.hangset.RequestHangSaleCondition;
import com.mu.io.game.packet.imp.player.hangset.StartHang;
import com.mu.io.game.packet.imp.player.offline.GetOfflineBuffInfo;
import com.mu.io.game.packet.imp.player.offline.GetOfflineRecoveryInfo;
import com.mu.io.game.packet.imp.player.offline.InitOfflineSystem;
import com.mu.io.game.packet.imp.player.offline.OfflineDungeonRecover;
import com.mu.io.game.packet.imp.player.pop.DealPopup;
import com.mu.io.game.packet.imp.player.tansaction.AcceptTransaction;
import com.mu.io.game.packet.imp.player.tansaction.ClosePanel;
import com.mu.io.game.packet.imp.player.tansaction.ConfirmTransactionByPlayer;
import com.mu.io.game.packet.imp.player.tansaction.DeleteItemByPlayer;
import com.mu.io.game.packet.imp.player.tansaction.InitTransaction;
import com.mu.io.game.packet.imp.player.tansaction.LockTransactionByPlayer;
import com.mu.io.game.packet.imp.player.tansaction.PlayerPutItem;
import com.mu.io.game.packet.imp.player.tansaction.PutMoneyByPlayer;
import com.mu.io.game.packet.imp.player.tansaction.RefuseTransaction;
import com.mu.io.game.packet.imp.player.tansaction.UnLockByPlayer;
import com.mu.io.game.packet.imp.player.tips.ExpiredPandaShortcutBuy;
import com.mu.io.game.packet.imp.rewardhall.online.OnlineRewardInform;
import com.mu.io.game.packet.imp.rewardhall.online.OnlineRewardInit;
import com.mu.io.game.packet.imp.rewardhall.online.OnlineRewardReceiveDay;
import com.mu.io.game.packet.imp.rewardhall.online.OnlineRewardReceiveWeek;
import com.mu.io.game.packet.imp.rewardhall.sign.SignBefore;
import com.mu.io.game.packet.imp.rewardhall.sign.SignInform;
import com.mu.io.game.packet.imp.rewardhall.sign.SignInit;
import com.mu.io.game.packet.imp.rewardhall.sign.SignReward;
import com.mu.io.game.packet.imp.rewardhall.sign.SignToday;
import com.mu.io.game.packet.imp.rewardhall.vitality.VitalityEnter;
import com.mu.io.game.packet.imp.rewardhall.vitality.VitalityInform;
import com.mu.io.game.packet.imp.rewardhall.vitality.VitalityInit;
import com.mu.io.game.packet.imp.rewardhall.vitality.VitalityReward;
import com.mu.io.game.packet.imp.shield.InitShield;
import com.mu.io.game.packet.imp.shield.ShieldInform;
import com.mu.io.game.packet.imp.shield.ShieldLevelJQDZ;
import com.mu.io.game.packet.imp.shield.ShieldRiseLevel;
import com.mu.io.game.packet.imp.shield.ShieldRiseRank;
import com.mu.io.game.packet.imp.shortcut.DelShortcut;
import com.mu.io.game.packet.imp.shortcut.ExchangeShortcut;
import com.mu.io.game.packet.imp.shortcut.SetShortcut;
import com.mu.io.game.packet.imp.skill.LearnSkill;
import com.mu.io.game.packet.imp.skill.RequestPassiveSkill;
import com.mu.io.game.packet.imp.skill.RequestSkillShortcutPanel;
import com.mu.io.game.packet.imp.skill.SelectSkill;
import com.mu.io.game.packet.imp.skill.UseSkill;
import com.mu.io.game.packet.imp.spiritOfWar.RequestSpiritDetail;
import com.mu.io.game.packet.imp.spiritOfWar.RequestSpiritItems;
import com.mu.io.game.packet.imp.spiritOfWar.SpiritCalExp;
import com.mu.io.game.packet.imp.spiritOfWar.SpiritConditionConfig;
import com.mu.io.game.packet.imp.spiritOfWar.SpiritRefine;
import com.mu.io.game.packet.imp.storage.BackpackCount;
import com.mu.io.game.packet.imp.storage.BackpackExpand;
import com.mu.io.game.packet.imp.storage.DeportExpand;
import com.mu.io.game.packet.imp.storage.DeportOpen;
import com.mu.io.game.packet.imp.storage.RequestBackpackExpand;
import com.mu.io.game.packet.imp.storage.SortoutStorage;
import com.mu.io.game.packet.imp.sys.ClientInfo;
import com.mu.io.game.packet.imp.sys.OpenFcmPannel;
import com.mu.io.game.packet.imp.sys.OpenPersonalBoss;
import com.mu.io.game.packet.imp.sys.OpenQqPay;
import com.mu.io.game.packet.imp.sys.ReadListPacket;
import com.mu.io.game.packet.imp.sys.RequestBlueVipKt;
import com.mu.io.game.packet.imp.sys.RequestQqPay;
import com.mu.io.game.packet.imp.sys.TransferPlayerBaseInfo;
import com.mu.io.game.packet.imp.tanxian.ReceiveChestReward;
import com.mu.io.game.packet.imp.tanxian.TanXianContinue;
import com.mu.io.game.packet.imp.tanxian.TanXianCountTip;
import com.mu.io.game.packet.imp.tanxian.TanXianDesInform;
import com.mu.io.game.packet.imp.tanxian.TanXianInform;
import com.mu.io.game.packet.imp.tanxian.TanXianInit;
import com.mu.io.game.packet.imp.tanxian.TanXianStart;
import com.mu.io.game.packet.imp.task.AcceptTask;
import com.mu.io.game.packet.imp.task.BuyRC;
import com.mu.io.game.packet.imp.task.DrawZXReward;
import com.mu.io.game.packet.imp.task.InitTask;
import com.mu.io.game.packet.imp.task.OpenXSUseItem;
import com.mu.io.game.packet.imp.task.OpenZXDetail;
import com.mu.io.game.packet.imp.task.RefreshRCStar;
import com.mu.io.game.packet.imp.task.SubmitRCTask;
import com.mu.io.game.packet.imp.task.SubmitTask;
import com.mu.io.game.packet.imp.team.TeamAgreeApply;
import com.mu.io.game.packet.imp.team.TeamAgreeInvite;
import com.mu.io.game.packet.imp.team.TeamApply;
import com.mu.io.game.packet.imp.team.TeamAround;
import com.mu.io.game.packet.imp.team.TeamAroundPlayers;
import com.mu.io.game.packet.imp.team.TeamChangeLeader;
import com.mu.io.game.packet.imp.team.TeamCreateSelf;
import com.mu.io.game.packet.imp.team.TeamInvite;
import com.mu.io.game.packet.imp.team.TeamKickOut;
import com.mu.io.game.packet.imp.team.TeamLeft;
import com.mu.io.game.packet.imp.team.TeamRefreshMateInfo;
import com.mu.io.game.packet.imp.team.TeamRefuseApply;
import com.mu.io.game.packet.imp.team.TeamRefuseInvite;
import com.mu.io.game.packet.imp.test.SwitchServer;
import com.mu.io.game.packet.imp.test.TestProtocal;
import com.mu.io.game.packet.imp.title.EquipTitle;
import com.mu.io.game.packet.imp.title.InitTitle;
import com.mu.io.game.packet.imp.transfer.TransferAkeyComplete;
import com.mu.io.game.packet.imp.transfer.TransferInform;
import com.mu.io.game.packet.imp.vip.InitVIP;
import com.mu.io.game.packet.imp.vip.VIPContinue;
import com.mu.io.game.packet.imp.vip.VIPOpen;
import com.mu.io.game2gateway.packet.imp.sys.GS_AskIp;

public class PacketFactory {
   public static ReadAndWritePacket getPacket(int code, byte[] readBuf) {
      switch(code) {
      case 101:
         return new TestProtocal(code, readBuf);
      case 102:
         return new SwitchServer(code, readBuf);
      case 105:
         return new DisconnectByInterRelogin(code, readBuf);
      case 106:
         return new RobotSwitchMap(code, readBuf);
      case 107:
         return new DisconnectByClient(code, readBuf);
      case 108:
         return new TransferPlayerBaseInfo(code, readBuf);
      case 109:
         return new PrepareMoveToRemoteServer(code, readBuf);
      case 111:
         return new RequestToInterServer(code, readBuf);
      case 115:
         return new ClientInfo(code, readBuf);
      case 116:
         return new GS_AskIp(code, readBuf);
      case 1002:
         return new OpenFcmPannel(code, readBuf);
      case 1014:
         return new OpenQqPay(code, readBuf);
      case 1015:
         return new RequestQqPay(code, readBuf);
      case 1017:
         return new RequestBlueVipKt(code, readBuf);
      case 1019:
         return new ExpiredPandaShortcutBuy(code, readBuf);
      case 1020:
         return new OpenPersonalBoss(code, readBuf);
      case 10000:
         return new GetRoleList(code, readBuf);
      case 10001:
         return new GetRandomName(code, readBuf);
      case 10002:
         return new CreateRole(code, readBuf);
      case 10003:
         return new PlayerAttributes(code, readBuf);
      case 10004:
         return new InitEnd(code, readBuf);
      case 10006:
         return new DealPopup(code, readBuf);
      case 10007:
         return new PlayerZening(code, readBuf);
      case 10008:
         return new GMCommand(code, readBuf);
      case 10009:
         return new PlayerRevival(code, readBuf);
      case 10011:
         return new PlayerSystemSetup(code, readBuf);
      case 10012:
         return new HangSet(code, readBuf);
      case 10013:
         return new StartHang(code, readBuf);
      case 10014:
         return new EndHang(code, readBuf);
      case 10018:
         return new DeleteRole(code, readBuf);
      case 10020:
         return new UserSystemSetup(code, readBuf);
      case 10023:
         return new InitArrowGuide(code, readBuf);
      case 10026:
         return new ReceiveSnGift(code, readBuf);
      case 10027:
         return new InitSnLogs(code, readBuf);
      case 10030:
         return new FunctionPreviewReceive(code, readBuf);
      case 10032:
         return new ShortcutBuyAndUse(code, readBuf);
      case 10033:
         return new InitDailyReceiveLogs(code, readBuf);
      case 10034:
         return new RequestHangSaleCondition(code, readBuf);
      case 10035:
         return new RequestCreateRoleInfo(code, readBuf);
      case 10036:
         return new InitPay(code, readBuf);
      case 10037:
         return new InitBossTelTime(code, readBuf);
      case 10042:
         return new RequestGameGuide(code, readBuf);
      case 10044:
         return new RoleClickStart(code, readBuf);
      case 10047:
         return new InitAchiveement(code, readBuf);
      case 10048:
         return new ClickComposeNotice(code, readBuf);
      case 10049:
         return new InitOfflineSystem(code, readBuf);
      case 10050:
         return new InitFunctionOpenTime(code, readBuf);
      case 10051:
         return new InitRedPacket(code, readBuf);
      case 10103:
         return new EnterMap(code, readBuf);
      case 10104:
         return new EnterMapSuccess(code, readBuf);
      case 10106:
         return new TransferMap(code, readBuf);
      case 10108:
         return new GetSmallMapElement(code, readBuf);
      case 10109:
         return new RequestTransPoint(code, readBuf);
      case 10110:
         return new MapTeleport(code, readBuf);
      case 10112:
         return new SwitchLine(code, readBuf);
      case 10113:
         return new MapSwitchRequest(code, readBuf);
      case 10115:
         return new GetAllMapId(code, readBuf);
      case 10150:
         return new GetOfflineBuffInfo(code, readBuf);
      case 10151:
         return new GetOfflineRecoveryInfo(code, readBuf);
      case 10152:
         return new OfflineDungeonRecover(code, readBuf);
      case 10201:
         return new Move(code, readBuf);
      case 10202:
         return new FrameCheck(code, readBuf);
      case 10203:
         return new StopMove(code, readBuf);
      case 10207:
         return new AllocatePotential(code, readBuf);
      case 10208:
         return new WashPotential(code, readBuf);
      case 10212:
         return new DelShortcut(code, readBuf);
      case 10213:
         return new SetShortcut(code, readBuf);
      case 10214:
         return new ExchangeShortcut(code, readBuf);
      case 10245:
         return new PotentialRocommoned(code, readBuf);
      case 10246:
         return new PreCalPotential(code, readBuf);
      case 10249:
         return new OtherPlayerDetail(code, readBuf);
      case 10250:
         return new GetTopList(code, readBuf);
      case 10251:
         return new Heartbeat(code, readBuf);
      case 10302:
         return new RequestBossInfo(code, readBuf);
      case 10305:
         return new BossTeleport(code, readBuf);
      case 10306:
         return new RequestBossTelTime(code, readBuf);
      case 10401:
         return new ChatWithNpc(code, readBuf);
      case 10403:
         return new CloseDialog(code, readBuf);
      case 10404:
         return new DialogEnd(code, readBuf);
      case 10405:
         return new ChooseDialogOption(code, readBuf);
      case 10500:
         return new ReceivePrivateMessage(code, readBuf);
      case 10501:
         return new ReceiveNotPrivateMessage(code, readBuf);
      case 10508:
         return new RequestSiLiao(code, readBuf);
      case 10600:
         return new GangBaseInfo(code, readBuf);
      case 10605:
         return new EditGangAnnouncement(code, readBuf);
      case 10606:
         return new GetGangList(code, readBuf);
      case 10607:
         return new ChangeApplyStatus(code, readBuf);
      case 10608:
         return new CreateGang(code, readBuf);
      case 10609:
         return new GangFlagConfigs(code, readBuf);
      case 10612:
         return new GetGangApply(code, readBuf);
      case 10613:
         return new AgreeOrRefuseInGang(code, readBuf);
      case 10615:
         return new TransferMaster(code, readBuf);
      case 10616:
         return new AppointViceMaster(code, readBuf);
      case 10617:
         return new KickOutFromGang(code, readBuf);
      case 10618:
         return new QuitGang(code, readBuf);
      case 10619:
         return new RequstGangLevelupInfo(code, readBuf);
      case 10620:
         return new GangLevelUp(code, readBuf);
      case 10621:
         return new RequestWelfareInfo(code, readBuf);
      case 10622:
         return new ReceiveWelfare(code, readBuf);
      case 10623:
         return new GangDynamicInfo(code, readBuf);
      case 10625:
         return new GangWarPanelInfo(code, readBuf);
      case 10626:
         return new AppointWarPost(code, readBuf);
      case 10628:
         return new GangQualification(code, readBuf);
      case 10629:
         return new GangWarDailyRewardInfo(code, readBuf);
      case 10630:
         return new ReceiveWarDaily(code, readBuf);
      case 10634:
         return new GangRequestZml(code, readBuf);
      case 10635:
         return new GangDonate(code, readBuf);
      case 10636:
         return new SelfRedPacket(code, readBuf);
      case 10638:
         return new SendRedPacket(code, readBuf);
      case 10639:
         return new GetGangRedPacket(code, readBuf);
      case 10640:
         return new OpenRedPacket(code, readBuf);
      case 10643:
         return new SummonOrEnterBoss(code, readBuf);
      case 10701:
         return new CollectionMaterial(code, readBuf);
      case 10800:
         return new InitActivityRoleLogs(code, readBuf);
      case 10801:
         return new ActivityBaseInfo(code, readBuf);
      case 10802:
         return new ActivityInfo(code, readBuf);
      case 10803:
         return new ActivityReceive(code, readBuf);
      case 10804:
         return new InitActivityUserLogs(code, readBuf);
      case 10805:
         return new BlueOneKeyReceive(code, readBuf);
      case 10806:
         return new RequestBlueRenew(code, readBuf);
      case 10851:
         return new CollectionInfo(code, readBuf);
      case 10852:
         return new CollectionReceive(code, readBuf);
      case 10900:
         return new InitMail(code, readBuf);
      case 10901:
         return new InitMailItem(code, readBuf);
      case 10904:
         return new RequestMailList(code, readBuf);
      case 10905:
         return new RequestMailDetail(code, readBuf);
      case 10906:
         return new ReceiveMailItem(code, readBuf);
      case 10907:
         return new ReceiveAllMailItem(code, readBuf);
      case 10908:
         return new DeleteMail(code, readBuf);
      case 10909:
         return new DeleteAllReadMail(code, readBuf);
      case 11000:
         return new TeamAround(code, readBuf);
      case 11001:
         return new TeamAroundPlayers(code, readBuf);
      case 11002:
         return new TeamApply(code, readBuf);
      case 11003:
         return new TeamAgreeApply(code, readBuf);
      case 11004:
         return new TeamInvite(code, readBuf);
      case 11005:
         return new TeamAgreeInvite(code, readBuf);
      case 11006:
         return new TeamCreateSelf(code, readBuf);
      case 11008:
         return new TeamRefreshMateInfo(code, readBuf);
      case 11009:
         return new TeamLeft(code, readBuf);
      case 11010:
         return new TeamKickOut(code, readBuf);
      case 11012:
         return new TeamChangeLeader(code, readBuf);
      case 11013:
         return new TeamRefuseApply(code, readBuf);
      case 11014:
         return new TeamRefuseInvite(code, readBuf);
      case 11102:
         return new MenuClick(code, readBuf);
      case 11200:
         return new InitFriend(code, readBuf);
      case 11202:
         return new GetFriendsInfo(code, readBuf);
      case 11203:
         return new GetFriendTipsInfo(code, readBuf);
      case 11204:
         return new AddFriend(code, readBuf);
      case 11205:
         return new DeleteFriend(code, readBuf);
      case 11206:
         return new GetFriendApplyList(code, readBuf);
      case 11207:
         return new FriendAgreeOrRefuseApply(code, readBuf);
      case 11212:
         return new AddBlack(code, readBuf);
      case 11213:
         return new GetWishPoolInfo(code, readBuf);
      case 11214:
         return new FriendBless(code, readBuf);
      case 11215:
         return new FriendWish(code, readBuf);
      case 11216:
         return new GetBlessInfo(code, readBuf);
      case 12000:
         return new InitDunLogs(code, readBuf);
      case 12001:
         return new RequestDungeonInfo(code, readBuf);
      case 12004:
         return new EnterDungeon(code, readBuf);
      case 12005:
         return new QuitDungeon(code, readBuf);
      case 12007:
         return new DungeonInspire(code, readBuf);
      case 12011:
         return new DunTrialReceive(code, readBuf);
      case 12012:
         return new InitTrialReceiveLogs(code, readBuf);
      case 12016:
         return new DungeonMutiReceive(code, readBuf);
      case 12017:
         return new RedFortReceive(code, readBuf);
      case 12018:
         return new DungeonAutoNext(code, readBuf);
      case 12019:
         return new BigDevilTopRequest(code, readBuf);
      case 12020:
         return new DungeonReceiveBigDevilReceive(code, readBuf);
      case 12021:
         return new DunTimingPanel(code, readBuf);
      case 12022:
         return new InitDunTotalLogs(code, readBuf);
      case 12023:
         return new DunRequestShortcutBuy(code, readBuf);
      case 12024:
         return new DungeonInitRecover(code, readBuf);
      case 12027:
         return new DunEnterMoLian(code, readBuf);
      case 13001:
         return new InitTransaction(code, readBuf);
      case 13003:
         return new AcceptTransaction(code, readBuf);
      case 13004:
         return new RefuseTransaction(code, readBuf);
      case 13007:
         return new ClosePanel(code, readBuf);
      case 13008:
         return new PlayerPutItem(code, readBuf);
      case 13010:
         return new DeleteItemByPlayer(code, readBuf);
      case 13012:
         return new PutMoneyByPlayer(code, readBuf);
      case 13014:
         return new LockTransactionByPlayer(code, readBuf);
      case 13016:
         return new UnLockByPlayer(code, readBuf);
      case 13018:
         return new ConfirmTransactionByPlayer(code, readBuf);
      case 15001:
         return new InitTitle(code, readBuf);
      case 15003:
         return new EquipTitle(code, readBuf);
      case 20000:
         return new InitItems(code, readBuf);
      case 20005:
         return new SortoutStorage(code, readBuf);
      case 20006:
         return new BuyNpcGood(code, readBuf);
      case 20007:
         return new SellItemToNPC(code, readBuf);
      case 20008:
         return new RepurchaseItemFromNpc(code, readBuf);
      case 20009:
         return new BackpackCount(code, readBuf);
      case 20011:
         return new BackpackExpand(code, readBuf);
      case 20012:
         return new OpenVipShop(code, readBuf);
      case 20013:
         return new DeportOpen(code, readBuf);
      case 20014:
         return new SplitItem(code, readBuf);
      case 20015:
         return new DiscardItem(code, readBuf);
      case 20017:
         return new ExchangePosition(code, readBuf);
      case 20018:
         return new UseItem(code, readBuf);
      case 20020:
         return new SeeShowItem(code, readBuf);
      case 20022:
         return new MoveItemToDeport(code, readBuf);
      case 20023:
         return new MoveItemFromDeport(code, readBuf);
      case 20024:
         return new DeportExpand(code, readBuf);
      case 20025:
         return new RequestQuickSale(code, readBuf);
      case 20026:
         return new QuickSale(code, readBuf);
      case 20027:
         return new QuickSaleCalMoney(code, readBuf);
      case 20028:
         return new GetMagicItemRecord(code, readBuf);
      case 20029:
         return new OpenMagicPanel(code, readBuf);
      case 20030:
         return new OperateMagicItem(code, readBuf);
      case 20032:
         return new GetItemWay(code, readBuf);
      case 20033:
         return new RequestBackpackExpand(code, readBuf);
      case 20034:
         return new MoveMagicToBackpack(code, readBuf);
      case 20035:
         return new ShowMagicMarketItems(code, readBuf);
      case 20036:
         return new BuyMagicMarketItem(code, readBuf);
      case 20201:
         return new EquipItem(code, readBuf);
      case 20202:
         return new UnEquipItem(code, readBuf);
      case 20203:
         return new StoneMosaic(code, readBuf);
      case 20204:
         return new StoneUnMosaic(code, readBuf);
      case 20205:
         return new RequestConversionStone(code, readBuf);
      case 20206:
         return new ConversionStone(code, readBuf);
      case 20207:
         return new RequestRuneMosaic(code, readBuf);
      case 20208:
         return new RuneMosaic(code, readBuf);
      case 20209:
         return new CompositeItem(code, readBuf);
      case 20211:
         return new RequestInherit(code, readBuf);
      case 20212:
         return new InheritEquipment(code, readBuf);
      case 20213:
         return new RequestStrength(code, readBuf);
      case 20214:
         return new StrengthEquipment(code, readBuf);
      case 20215:
         return new RequestZhuija(code, readBuf);
      case 20216:
         return new ZhuijiaEquipment(code, readBuf);
      case 20217:
         return new RuneUnMosaic(code, readBuf);
      case 20218:
         return new RequestStoneMosaic(code, readBuf);
      case 20219:
         return new RequestRuneInherit(code, readBuf);
      case 20220:
         return new RuneInherit(code, readBuf);
      case 20223:
         return new RequestComConfigure(code, readBuf);
      case 20224:
         return new RequestComPreview(code, readBuf);
      case 20225:
         return new ComFilterMaterial(code, readBuf);
      case 20226:
         return new ComAKeyFilter(code, readBuf);
      case 20229:
         return new RequestDuraRepairMoney(code, readBuf);
      case 20230:
         return new DuraRepair(code, readBuf);
      case 20234:
         return new RequestForgingEquip(code, readBuf);
      case 20235:
         return new RequestUpgrade(code, readBuf);
      case 20236:
         return new UpgradeItem(code, readBuf);
      case 20237:
         return new RequestHorseFuse(code, readBuf);
      case 20238:
         return new HorseFusion(code, readBuf);
      case 20239:
         return new RequestStoneStats(code, readBuf);
      case 20301:
         return new PickoutItem(code, readBuf);
      case 20305:
         return new WellDrop(code, readBuf);
      case 20401:
         return new MarketConfig(code, readBuf);
      case 20402:
         return new RequestMarketItem(code, readBuf);
      case 20403:
         return new BuyMarketItem(code, readBuf);
      case 20404:
         return new MarketItemUpShelve(code, readBuf);
      case 20405:
         return new OffShelveMarketItem(code, readBuf);
      case 20406:
         return new MarketRecords(code, readBuf);
      case 20407:
         return new RequestMarketItemAtom(code, readBuf);
      case 20408:
         return new PublicMarketItem(code, readBuf);
      case 20500:
         return new ExTargetInitCollected(code, readBuf);
      case 20501:
         return new ExTargetInitReceived(code, readBuf);
      case 20502:
         return new ExTargetGetInfo(code, readBuf);
      case 20503:
         return new ExTargetGetWay(code, readBuf);
      case 20504:
         return new ExTargetReceive(code, readBuf);
      case 20601:
         return new RefineHallows(code, readBuf);
      case 20602:
         return new HallowDetail(code, readBuf);
      case 20603:
         return new ShowHallowStats(code, readBuf);
      case 20604:
         return new RequestSpiritDetail(code, readBuf);
      case 20605:
         return new SpiritConditionConfig(code, readBuf);
      case 20606:
         return new RequestSpiritItems(code, readBuf);
      case 20607:
         return new SpiritRefine(code, readBuf);
      case 20608:
         return new SpiritCalExp(code, readBuf);
      case 20610:
         return new RequestLuckyTurnTableInfo(code, readBuf);
      case 20611:
         return new TurnLuckyTable(code, readBuf);
      case 20612:
         return new LuckyTurnTabelRecord(code, readBuf);
      case 30004:
         return new LearnSkill(code, readBuf);
      case 30006:
         return new SelectSkill(code, readBuf);
      case 30008:
         return new UseSkill(code, readBuf);
      case 30012:
         return new RequestPassiveSkill(code, readBuf);
      case 30013:
         return new RequestSkillShortcutPanel(code, readBuf);
      case 31004:
         return new ClickBuff(code, readBuf);
      case 32003:
         return new AttackCreature(code, readBuf);
      case 32010:
         return new SetPkMode(code, readBuf);
      case 40000:
         return new InitTask(code, readBuf);
      case 40007:
         return new SubmitRCTask(code, readBuf);
      case 40008:
         return new BuyRC(code, readBuf);
      case 40009:
         return new RefreshRCStar(code, readBuf);
      case 40010:
         return new AcceptTask(code, readBuf);
      case 40011:
         return new SubmitTask(code, readBuf);
      case 40016:
         return new OpenXSUseItem(code, readBuf);
      case 40028:
         return new OpenZXDetail(code, readBuf);
      case 40029:
         return new DrawZXReward(code, readBuf);
      case 41000:
         return new InitVIP(code, readBuf);
      case 41002:
         return new VIPOpen(code, readBuf);
      case 41003:
         return new VIPContinue(code, readBuf);
      case 42000:
         return new InitFinancing(code, readBuf);
      case 42001:
         return new FinancingInform(code, readBuf);
      case 42004:
         return new FinancingBuy(code, readBuf);
      case 42005:
         return new FinancingReceive(code, readBuf);
      case 43001:
         return new InitPet(code, readBuf);
      case 43006:
         return new PetOpen(code, readBuf);
      case 43007:
         return new PetAttributeRise(code, readBuf);
      case 43009:
         return new PetRiseInform(code, readBuf);
      case 43010:
         return new PetRise(code, readBuf);
      case 43012:
         return new PetRiseStop(code, readBuf);
      case 43013:
         return new PetShowHide(code, readBuf);
      case 44001:
         return new TransferInform(code, readBuf);
      case 44002:
         return new TransferAkeyComplete(code, readBuf);
      case 45101:
         return new MallConfig(code, readBuf);
      case 45102:
         return new MallBuy(code, readBuf);
      case 45104:
         return new MallOpenLink(code, readBuf);
      case 45105:
         return new MallByItemModel(code, readBuf);
      case 45201:
         return new InitShield(code, readBuf);
      case 45202:
         return new ShieldInform(code, readBuf);
      case 45203:
         return new ShieldRiseLevel(code, readBuf);
      case 45204:
         return new ShieldRiseRank(code, readBuf);
      case 45205:
         return new ShieldLevelJQDZ(code, readBuf);
      case 46100:
         return new SignInit(code, readBuf);
      case 46101:
         return new SignInform(code, readBuf);
      case 46103:
         return new SignToday(code, readBuf);
      case 46104:
         return new SignBefore(code, readBuf);
      case 46105:
         return new SignReward(code, readBuf);
      case 46200:
         return new VitalityInit(code, readBuf);
      case 46202:
         return new VitalityInform(code, readBuf);
      case 46204:
         return new VitalityEnter(code, readBuf);
      case 46207:
         return new VitalityReward(code, readBuf);
      case 46300:
         return new OnlineRewardInit(code, readBuf);
      case 46301:
         return new OnlineRewardInform(code, readBuf);
      case 46302:
         return new OnlineRewardReceiveDay(code, readBuf);
      case 46303:
         return new OnlineRewardReceiveWeek(code, readBuf);
      case 46401:
         return new SevenDayPush(code, readBuf);
      case 46402:
         return new SevenDayFindItem(code, readBuf);
      case 48000:
         return new TanXianInit(code, readBuf);
      case 48001:
         return new TanXianInform(code, readBuf);
      case 48002:
         return new TanXianDesInform(code, readBuf);
      case 48003:
         return new TanXianCountTip(code, readBuf);
      case 48004:
         return new TanXianStart(code, readBuf);
      case 48007:
         return new ReceiveChestReward(code, readBuf);
      case 48009:
         return new TanXianContinue(code, readBuf);
      case 59001:
         return new InitStorage(code, readBuf);
      case 59002:
         return new InitSkills(code, readBuf);
      case 59003:
         return new InitShortcut(code, readBuf);
      case 59004:
         return new InitFirstkill(code, readBuf);
      case 59005:
         return new InitRoleDropCounts(code, readBuf);
      case 59006:
         return new InitItemLimits(code, readBuf);
      case 59007:
         return new InitBuffs(code, readBuf);
      case 59008:
         return new InitHangset(code, readBuf);
      case 59010:
         return new InitSevenDayTreasure(code, readBuf);
      case 59011:
         return new InitSpirit(code, readBuf);
      case 59012:
         return new InitHallows(code, readBuf);
      case 59013:
         return new InitLuckyTurnTable(code, readBuf);
      case 60001:
         return new ReadListPacket(code, readBuf);
      default:
         return null;
      }
   }
}
