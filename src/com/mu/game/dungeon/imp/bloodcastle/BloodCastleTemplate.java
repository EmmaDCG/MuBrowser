package com.mu.game.dungeon.imp.bloodcastle;

import com.mu.game.dungeon.DungeonReward;
import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.dungeon.MultiReceiveInfo;
import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.mall.ShortcutBuyPanel;
import com.mu.game.model.map.BigMonsterGroup;
import com.mu.game.model.map.NpcInfo;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.Profession;
import com.mu.game.model.unit.player.dun.DunLogManager;
import com.mu.game.model.unit.player.dun.DunLogs;
import com.mu.game.model.unit.robot.RobotInfo;
import com.mu.game.model.vip.effect.VIPEffectType;
import com.mu.io.game.packet.imp.dungeon.RequestDungeonInfo;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.mall.OpenShortcutBuyAndUse;
import com.mu.utils.Rnd;
import com.mu.utils.Tools;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import jxl.Sheet;
import jxl.Workbook;

public class BloodCastleTemplate extends DungeonTemplate {
    public static final int Batch_Bridge = 1;
    public static final int Batch_Gate = 2;
    public static final int Batch_Wizard = 3;
    public static final int Batch_Coffin = 4;
    public static final int Batch_Angel = 5;
    private HashMap levelMap = new HashMap();
    private HashMap rankMap = new HashMap();
    private HashMap finishConditionsMap = new HashMap();
    private HashMap scheduleExplainMap = new HashMap();
    private ArrayList rankList = new ArrayList();
    private ArrayList multiReciveList = new ArrayList();
    private HashMap robotMap = new HashMap();
    private ArrayList gateBlockList = new ArrayList();
    private NpcInfo angelInfo = null;
    private int maxRank = 0;
    private Point dropPoint = null;
    private String playDes;
    private int robotTimes = 0;
    private int pointMenu = 21;
    private String pointMsg = "";

    public BloodCastleTemplate(Workbook wb) {
        super(1, wb);
        this.canInspire = true;
    }

    public void init() throws Exception {
        this.initGeneral(this.wb.getSheet(1));
        this.initBloodCastleLevel(this.wb.getSheet(2));
        this.initBloodCastleRank(this.wb.getSheet(3));
        this.initBloodCastleReward(this.wb.getSheet(4));
        this.initMultiRecive(this.wb.getSheet(5));
        this.initAngelInfo(this.wb.getSheet(6));
        this.initFinishConditions(this.wb.getSheet(7));
        this.initMonsterStar(this.wb.getSheet(8));
        this.initRobot(this.wb.getSheet(9));
        this.initMonsters();
    }

    private void initGeneral(Sheet sheet) {
        this.setInspireMoney(Tools.getCellIntValue(sheet.getCell("I2")));
        this.setInspireIngot(Tools.getCellIntValue(sheet.getCell("J2")));
        this.setMoneyInspireDes(Tools.getCellValue(sheet.getCell("K2")));
        this.setIngotInspireDes(Tools.getCellValue(sheet.getCell("L2")));
        this.playDes = Tools.getCellValue(sheet.getCell("M2"));
        String[] tmpPoint = Tools.getCellValue(sheet.getCell("N2")).split(",");
        this.dropPoint = new Point(Integer.parseInt(tmpPoint[0]), Integer.parseInt(tmpPoint[1]));
        String[] tmpBlocks = Tools.getCellValue(sheet.getCell("O2")).split(";");

        int gtId;
        for (gtId = 0; gtId < tmpBlocks.length; ++gtId) {
            String[] block = tmpBlocks[gtId].split(",");
            this.gateBlockList.add(new int[]{Integer.parseInt(block[0]), Integer.parseInt(block[1])});
        }

        gtId = Tools.getCellIntValue(sheet.getCell("P2"));
        this.setGlobalTicket(ItemTools.createItem(gtId, 1, 2));
        this.robotTimes = Tools.getCellIntValue(sheet.getCell("Q2"));
        this.pointMenu = Tools.getCellIntValue(sheet.getCell("R2"));
        this.pointMsg = Tools.getCellValue(sheet.getCell("S2"));
    }

