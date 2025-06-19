package astar;

import astar.AstarMainFrame;
import astar.AstarTestMain;
import com.mu.game.model.map.AStar;
import com.mu.game.model.map.AstarNode;
import com.mu.game.model.map.Map;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class AStarMapPanel
extends JPanel {
    private static final long serialVersionUID = 1L;
    private int tileSize = 5;
    private Map map;
    private Point tile_start;
    private Point tile_end;
    private List<Point> optimizePath;
    private List<AstarNode> pathList;
    private long time;
    private boolean drawGrid;
    private boolean drawCoordinate;

    public AStarMapPanel(Map map) {
        this.map = map;
    }

    public void init() {
        this.drawGrid = true;
        this.drawCoordinate = false;
        this.setLocation(10, 10);
        this.optimizePath = new ArrayList<Point>();
        this.pathList = null;
        this.tile_start = null;
        this.tile_end = null;
        this.time = 0L;
        this.setTileSize(5);
        ControlMonitor ctrlMonitor = new ControlMonitor();
        this.addMouseListener(ctrlMonitor);
        this.addMouseMotionListener(ctrlMonitor);
        this.addMouseWheelListener(ctrlMonitor);
        AstarTestMain.mainFrame.addKeyListener(ctrlMonitor);
    }

    private void setTileSize(int tileSize) {
        this.tileSize = tileSize = Math.max(2, Math.min(200, tileSize));
        this.setSize(this.map.getTileHorizontalNumber() * tileSize + 10, this.map.getTileVerticalNumber() * tileSize + 10);
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        int width = this.map.getTileHorizontalNumber() * this.tileSize;
        int height = this.map.getTileVerticalNumber() * this.tileSize;
        ((Graphics2D)g).setStroke(new BasicStroke(1.0f, 0, 2));
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        byte[][] blocks = this.map.getBlocks();
        int i = 0;
        while (i < blocks.length) {
            int j = 0;
            while (j < blocks[i].length) {
                g.setColor(this.map.tileIsBlocked(i, j) ? Color.WHITE : Color.RED);
                g.fillRect(i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize);
                ++j;
            }
            ++i;
        }
        if (this.pathList != null) {
            g.setColor(Color.green);
            for (AstarNode node : this.pathList) {
                g.fillRect(node.x * this.tileSize, node.y * this.tileSize, this.tileSize, this.tileSize);
                this.drawCoordinate(g, node.x, node.y);
            }
        }
        i = 0;
        while (i < this.optimizePath.size()) {
            Point point = this.optimizePath.get(i);
            g.setColor(Color.blue);
            g.fillRect(this.map.getTileX(point.x) * this.tileSize, this.map.getTileY(point.y) * this.tileSize, this.tileSize, this.tileSize);
            if (i > 0) {
                g.setColor(Color.BLACK);
                g.drawLine(this.map.getTileX(this.optimizePath.get((int)(i - 1)).x) * this.tileSize + this.tileSize / 2, this.map.getTileY(this.optimizePath.get((int)(i - 1)).y) * this.tileSize + this.tileSize / 2, this.map.getTileX(point.x) * this.tileSize + this.tileSize / 2, this.map.getTileY(point.y) * this.tileSize + this.tileSize / 2);
            }
            this.drawCoordinate(g, this.map.getTileX(point.x), this.map.getTileY(point.y));
            ++i;
        }
        if (this.tile_start != null) {
            g.setColor(Color.BLACK);
            g.fillRect(this.tile_start.x * this.tileSize, this.tile_start.y * this.tileSize, this.tileSize, this.tileSize);
            this.drawCoordinate(g, this.tile_start.x, this.tile_start.y);
        }
        if (this.drawGrid) {
            g.setColor(Color.BLACK);
            i = 0;
            while (i <= this.map.getTileHorizontalNumber()) {
                g.drawLine(i * this.tileSize, 0, i * this.tileSize, height);
                ++i;
            }
            i = 0;
            while (i <= this.map.getTileVerticalNumber()) {
                g.drawLine(0, i * this.tileSize, width, i * this.tileSize);
                ++i;
            }
        }
    }

    private void drawCoordinate(Graphics g, int x, int y) {
        if (this.drawCoordinate && this.tileSize > 10) {
            Color oldColor = g.getColor();
            g.setColor(new Color(255 - oldColor.getRed(), 255 - oldColor.getGreen(), 255 - oldColor.getBlue()));
            g.drawString(String.valueOf(x) + "," + y, x * this.tileSize + 2, y * this.tileSize + 12);
            g.setColor(oldColor);
        }
    }

    public Point getTile(Point position) {
        return new Point(position.x / this.tileSize, position.y / this.tileSize);
    }

    public int getTileSize() {
        return this.tileSize;
    }

    static /* synthetic */ void access$1(AStarMapPanel aStarMapPanel, Point point) {
        aStarMapPanel.tile_start = point;
    }

    static /* synthetic */ void access$2(AStarMapPanel aStarMapPanel, Point point) {
        aStarMapPanel.tile_end = point;
    }

    static /* synthetic */ void access$3(AStarMapPanel aStarMapPanel, List list) {
        aStarMapPanel.pathList = list;
    }

    static /* synthetic */ Point access$6(AStarMapPanel aStarMapPanel) {
        return aStarMapPanel.tile_end;
    }

    static /* synthetic */ void access$7(AStarMapPanel aStarMapPanel, long l) {
        aStarMapPanel.time = l;
    }

    static /* synthetic */ void access$12(AStarMapPanel aStarMapPanel, boolean bl) {
        aStarMapPanel.drawGrid = bl;
    }

    static /* synthetic */ void access$14(AStarMapPanel aStarMapPanel, boolean bl) {
        aStarMapPanel.drawCoordinate = bl;
    }

    class ControlMonitor
    extends MouseAdapter
    implements KeyListener {
        private boolean move;
        private int o_mouseX;
        private int o_mouseY;

        ControlMonitor() {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            boolean bl = this.move = e.getButton() == 3;
            if (this.move) {
                AStarMapPanel.this.setCursor(new Cursor(13));
                this.o_mouseX = e.getLocationOnScreen().x;
                this.o_mouseY = e.getLocationOnScreen().y;
            } else if (e.isControlDown()) {
                Point tile = AStarMapPanel.this.getTile(e.getPoint());
                if (!AStarMapPanel.this.map.tileIsBlocked(tile.x, tile.y)) {
                    AStarMapPanel.access$1(AStarMapPanel.this, tile);
                    AStarMapPanel.access$2(AStarMapPanel.this, null);
                    AStarMapPanel.access$3(AStarMapPanel.this, null);
                    AStarMapPanel.this.optimizePath.clear();
                    AStarMapPanel.this.repaint();
                }
            } else {
                Point tile = AStarMapPanel.this.getTile(e.getPoint());
                if (AStarMapPanel.this.tile_start != null && !AStarMapPanel.this.map.tileIsBlocked(tile.x, tile.y)) {
                    AStarMapPanel.access$2(AStarMapPanel.this, tile);
                    AStarMapPanel.this.optimizePath.clear();
                    AStarMapPanel.access$3(AStarMapPanel.this, null);
                    long cur = System.currentTimeMillis();
                    List<AstarNode> list = AStar.astarFindingWay(AStarMapPanel.this.optimizePath, AStarMapPanel.this.map, AStarMapPanel.this.map.getXByTile(AStarMapPanel.access$5((AStarMapPanel)AStarMapPanel.this).x), AStarMapPanel.this.map.getYByTile(AStarMapPanel.access$5((AStarMapPanel)AStarMapPanel.this).y), AStarMapPanel.this.map.getXByTile(AStarMapPanel.access$6((AStarMapPanel)AStarMapPanel.this).x), AStarMapPanel.this.map.getYByTile(AStarMapPanel.access$6((AStarMapPanel)AStarMapPanel.this).y));
                    AStarMapPanel.access$3(AStarMapPanel.this, list);
                    if (list == null) {
                        AStarMapPanel.access$2(AStarMapPanel.this, null);
                        AStarMapPanel.this.optimizePath.clear();
                    }
                    AStarMapPanel.access$7(AStarMapPanel.this, System.currentTimeMillis() - cur);
                    AstarTestMain.mainFrame.setTitle("AStar\u5bfb\u8def\u7b97\u6cd5\u6d4b\u8bd5(\u9f20\u6807\u53f3\u952e\u62d6\u52a8: \u79fb\u52a8\u5730\u56fe; \u9f20\u6807\u6eda\u52a8: \u7f29\u653e\u5730\u56fe(+Ctrl\u5feb\u901f); Ctrl+\u9f20\u6807\u5de6\u952e: \u9009\u4e2d\u5bfb\u8def\u8d77\u59cb\u70b9; \u9f20\u6807\u5de6\u952e: \u5f00\u59cb\u5bfb\u8def;) \u8017\u65f6: " + AStarMapPanel.this.time + " [" + AStarMapPanel.this.getTile((Point)e.getPoint()).x + ", " + AStarMapPanel.this.getTile((Point)e.getPoint()).y + "]");
                    AStarMapPanel.this.repaint();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            if (!this.move) {
                AstarTestMain.mainFrame.setTitle("AStar\u5bfb\u8def\u7b97\u6cd5\u6d4b\u8bd5(\u9f20\u6807\u53f3\u952e\u62d6\u52a8: \u79fb\u52a8\u5730\u56fe; \u9f20\u6807\u6eda\u52a8: \u7f29\u653e\u5730\u56fe(+Ctrl\u5feb\u901f); Ctrl+\u9f20\u6807\u5de6\u952e: \u9009\u4e2d\u5bfb\u8def\u8d77\u59cb\u70b9; \u9f20\u6807\u5de6\u952e: \u5f00\u59cb\u5bfb\u8def;) \u8017\u65f6: " + AStarMapPanel.this.time + " [" + AStarMapPanel.this.getTile((Point)e.getPoint()).x + ", " + AStarMapPanel.this.getTile((Point)e.getPoint()).y + "]");
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            this.move = false;
            AStarMapPanel.this.setCursor(new Cursor(0));
            super.mouseReleased(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (this.move) {
                int i_mouseX = e.getLocationOnScreen().x - this.o_mouseX;
                int i_mouseY = e.getLocationOnScreen().y - this.o_mouseY;
                this.o_mouseX = e.getLocationOnScreen().x;
                this.o_mouseY = e.getLocationOnScreen().y;
                e.getComponent().setLocation(e.getComponent().getLocation().x + i_mouseX, e.getComponent().getLocation().y + i_mouseY);
            }
            super.mouseDragged(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int speed;
            int n = speed = e.getWheelRotation() > 0 ? 1 : -1;
            if (e.isControlDown()) {
                speed *= 5;
            }
            double x = AStarMapPanel.this.getLocation().getX() + e.getPoint().getX();
            double y = AStarMapPanel.this.getLocation().getY() + e.getPoint().getY();
            int tile = AStarMapPanel.this.getTileSize();
            AStarMapPanel.this.setTileSize(AStarMapPanel.this.tileSize - speed);
            AStarMapPanel.this.setLocation((int)Math.round(x -= e.getPoint().getX() * (double)AStarMapPanel.this.getTileSize() / (double)tile), (int)Math.round(y -= e.getPoint().getY() * (double)AStarMapPanel.this.getTileSize() / (double)tile));
            AStarMapPanel.this.repaint();
            super.mouseWheelMoved(e);
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            char keyChar = e.getKeyChar();
            switch (keyChar) {
                case '1': {
                    AStarMapPanel.access$12(AStarMapPanel.this, !AStarMapPanel.this.drawGrid);
                    AStarMapPanel.this.repaint();
                    break;
                }
                case '2': {
                    AStarMapPanel.access$14(AStarMapPanel.this, !AStarMapPanel.this.drawCoordinate);
                    AStarMapPanel.this.repaint();
                }
            }
        }
    }

}