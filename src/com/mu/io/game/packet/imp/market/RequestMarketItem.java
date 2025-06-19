package com.mu.io.game.packet.imp.market;

import com.mu.game.model.map.Map;
import com.mu.game.model.market.MarketItem;
import com.mu.game.model.market.MarketManager;
import com.mu.game.model.market.MarketSort;
import com.mu.game.model.market.condition.ConditionAtom;
import com.mu.game.model.market.condition.MarketCondition;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.SystemMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RequestMarketItem extends ReadAndWritePacket {
    public RequestMarketItem(int code, byte[] readBuf) {
        super(code, readBuf);
    }

    public void process() throws Exception {
        Player player = this.getPlayer();
        int fatherSortID = this.readShort();
        int sonSortID = this.readShort();
        MarketSort fatherSort = MarketSort.getSort(fatherSortID);
        if (fatherSort != null || fatherSortID == -1) {
            if (fatherSort == null || sonSortID == -1 || fatherSort.hasSonSort(sonSortID)) {
                int conSize = this.readByte();
                List indexList = new ArrayList();

                byte compareType;
                for (int i = 0; i < conSize; ++i) {
                    compareType = this.readByte();
                    indexList.add(Integer.valueOf(compareType));
                }

                String key = this.readUTF();
                compareType = this.readByte();
                int compareStyle = this.readByte();
                int page = this.readShort();
                List atoms = new ArrayList();
                if (fatherSortID != -1) {
                    int result = this.getConditionAtom(fatherSort, indexList, atoms);
                    if (result != 1) {
                        SystemMessage.writeMessage(player, result);
                        indexList.clear();
                        indexList = null;
                        atoms.clear();
                        atoms = null;
                        return;
                    }
                }

                int sortID = sonSortID == -1 ? fatherSortID : sonSortID;
                if (fatherSortID == -1) {
                    sortID = fatherSortID;
                }

                List items = MarketManager.getShowItem(sortID, atoms, key);
                if (items.size() > 0) {
                    Comparator mc = createCompartorF(compareType, compareStyle);
                    Collections.sort(items, mc);
                }

                int allSize = items.size();
                int pages = allSize % 12 == 0 ? allSize / 12 : allSize / 12 + 1;
                if (page > pages) {
                    page = pages;
                }

                if (page < 1) {
                    page = 1;
                }

                int first = (page - 1) * 12;
                int end = first + 12 > allSize ? allSize : first + 12;
                this.writeShort(fatherSortID);
                this.writeShort(sonSortID);
                this.writeShort(page);
                this.writeShort(pages);
                this.writeByte(compareType);
                this.writeByte(compareStyle);
                this.writeByte(end - first);

                for (int i = first; i < end; ++i) {
                    MarketItem item = (MarketItem) items.get(i);
                    writeItemDetail(item, this);
                }

                player.writePacket(this);
                indexList.clear();
                indexList = null;
                atoms.clear();
                atoms = null;
                items.clear();
                items = null;
            }
        }
    }

    public static void writeItemDetail(MarketItem mItem, WriteOnlyPacket packet) throws Exception {
        GetItemStats.writeItem(mItem.getItem(), packet);
        packet.writeDouble((double) mItem.getRoleID());
        packet.writeUTF(mItem.getOwnerName());
    }

    private int getConditionAtom(MarketSort sort, List readCons, List atoms) {
        for (int i = 0; i < readCons.size(); ++i) {
            MarketCondition condition = sort.getCondition(i);
            if (condition == null) {
                return 16603;
            }

            int index = ((Integer) readCons.get(i)).intValue();
            ConditionAtom atom = condition.getAtom(index);
            if (atom == null) {
                return 16603;
            }

            atoms.add(atom);
        }

        return 1;
    }

    private static Comparator createCompartorF(int compareType, int compareStyle) {
        Comparator mc = null;
        if (compareType == 0) {
            if (compareStyle == 0) {
                mc = new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        return ((MarketItem)o1).getAveragePrice() - ((MarketItem)o2).getAveragePrice();
                    }
                };
            } else {
                mc = new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        return ((MarketItem)o2).getAveragePrice() - ((MarketItem)o1).getAveragePrice();
                    }
                };
            }
        } else if (compareStyle == 0) {
            mc = new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return ((MarketItem)o1).getItem().getMoney() - ((MarketItem)o2).getItem().getMoney();
                }
            };
        } else {
            mc = new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return ((MarketItem)o2).getItem().getMoney() - ((MarketItem)o1).getItem().getMoney();
                }
            };
        }
        return mc;
    }
}