    public int getRobotTimes() {
        return this.robotTimes;
    }

    public int getPointMenu() {
        return this.pointMenu;
    }

    public String getPointMsg() {
        return this.pointMsg;
    }

    private void initMultiRecive(Sheet sheet) throws Exception {
        int rows = sheet.getRows();

        for (int i = 2; i <= rows; ++i) {
            int index = Tools.getCellIntValue(sheet.getCell("A" + i));
            int times = Tools.getCellIntValue(sheet.getCell("B" + i));
            int ingot = Tools.getCellIntValue(sheet.getCell("C" + i));
            int vipLevel = Tools.getCellIntValue(sheet.getCell("D" + i));
            String str = Tools.getCellValue(sheet.getCell("E" + i));
            String rt = Tools.getCellValue(sheet.getCell("F" + i));
            MultiReceiveInfo info = new MultiReceiveInfo();
            info.setIndex(index);
            info.setTimes(times);
            info.setIngot(ingot);
            info.setVipLevel(vipLevel);
            info.setReceiveName(str);
            info.setReceiveTitle(rt);
            this.multiReciveList.add(info);
        }

    }

    private void initFinishConditions(Sheet sheet) throws Exception {
        int rows = sheet.getRows();

        for (int i = 2; i <= rows; ++i) {
            int schedule = Tools.getCellIntValue(sheet.getCell("A" + i));
            String explain = Tools.getCellValue(sheet.getCell("B" + i));
            String targetStr = Tools.getCellValue(sheet.getCell("C" + i));
            int number = Tools.getCellIntValue(sheet.getCell("D" + i));
            ScheduleConditions con = new ScheduleConditions(schedule);
            con.setExplain(explain);
            con.setMaxNumber(number);
            if (targetStr != null && !targetStr.trim().equals("")) {
                String[] tmp = targetStr.split(",");
                int[] target = new int[tmp.length];

                for (int j = 0; j < tmp.length; ++j) {
                    target[j] = Integer.parseInt(tmp[j]);
                }

                con.setTarget(target);
            }

            this.finishConditionsMap.put(schedule, con);
        }

    }

    public ScheduleConditions getScheduleConditions(int schedule) {
        return (ScheduleConditions) this.finishConditionsMap.get(schedule);
    }

    private void initAngelInfo(Sheet sheet) throws Exception {
        this.angelInfo = new NpcInfo();
        this.angelInfo.setName(Tools.getCellValue(sheet.getCell("B2")));
        this.angelInfo.setTemplateId(Tools.getCellIntValue(sheet.getCell("A2")));
        this.angelInfo.setX(Tools.getCellIntValue(sheet.getCell("C2")));
        this.angelInfo.setY(Tools.getCellIntValue(sheet.getCell("D2")));
        String[] faceStr = Tools.getCellValue(sheet.getCell("E2")).split(",");
        this.angelInfo.setFace(new int[]{Integer.parseInt(faceStr[0]), Integer.parseInt(faceStr[1])});
    }

    public NpcInfo getAngelInfo() {
        return this.angelInfo;
    }

