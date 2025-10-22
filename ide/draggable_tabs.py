"""
Draggable Tab Widget
Custom QTabWidget that supports drag and drop reordering of tabs, just like VS Code.
"""

from PyQt5.QtWidgets import QTabWidget, QTabBar, QApplication
from PyQt5.QtCore import Qt, QPoint, QMimeData, pyqtSignal
from PyQt5.QtGui import QDrag, QPainter


class DraggableTabBar(QTabBar):
    """Custom tab bar that supports dragging tabs to reorder them"""
    
    tab_moved = pyqtSignal(int, int)  # from_index, to_index
    
    def __init__(self, parent=None):
        super().__init__(parent)
        self.setAcceptDrops(True)
        self.drag_start_position = QPoint()
        self.dragging_index = -1
        
    def mousePressEvent(self, event):
        if event.button() == Qt.LeftButton:
            self.drag_start_position = event.pos()
            self.dragging_index = self.tabAt(event.pos())
        super().mousePressEvent(event)
        
    def mouseMoveEvent(self, event):
        if not (event.buttons() & Qt.LeftButton):
            return
            
        # Check if we should start dragging
        if ((event.pos() - self.drag_start_position).manhattanLength() < 
            QApplication.startDragDistance()):
            return
            
        if self.dragging_index >= 0:
            self.start_drag()
            
    def start_drag(self):
        """Start dragging the tab"""
        if self.dragging_index < 0:
            return
            
        tab_text = self.tabText(self.dragging_index)
        tab_icon = self.tabIcon(self.dragging_index)
        
        # Create drag object
        drag = QDrag(self)
        mime_data = QMimeData()
        mime_data.setText(f"tab:{self.dragging_index}:{tab_text}")
        drag.setMimeData(mime_data)
        
        # Create drag pixmap (visual representation of the tab being dragged)
        tab_rect = self.tabRect(self.dragging_index)
        pixmap = self.grab(tab_rect)
        
        # Make it semi-transparent
        painter = QPainter(pixmap)
        painter.setCompositionMode(QPainter.CompositionMode_DestinationIn)
        painter.fillRect(pixmap.rect(), Qt.darkGray)
        painter.end()
        
        drag.setPixmap(pixmap)
        drag.setHotSpot(QPoint(tab_rect.width() // 2, tab_rect.height() // 2))
        
        # Execute drag
        drop_action = drag.exec_(Qt.MoveAction)
        
    def dragEnterEvent(self, event):
        if event.mimeData().hasText() and event.mimeData().text().startswith("tab:"):
            event.acceptProposedAction()
        else:
            event.ignore()
            
    def dragMoveEvent(self, event):
        if event.mimeData().hasText() and event.mimeData().text().startswith("tab:"):
            event.acceptProposedAction()
        else:
            event.ignore()
            
    def dropEvent(self, event):
        if not (event.mimeData().hasText() and event.mimeData().text().startswith("tab:")):
            event.ignore()
            return
            
        # Parse the drag data
        drag_data = event.mimeData().text().split(":", 2)
        if len(drag_data) < 3:
            event.ignore()
            return
            
        from_index = int(drag_data[1])
        drop_position = event.pos()
        to_index = self.tabAt(drop_position)
        
        if to_index < 0:
            # Dropped at the end
            to_index = self.count() - 1
            
        if from_index != to_index and from_index >= 0:
            self.tab_moved.emit(from_index, to_index)
            
        event.acceptProposedAction()


class DraggableTabWidget(QTabWidget):
    """Custom QTabWidget with draggable tabs"""
    
    def __init__(self, parent=None):
        super().__init__(parent)
        
        # Replace the default tab bar with our draggable one
        self.draggable_tab_bar = DraggableTabBar(self)
        self.setTabBar(self.draggable_tab_bar)
        
        # Connect the tab moved signal
        self.draggable_tab_bar.tab_moved.connect(self.move_tab)
        
        # Enable tab closing with middle click
        self.setTabsClosable(False)  # We'll handle closing manually
        self.setMovable(False)  # We handle moving with drag and drop
        
    def move_tab(self, from_index, to_index):
        """Move a tab from one position to another"""
        if from_index == to_index:
            return
            
        # Get the widget and tab data
        widget = self.widget(from_index)
        tab_text = self.tabText(from_index)
        tab_icon = self.tabIcon(from_index)
        tab_tooltip = self.tabToolTip(from_index)
        
        # Remove the tab (but keep the widget)
        self.removeTab(from_index)
        
        # Adjust the target index if necessary
        if from_index < to_index:
            to_index -= 1
            
        # Insert at the new position
        self.insertTab(to_index, widget, tab_icon, tab_text)
        self.setTabToolTip(to_index, tab_tooltip)
        
        # Make the moved tab current
        self.setCurrentIndex(to_index)
        
    def wheelEvent(self, event):
        """Handle mouse wheel scrolling through tabs"""
        if event.angleDelta().y() > 0:
            # Scroll up - previous tab
            current = self.currentIndex()
            if current > 0:
                self.setCurrentIndex(current - 1)
        else:
            # Scroll down - next tab
            current = self.currentIndex()
            if current < self.count() - 1:
                self.setCurrentIndex(current + 1)
                
    def mouseDoubleClickEvent(self, event):
        """Handle double-click to maximize/restore tab area"""
        if event.button() == Qt.LeftButton:
            # Toggle between normal and maximized height
            current_height = self.maximumHeight()
            if current_height == 250:  # Normal height
                self.setMaximumHeight(600)  # Expand
            else:
                self.setMaximumHeight(250)  # Restore
                
    def contextMenuEvent(self, event):
        """Show context menu on right-click"""
        from PyQt5.QtWidgets import QMenu, QAction
        
        tab_index = self.draggable_tab_bar.tabAt(event.pos())
        if tab_index < 0:
            return
            
        menu = QMenu(self)
        
        # Close tab action
        close_action = QAction("Close Tab", self)
        close_action.triggered.connect(lambda: self.close_tab(tab_index))
        menu.addAction(close_action)
        
        # Close other tabs action
        close_others_action = QAction("Close Other Tabs", self)
        close_others_action.triggered.connect(lambda: self.close_other_tabs(tab_index))
        menu.addAction(close_others_action)
        
        menu.addSeparator()
        
        # Move to beginning/end actions
        move_start_action = QAction("Move to Beginning", self)
        move_start_action.triggered.connect(lambda: self.move_tab(tab_index, 0))
        menu.addAction(move_start_action)
        
        move_end_action = QAction("Move to End", self)
        move_end_action.triggered.connect(lambda: self.move_tab(tab_index, self.count() - 1))
        menu.addAction(move_end_action)
        
        menu.exec_(event.globalPos())
        
    def close_tab(self, index):
        """Close a specific tab (if closable)"""
        # For now, just hide the tab since we don't want to lose important panels
        # In a real implementation, you might want to check if the tab can be closed
        widget = self.widget(index)
        if widget:
            widget.setVisible(False)
            
    def close_other_tabs(self, keep_index):
        """Close all tabs except the specified one"""
        for i in range(self.count()):
            if i != keep_index:
                self.close_tab(i)
