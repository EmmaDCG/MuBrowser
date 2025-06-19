package com.mu.game.model.unit.player.popup;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public abstract class Popup {
   public static final int Popup_UseItem = 1;
   public static final int Popup_ExpandBackpack = 2;
   public static final int Popup_QuitGang = 3;
   public static final int Popup_TaskImmediateSubmit = 4;
   public static final int Popup_TeamInvite = 5;
   public static final int Popup_DungeonInvite = 6;
   public static final int Popup_DungeonPromptTeam = 7;
   public static final int Popup_DungeonNotAllConfirm = 9;
   public static final int Popup_ExpandDeport = 10;
   public static final int Popup_TrialReceive = 11;
   public static final int Popup_EquipStrength_1 = 12;
   public static final int Popup_EquipStrength_2 = 13;
   public static final int Popup_EquipStrength_3 = 14;
   public static final int Popup_RuneUnMosaic = 15;
   public static final int Popup_BasicPropertyNoErr = 16;
   public static final int Popup_AfterAllocate = 17;
   public static final int Popup_Item_Buff_Priority = 18;
   public static final int Popup_MALL_INGOT = 19;
   public static final int Popup_RedName = 20;
   public static final int Popup_DungeonQuit = 21;
   public static final int Popup_Renew = 22;
   public static final int Popup_TaskAKeyCompleteAll = 23;
   public static final int Popup_MallBuy = 24;
   public static final int Popup_UnMosaicStone = 25;
   public static final int Popup_Financing = 26;
   public static final int Popup_Transfer = 27;
   public static final int Popup_Composite = 28;
   public static final int Popup_TeHui = 29;
   public static final int Popup_Angel = 30;
   public static final int Popup_DunBuyTicket = 31;
   public static final int Popup_GangTranseferMaster = 32;
   public static final int Popup_DungeonRecover = 33;
   public static final int Popup_RedPacket = 34;
   public static final int Popup_Pay = 35;
   public static final int Popup_ToDepot = 36;
   public static final int Popup_DelFriend = 37;
   public static final int Popup_FriendToBlack = 38;
   public static final int Popup_AddToBlack = 39;
   public static final int Popup_SpiritRefine = 40;
   private int id;

   public Popup(int id) {
      this.id = id;
   }

   public int getID() {
      return this.id;
   }

   public abstract String getContent();

   public abstract void dealLeftClick(Player var1);

   public abstract void dealRightClick(Player var1);

   public abstract boolean isShowAgain(Player var1);

   public abstract int getType();

   public String getTitle() {
      return MessageText.getText(4034);
   }

   public String getLeftButtonContent() {
      return MessageText.getText(4001);
   }

   public String getRightButtonContent() {
      return MessageText.getText(4002);
   }

   public void writeContent(WriteOnlyPacket packet, Player player) throws Exception {
      packet.writeUTF(this.getContent());
      packet.writeByte(0);
   }

   public void dealCloseClick(Player player) {
      player.removePopup(this.getID());
      this.destroy();
   }

   public int getRemainTime() {
      return 0;
   }

   public void destroy() {
   }
}