    private void initBloodCastleReward(Sheet sheet) throws Exception {
        int rows = sheet.getRows();

        for (int i = 2; i <= rows; ++i) {
            DungeonReward re = new DungeonReward();
            int level = Tools.getCellIntValue(sheet.getCell("A" + i));
            int rank = Tools.getCellIntValue(sheet.getCell("B" + i));
            long exp = Tools.getCellLongValue(sheet.getCell("C" + i));
            int money = Tools.getCellIntValue(sheet.getCell("D" + i));
            re.setExp(exp);
            re.setMoney(money);
            String str = Tools.getCellValue(sheet.getCell("E" + i));
            if (str != null && str.trim().length() > 0) {
                String[] tmp = str.split(";");

                for (int j = 0; j < tmp.length; ++j) {
                    String[] itemStr = tmp[j].split(",");
                    int modelId = Integer.parseInt(itemStr[0]);
                    int num = Integer.parseInt(itemStr[1]);
                    boolean isBind = Integer.parseInt(itemStr[2]) == 1;
                    Item item = ItemTools.createItem(modelId, num, 2);
                    item.setBind(isBind);
                    ItemDataUnit unit = new ItemDataUnit(modelId, num, isBind);
                    re.addItem(item);
                    re.addItemData(unit);
                }
            }

            BloodCastleLevel dl = this.getDevilSquareLevel(level);
            if (dl != null) {
                dl.addReward(rank, re);
            }
        }

    }

