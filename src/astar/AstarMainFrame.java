package astar;

import com.mu.game.model.map.LineMap;
import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class AstarMainFrame extends JFrame {
   public static final String title = "AStar寻路算法测试(鼠标右键拖动: 移动地图; 鼠标滚动: 缩放地图(+Ctrl快速); Ctrl+鼠标左键: 选中寻路起始点; 鼠标左键: 开始寻路;)";
   private Map firstMap;
   private static final long serialVersionUID = 1L;

   public void launchFrame() {
      this.setTitle("AStar寻路算法测试(鼠标右键拖动: 移动地图; 鼠标滚动: 缩放地图(+Ctrl快速); Ctrl+鼠标左键: 选中寻路起始点; 鼠标左键: 开始寻路;)");
      this.setContentPane(new Panel((LayoutManager)null));
      this.createMenu();
      this.setBounds(200, 200, 1000, 700);
      this.setExtendedState(6);
      this.setVisible(true);
      this.setDefaultCloseOperation(3);
      this.selectMap(this.firstMap);
   }

   private void createMenu() {
      JMenuBar menubar = new JMenuBar();
      JMenu mapMenu = new JMenu("选择地图");
      Iterator it = MapConfig.getLineMaps().values().iterator();

      while(it.hasNext()) {
         final Map map = ((LineMap)it.next()).getFirstMap();
         this.firstMap = this.firstMap == null ? map : this.firstMap;
         JMenuItem item = new JMenuItem(map.getName());
         mapMenu.add(item);
         item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               AstarMainFrame.this.selectMap(map);
            }
         });
      }

      menubar.add(mapMenu);
      this.setJMenuBar(menubar);
   }

   private void selectMap(Map map) {
      if (map != null) {
         AStarMapPanel contentPanel = new AStarMapPanel(map);
         this.getContentPane().removeAll();
         this.getContentPane().repaint();
         this.getContentPane().add(contentPanel);
         contentPanel.init();
      }
   }
}