    private void initBloodCastleRank(Sheet sheet) throws Exception {
        int rows = sheet.getRows();

        for (int i = 2; i <= rows; ++i) {
            BloodCastleRank dr = new BloodCastleRank();
            int rank = Tools.getCellIntValue(sheet.getCell("A" + i));
            int time = Tools.getCellIntValue(sheet.getCell("B" + i));
            String target = Tools.getCellValue(sheet.getCell("C" + i));
            int actualTime = Tools.getCellIntValue(sheet.getCell("E" + i));
            if (rank > this.maxRank) {
                this.maxRank = rank;
            }

            dr.setRank(rank);
            dr.setMaxTime(time);
            dr.setTarget(target);
            dr.setTimeLeft(this.timeLimit - time);
            dr.setActualTime(actualTime);
            this.rankMap.put(rank, dr);
            this.rankList.add(dr);
        }

        Collections.sort(this.rankList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((BloodCastleRank)o2).getTimeLeft() - ((BloodCastleRank)o2).getTimeLeft();
            }
        });
    }

    public int getMaxRank() {
        return this.maxRank;
    }

    private void initRobot(Sheet sheet) throws Exception {
        int rows = sheet.getRows();

        for (int j = 2; j <= rows; ++j) {
            String name = Tools.getCellValue(sheet.getCell("A" + j));
            int pro = Tools.getCellIntValue(sheet.getCell("B" + j));
            String[] p = Tools.getCellValue(sheet.getCell("C" + j)).split(",");
            Point point = new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
            String[] equips = Tools.getCellValue(sheet.getCell("D" + j)).split(";");
            ArrayList listEquip = new ArrayList();

            for (int k = 0; k < equips.length; ++k) {
                String[] e = equips[k].split(",");
                listEquip.add(new int[]{Integer.parseInt(e[0]), Integer.parseInt(e[1]), Integer.parseInt(e[2])});
            }

            String[] skills = Tools.getCellValue(sheet.getCell("E" + j)).split(",");
            ArrayList listSkill = new ArrayList();

            int level;
            for (level = 0; level < skills.length; ++level) {
                listSkill.add(Integer.parseInt(skills[level]));
            }

            level = Tools.getCellIntValue(sheet.getCell("F" + j));
            RobotInfo info = new RobotInfo();
            info.setEquips(listEquip);
            info.setName(name);
            info.setPoint(point);
            info.setSkills(listSkill);
            info.setPro(pro);
            info.setLevel(level);
            info.setRadius(200000);
            String tmpWord = Tools.getCellValue(sheet.getCell("G" + j));
            if (tmpWord != null && !tmpWord.trim().equals("")) {
                String[] words = tmpWord.split("#_#");
                String[] var19 = words;
                int var18 = words.length;

                for (int var17 = 0; var17 < var18; ++var17) {
                    String w = var19[var17];
                    info.addWord(w);
                }
            }

            this.robotMap.put(Profession.getProfession(pro).getProType(), info);
        }

    }

    public RobotInfo getRobotInfo(int proType) {
        return (RobotInfo) this.robotMap.get(proType);
    }

    private void initBloodCastleLevel(Sheet sheet) throws Exception {
        int rows = sheet.getRows();

        for (int i = 2; i <= rows; ++i) {
            BloodCastleLevel dl = new BloodCastleLevel();
            this.initDungeonLevel(sheet, i, dl);
            this.levelMap.put(dl.getLevel(), dl);
            dl.setShortcutBuyTitle(Tools.getCellValue(sheet.getCell("K" + i)));
            String canSellStr = Tools.getCellValue(sheet.getCell("I" + i));
            int j;
            if (canSellStr != null && !canSellStr.trim().equals("")) {
                String[] tmp = canSellStr.trim().split(",");
                int[] in = new int[tmp.length];

                for (j = 0; j < in.length; ++j) {
                    in[j] = Integer.parseInt(tmp[j]);
                }

                dl.setCanSellItem(in);
            }

            String buyStr = Tools.getCellValue(sheet.getCell("J" + i));
            if (buyStr != null && !buyStr.trim().equals("")) {
                String[] tmp = buyStr.trim().split(";");

                for (j = 0; j < tmp.length; ++j) {
                    String[] iStr = tmp[j].split(",");
                    int modelId = Integer.parseInt(iStr[0]);
                    int price = Integer.parseInt(iStr[1]);
                    boolean isBind = Integer.parseInt(iStr[2]) == 1;
                    int moneyType = Integer.parseInt(iStr[3]);
                    Item item = ShortcutBuyPanel.addSellItem("血色快捷购买 ", modelId, price, moneyType, isBind, -1L);
                    dl.addBuyItem(j, item);
                }
            }
        }

    }

    public BloodCastleRank getRank(int rnk) {
        return (BloodCastleRank) this.rankMap.get(rnk);
    }

    public BloodCastleRank getRankByLeftTime(int timeLeft) {
        Iterator var3 = this.rankList.iterator();

        while (var3.hasNext()) {
            BloodCastleRank rank = (BloodCastleRank) var3.next();
            if (timeLeft >= rank.getTimeLeft()) {
                return rank;
            }
        }

        return (BloodCastleRank) this.rankMap.get(Integer.valueOf(0));
    }

    public String getScheduleExplain(int schedule) {
        return (String) this.scheduleExplainMap.get(schedule);
    }

    private void initMonsters() throws Exception {
        Sheet[] sheets = this.wb.getSheets();

        for (int i = 10; i < sheets.length; ++i) {
            Sheet sheet = sheets[i];
            int rows = sheet.getRows();

            for (int j = 2; j <= rows; ++j) {
                BloodCastleMonsterGroup group = new BloodCastleMonsterGroup();
                BigMonsterGroup.parseMonsterGroup(group, sheet, j);
                group.setBatch(Tools.getCellIntValue(sheet.getCell("T" + j)));
                group.setLevelId(Tools.getCellIntValue(sheet.getCell("U" + j)));
                group.setBossRank(Tools.getCellIntValue(sheet.getCell("V" + j)));
                group.setFace(BigMonsterGroup.parseFace(Tools.getCellValue(sheet.getCell("W" + j))));
                BloodCastleLevel bl = (BloodCastleLevel) this.levelMap.get(group.getLevelId());
                if (bl != null) {
                    switch (group.getBatch()) {
                        case 1:
                            bl.addBridgeMonster(group);
                            break;
                        case 2:
                            bl.setGroupGate(group);
                            break;
                        case 3:
                            bl.addWizardMonster(group);
                            break;
                        case 4:
                            bl.setGroupCoffin(group);
                            break;
                        case 5:
                            bl.setGroupAngel(group);
                    }

                    this.checkMonsterStar(group);
                }
            }
        }

    }

    public HashMap getLevelMap() {
        return this.levelMap;
    }

    public BloodCastleLevel getDevilSquareLevel(int levelId) {
        return (BloodCastleLevel) this.levelMap.get(levelId);
    }

    public BloodCastleLevel getPlayerFitLevel(Player player) {
        Iterator it = this.levelMap.values().iterator();

        BloodCastleLevel dl;
        do {
            if (!it.hasNext()) {
                return null;
            }

            dl = (BloodCastleLevel) it.next();
        } while (dl.getMinLevelReq() > player.getLevel() || dl.getMaxLevelReq() < player.getLevel());

        return dl;
    }

    public List getForceDrop(int[] target) {
        List list = new ArrayList();
        ItemDataUnit unit = new ItemDataUnit(target[Rnd.get(target.length)], 1, true);
        list.add(unit);
        return list;
    }

    public int canEnter(Player player, Item ticket, Object... obj) {
        BloodCastleLevel level = (BloodCastleLevel) obj[0];
        if (this.getPlayerFitLevel(player).getLevel() != level.getLevel()) {
            return 14010;
        } else {
            DunLogManager lm = player.getDunLogsManager();
            DunLogs log = lm.getLog(this.templateID);
            return log != null && log.notReceivd() ? 14043 : super.canEnter(player, ticket, obj);
        }
    }

    public MultiReceiveInfo getMultiReceiveInfo(int index) {
        return index >= 0 && index <= this.multiReciveList.size() ? (MultiReceiveInfo) this.multiReciveList.get(index) : null;
    }

    public Point getDropPoint() {
        return this.dropPoint;
    }

    public void writeDungeonInfo(Player player) throws Exception {
        BloodCastleLevel dl = this.getPlayerFitLevel(player);
        if (dl != null) {
            RequestDungeonInfo packet = new RequestDungeonInfo();
            packet.writeByte(this.getTemplateID());
            packet.writeByte(dl.getLevel());
            packet.writeUTF(dl.getLevelStr());
            packet.writeUTF(dl.getStrength());
            ArrayList list = dl.getDropList();
            packet.writeByte(list.size());
            Iterator var6 = list.iterator();

            Item ticket;
            while (var6.hasNext()) {
                ticket = (Item) var6.next();
                GetItemStats.writeItem(ticket, packet);
            }

            ticket = dl.getReqItem();
            if (player.getBackpack().hasEnoughItem(ticket.getModel().getID(), ticket.getCount())) {
                GetItemStats.writeItem(ticket, packet);
            } else {
                Item gTicket = this.getGlobalTicket();
                if (gTicket != null && player.getBackpack().hasEnoughItem(gTicket.getModel().getID(), gTicket.getCount())) {
                    GetItemStats.writeItem(gTicket, packet);
                } else {
                    GetItemStats.writeItem(ticket, packet);
                }
            }

            packet.writeByte(this.getPlayerLeftTimes(player, 0));
            packet.writeUTF(this.playDes);
            player.writePacket(packet);
            ArrowGuideManager.pushArrow(player, 14, (String) null);
        }
    }

    public ArrayList getMultiReciveList() {
        return this.multiReciveList;
    }

    public ArrayList getGateBlockList() {
        return this.gateBlockList;
    }

    public void writeShortcutBuyTicket(Player player, int key) {
        try {
            BloodCastleLevel bl = this.getPlayerFitLevel(player);
            if (bl == null) {
                return;
            }

            OpenShortcutBuyAndUse osbu = new OpenShortcutBuyAndUse();
            osbu.writeInt(key);
            osbu.writeUTF(bl.getShortcutBuyTitle());
            TreeMap map = bl.getBuyItemMap();
            osbu.writeByte(map.size());
            Iterator it = map.values().iterator();

            while (it.hasNext()) {
                GetItemStats.writeItem((Item) it.next(), osbu);
            }

            player.writePacket(osbu);
            osbu.destroy();
            osbu = null;
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    public int getMaxTimes(Player player, int smallId) {
        int times = super.getMaxTimes(player, smallId) + player.getVIPManager().getEffectIntegerValue(VIPEffectType.VE_24);
        int recoverTimes = player.getOffLineManager().getRecoverTimes(this.getTemplateID());
        return times + recoverTimes;
    }

    public boolean showDynamicMenu() {
        return false;
    }
}
