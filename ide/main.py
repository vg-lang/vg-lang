import sys
import subprocess
import threading
import json
import os
import re
import difflib
import queue
from PyQt5.QtWidgets import (QApplication, QMainWindow, QTextEdit, QVBoxLayout,
                             QHBoxLayout, QWidget, QPushButton, QToolBar, QSplitter,
                             QListWidget, QLabel, QStatusBar, QMessageBox, QFileDialog,
                             QPlainTextEdit, QAction, QListWidgetItem, QDialog, QLineEdit,
                             QFormLayout, QDialogButtonBox, QCheckBox, QComboBox, QSpinBox,
                             QTabWidget, QColorDialog, QFontDialog)
from PyQt5.QtCore import Qt, QTimer, QThread, pyqtSignal, QSize
from PyQt5.QtGui import QColor, QFont, QPainter, QPen, QTextFormat, QTextCursor


class InteractiveConsole(QTextEdit):
    """Custom QTextEdit for interactive console"""
    def __init__(self, parent=None):
        super().__init__(parent)
        self.parent_editor = parent
        
    def keyPressEvent(self, event):
        if not self.isReadOnly():
            # If console is editable and Enter is pressed, send the input
            if event.key() == Qt.Key_Return or event.key() == Qt.Key_Enter:
                # Get the current content from where the last program output ended
                full_text = self.toPlainText()
                
                # Find the last occurrence of program output to determine where user input starts
                lines = full_text.split('\n')
                user_input = ""
                
                # Look for the last line that doesn't start with program output patterns
                for i in range(len(lines) - 1, -1, -1):
                    line = lines[i].strip()
                    if line and not line.startswith('---') and not line.startswith('🟢') and not line.startswith('Using'):
                        # This might be user input
                        user_input = line
                        break
                
                if user_input and hasattr(self.parent_editor, 'current_process') and self.parent_editor.current_process and self.parent_editor.current_process.poll() is None:
                    # Send the input to the process
                    try:
                        # Convert to bytes for binary mode
                        input_bytes = (user_input + '\n').encode('utf-8')
                        self.parent_editor.current_process.stdin.write(input_bytes)
                        self.parent_editor.current_process.stdin.flush()
                        print(f"Sent input to process: {user_input}")
                        # Add a new line in the console
                        cursor = self.textCursor()
                        cursor.movePosition(cursor.End)
                        self.setTextCursor(cursor)
                        self.insertPlainText('\n')
                        return
                    except Exception as e:
                        print(f"Error sending input to process: {e}")
            
            # For other keys, use default behavior
            super().keyPressEvent(event)
        else:
            # If read-only, ignore all key presses
            pass


class ProjectStartupDialog(QDialog):
    """Startup dialog for project management."""
    
    def __init__(self, recent_projects=None):
        super().__init__()
        self.setWindowTitle("VG Language IDE - Welcome")
        self.setModal(True)
        self.resize(800, 500)
        self.selected_project_path = None
        self.recent_projects = recent_projects or []
        self.setup_ui()
        
    def setup_ui(self):
        """Setup the startup dialog UI."""
        main_layout = QVBoxLayout(self)
        
        # Title
        title_label = QLabel("Welcome to VG Language IDE")
        title_label.setStyleSheet("font-size: 24px; font-weight: bold; margin: 20px; text-align: center;")
        main_layout.addWidget(title_label)
        
        # Content layout (horizontal split)
        content_layout = QHBoxLayout()
        
        # Main action buttons
        buttons_layout = QVBoxLayout()
        
        # Create New Project button
        create_btn = QPushButton("Create New Project")
        create_btn.setStyleSheet("QPushButton { padding: 15px; font-size: 14px; }")
        create_btn.clicked.connect(self.show_new_project_dialog)
        buttons_layout.addWidget(create_btn)
        
        # Open Existing Project button
        open_btn = QPushButton("Open Existing Project")
        open_btn.setStyleSheet("QPushButton { padding: 15px; font-size: 14px; }")
        open_btn.clicked.connect(self.open_existing_project)
        buttons_layout.addWidget(open_btn)
        
        # Recent Projects section
        recent_label = QLabel("Recent Projects:")
        recent_label.setStyleSheet("font-size: 16px; font-weight: bold; margin-top: 20px;")
        buttons_layout.addWidget(recent_label)
        
        self.recent_list = QListWidget()
        self.recent_list.setMaximumHeight(200)
        self.load_recent_projects()
        self.recent_list.itemDoubleClicked.connect(self.open_recent_project)
        buttons_layout.addWidget(self.recent_list)
        
        buttons_layout.addStretch()
        
        # Close button
        close_btn = QPushButton("Exit")
        close_btn.clicked.connect(self.reject)
        buttons_layout.addWidget(close_btn)
        
        content_layout.addLayout(buttons_layout)
        main_layout.addLayout(content_layout)
        
    def show_new_project_dialog(self):
        """Show the new project creation dialog."""
        dialog = NewProjectDialog(self)
        if dialog.exec_() == QDialog.Accepted:
            self.selected_project_path = dialog.project_path
            self.accept()
            
    def open_existing_project(self):
        """Open an existing project."""
        project_path = QFileDialog.getExistingDirectory(self, "Select Project Directory")
        if project_path:
            self.selected_project_path = project_path
            self.accept()
            
    def open_recent_project(self, item):
        """Open a recent project."""
        project_path = item.data(Qt.UserRole)
        if project_path:
            self.selected_project_path = project_path
            self.accept()
        
    def load_recent_projects(self):
        """Load recent projects list."""
        self.recent_list.clear()
        if self.recent_projects:
            for project_path in self.recent_projects:
                if os.path.exists(project_path):
                    item_text = f"📁 {os.path.basename(project_path)} ({project_path})"
                    item = QListWidgetItem(item_text)
                    item.setData(Qt.UserRole, project_path)
                    self.recent_list.addItem(item)
        
        if self.recent_list.count() == 0:
            empty_item = QListWidgetItem("No recent projects")
            empty_item.setFlags(Qt.NoItemFlags)
            self.recent_list.addItem(empty_item)


class NewProjectDialog(QDialog):
    """Dialog for creating new projects with different templates."""
    
    def __init__(self, parent=None):
        super().__init__(parent)
        self.setWindowTitle("Create New Project")
        self.setModal(True)
        self.resize(800, 600)
        self.project_path = None
        self.templates_dir = os.path.join(os.path.dirname(__file__), "templates")
        self.templates = self.load_templates()
        self.setup_ui()
        
    def load_templates(self):
        """Load all available templates from the templates directory."""
        templates = {}
        
        if not os.path.exists(self.templates_dir):
            print(f"Templates directory not found: {self.templates_dir}")
            return templates
            
        try:
            # Iterate through category directories
            for category_dir in os.listdir(self.templates_dir):
                category_path = os.path.join(self.templates_dir, category_dir)
                if os.path.isdir(category_path):
                    # Look for template directories within each category
                    for template_dir in os.listdir(category_path):
                        template_path = os.path.join(category_path, template_dir)
                        if os.path.isdir(template_path):
                            template_file = os.path.join(template_path, "template.json")
                            if os.path.exists(template_file):
                                try:
                                    with open(template_file, 'r', encoding='utf-8') as f:
                                        template_config = json.load(f)
                                        template_config['path'] = template_path
                                        category = template_config.get('category', category_dir.title())
                                        if category not in templates:
                                            templates[category] = []
                                        templates[category].append(template_config)
                                except Exception as e:
                                    print(f"Error loading template {template_file}: {e}")
        except Exception as e:
            print(f"Error reading templates directory: {e}")
            
        return templates
        
    def setup_ui(self):
        """Setup the new project dialog UI."""
        main_layout = QVBoxLayout(self)
        
        # Content layout (horizontal split)
        content_layout = QHBoxLayout()
        
        # Left side menu
        self.menu_list = QListWidget()
        self.menu_list.setMaximumWidth(200)
        
        # Populate categories from loaded templates
        self.categories = list(self.templates.keys())
        if not self.categories:
            # Fallback if no templates are loaded
            self.categories = ["No Templates Found"]
            
        for category in self.categories:
            self.menu_list.addItem(category)
            
        self.menu_list.setCurrentRow(0)
        self.menu_list.currentRowChanged.connect(self.on_category_changed)
        content_layout.addWidget(self.menu_list)
        
        # Right content area
        self.content_widget = QTabWidget()
        self.content_widget.tabBar().setVisible(False)  # Hide tab bar
        content_layout.addWidget(self.content_widget)
        
        # Create tabs for each category
        self.template_lists = {}
        for category in self.categories:
            widget = QWidget()
            layout = QVBoxLayout(widget)
            layout.addWidget(QLabel(f"{category} Templates:"))
            
            templates_list = QListWidget()
            templates_list.setProperty("category", category)
            templates_list.itemClicked.connect(self.on_template_clicked)
            self.template_lists[category] = templates_list
            
            # Populate templates for this category
            if category in self.templates:
                for template in self.templates[category]:
                    item_text = f"{template.get('icon', '📄')} {template.get('name', 'Unknown')} - {template.get('description', 'No description')}"
                    item = QListWidgetItem(item_text)
                    item.setData(Qt.UserRole, template)
                    templates_list.addItem(item)
            else:
                item = QListWidgetItem("No templates available")
                item.setFlags(Qt.NoItemFlags)
                templates_list.addItem(item)
                
            layout.addWidget(templates_list)
            self.content_widget.addTab(widget, category)
        
        main_layout.addLayout(content_layout)
        
        # Project details section
        details_layout = QFormLayout()
        
        self.project_name = QLineEdit()
        self.project_name.setPlaceholderText("Enter project name...")
        details_layout.addRow("Project Name:", self.project_name)
        
        self.project_location = QLineEdit()
        self.project_location.setPlaceholderText("Choose project location...")
        browse_btn = QPushButton("Browse...")
        browse_btn.clicked.connect(self.browse_location)
        location_layout = QHBoxLayout()
        location_layout.addWidget(self.project_location)
        location_layout.addWidget(browse_btn)
        details_layout.addRow("Location:", location_layout)
        
        main_layout.addLayout(details_layout)
        
        # Bottom buttons
        button_layout = QHBoxLayout()
        button_layout.addStretch()
        
        cancel_btn = QPushButton("Cancel")
        cancel_btn.clicked.connect(self.reject)
        button_layout.addWidget(cancel_btn)
        
        self.create_btn = QPushButton("Create Project")
        self.create_btn.clicked.connect(self.create_project)
        self.create_btn.setEnabled(False)
        button_layout.addWidget(self.create_btn)
        
        main_layout.addLayout(button_layout)
        
        # Set initial state
        self.selected_template = None
        
    def on_category_changed(self, index):
        """Handle category selection change."""
        self.content_widget.setCurrentIndex(index)
        
    def on_template_clicked(self, item):
        """Handle template selection."""
        self.selected_template = item.data(Qt.UserRole)
        self.create_btn.setEnabled(self.selected_template is not None)
        
    def browse_location(self):
        """Browse for project location."""
        location = QFileDialog.getExistingDirectory(self, "Select Project Location")
        if location:
            self.project_location.setText(location)
            self.project_location.setText(location)
            
    def create_project(self):
        """Create the new project."""
        name = self.project_name.text().strip()
        location = self.project_location.text().strip()
        
        if not name:
            QMessageBox.warning(self, "Warning", "Please enter a project name.")
            return
            
        if not location:
            QMessageBox.warning(self, "Warning", "Please select a project location.")
            return
            
        if not self.selected_template:
            QMessageBox.warning(self, "Warning", "Please select a template.")
            return
            
        # Create project directory
        self.project_path = os.path.join(location, name)
        try:
            os.makedirs(self.project_path, exist_ok=True)
            
            # Create files from template
            self.create_files_from_template(self.selected_template)
                
            QMessageBox.information(self, "Success", f"Project '{name}' created successfully!")
            self.accept()
            
        except Exception as e:
            QMessageBox.critical(self, "Error", f"Failed to create project: {str(e)}")
            
    def create_files_from_template(self, template):
        """Create project files from template configuration."""
        template_path = template['path']
        
        for file_config in template.get('files', []):
            file_name = file_config['name']
            content_file = file_config['content_file']
            
            # Read template file content
            template_file_path = os.path.join(template_path, content_file)
            if os.path.exists(template_file_path):
                with open(template_file_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                    
                # Create project file (handle subdirectories)
                project_file_path = os.path.join(self.project_path, file_name)
                project_file_dir = os.path.dirname(project_file_path)
                if project_file_dir:
                    os.makedirs(project_file_dir, exist_ok=True)
                    
                with open(project_file_path, 'w', encoding='utf-8') as f:
                    f.write(content)
            else:
                print(f"Template file not found: {template_file_path}")


class LineNumberArea(QWidget):
    """Widget for displaying line numbers and breakpoints."""
    
    def __init__(self, editor):
        super().__init__(editor)
        self.codeEditor = editor

    def sizeHint(self):
        return QSize(self.codeEditor.lineNumberAreaWidth(), 0)

    def paintEvent(self, event):
        self.codeEditor.lineNumberAreaPaintEvent(event)

    def mousePressEvent(self, event):
        # Handle breakpoint toggle on line number area click
        if hasattr(self.codeEditor, 'main_window') and self.codeEditor.main_window:
            line_number = self.codeEditor.getLineNumberFromY(event.y())
            if line_number > 0:
                # Move cursor to clicked line and toggle breakpoint
                cursor = self.codeEditor.textCursor()
                cursor.movePosition(cursor.Start)
                for _ in range(line_number - 1):
                    cursor.movePosition(cursor.Down)
                self.codeEditor.setTextCursor(cursor)
                self.codeEditor.main_window.toggle_breakpoint()


def ensure_qapplication():
    """Ensure QApplication exists before creating dialogs."""
    app = QApplication.instance()
    if app is None:
        app = QApplication(sys.argv)
    return app


class CodeEditor(QPlainTextEdit):
    """Lightweight code editor widget used by the IDE.

    This class previously had its methods at the module level; restore a
    minimal class wrapper and a line-number paint handler so the file
    parses and the editor can be instantiated.
    """
    def __init__(self, parent=None):
        super().__init__(parent)
        # basic state used by methods in this file
        self.search_matches = []
        self.current_match_index = -1
        
        # Create line number area
        self.lineNumberArea = LineNumberArea(self)
        
        # don't wrap long lines by default
        try:
            self.setLineWrapMode(QPlainTextEdit.NoWrap)
        except Exception:
            pass
        # hook text change for highlighting debounce
        try:
            self.textChanged.connect(self.on_text_changed)
        except Exception:
            pass
            
        # Connect signals for line number area
        self.blockCountChanged.connect(self.updateLineNumberAreaWidth)
        self.updateRequest.connect(self.updateLineNumberArea)
        self.cursorPositionChanged.connect(self.highlightCurrentLine)
        
        self.updateLineNumberAreaWidth(0)

    def lineNumberAreaPaintEvent(self, event):
        """Paint the line number area, including space for breakpoint dots."""
        painter = QPainter(self.lineNumberArea)
        
        # Use customizable line number background color
        line_bg_color = QColor(240, 240, 240)  # Default
        if hasattr(self, 'main_window') and hasattr(self.main_window, 'line_bg_color'):
            try:
                color_text = self.main_window.line_bg_color.text()
                if color_text.startswith('#'):
                    line_bg_color = QColor(color_text)
            except:
                pass
        
        painter.fillRect(event.rect(), line_bg_color)

        block = self.firstVisibleBlock()
        blockNumber = block.blockNumber()
        top = int(self.blockBoundingGeometry(block).translated(self.contentOffset()).top())
        bottom = top + int(self.blockBoundingRect(block).height())
        height = int(self.blockBoundingRect(block).height())

        while block.isValid() and (top <= event.rect().bottom()):
            if block.isVisible() and (bottom >= event.rect().top()):
                line_number = blockNumber + 1
                number = str(line_number)
                
                # Draw breakpoint indicator if this line has a breakpoint
                if hasattr(self, 'main_window') and self.main_window and line_number in self.main_window.breakpoints:
                    # Use customizable breakpoint color
                    bp_color = QColor(255, 0, 0)  # Default red
                    if hasattr(self.main_window, 'bp_color'):
                        try:
                            color_text = self.main_window.bp_color.text()
                            if color_text.startswith('#'):
                                bp_color = QColor(color_text)
                        except:
                            pass
                    
                    painter.setBrush(bp_color)
                    painter.setPen(bp_color)
                    # Draw a smaller circle - about 8x8 pixels centered in the line
                    circle_size = 8
                    x_pos = 5
                    y_pos = top + (height - circle_size) // 2
                    painter.drawEllipse(x_pos, y_pos, circle_size, circle_size)
                
                # Draw line number
                painter.setPen(QColor(120, 120, 120))
                painter.drawText(0, top, self.lineNumberArea.width() - 5, height, Qt.AlignRight, number)

            block = block.next()
            top = bottom
            bottom = top + int(self.blockBoundingRect(block).height())
            blockNumber += 1

    def lineNumberAreaWidth(self):
        """Calculate the width needed for the line number area."""
        digits = 1
        max_num = max(1, self.blockCount())
        while max_num >= 10:
            max_num //= 10
            digits += 1
        space = 3 + self.fontMetrics().horizontalAdvance('9') * digits + 20  # Extra space for breakpoint dots
        return space

    def resizeEvent(self, event):
        """Handle resize events to update line number area."""
        super().resizeEvent(event)
        cr = self.contentsRect()
        self.lineNumberArea.setGeometry(cr.left(), cr.top(), self.lineNumberAreaWidth(), cr.height())

    def updateLineNumberAreaWidth(self, newBlockCount):
        """Update the viewport margins when block count changes."""
        self.setViewportMargins(self.lineNumberAreaWidth(), 0, 0, 0)

    def updateLineNumberArea(self, rect, dy):
        """Update the line number area when the editor is scrolled."""
        if dy:
            self.lineNumberArea.scroll(0, dy)
        else:
            self.lineNumberArea.update(0, rect.y(), self.lineNumberArea.width(), rect.height())

        if rect.contains(self.viewport().rect()):
            self.updateLineNumberAreaWidth(0)

    def getLineNumberFromY(self, y):
        """Get line number from Y coordinate in the line number area."""
        block = self.firstVisibleBlock()
        blockNumber = block.blockNumber()
        top = int(self.blockBoundingGeometry(block).translated(self.contentOffset()).top())
        bottom = top + int(self.blockBoundingRect(block).height())

        while block.isValid():
            if top <= y <= bottom:
                return blockNumber + 1
            block = block.next()
            top = bottom
            bottom = top + int(self.blockBoundingRect(block).height())
            blockNumber += 1
        return -1

    def highlightCurrentLine(self):
        extraSelections = []
        if not self.isReadOnly():
            selection = QTextEdit.ExtraSelection()
            
            # Use customizable current line highlight color
            lineColor = QColor(Qt.yellow).lighter(160)  # Default fallback
            if hasattr(self, 'main_window') and hasattr(self.main_window, 'current_line_color_value'):
                try:
                    lineColor = QColor(self.main_window.current_line_color_value)
                except:
                    pass
            
            selection.format.setBackground(lineColor)
            selection.format.setProperty(QTextFormat.FullWidthSelection, True)
            selection.cursor = self.textCursor()
            selection.cursor.clearSelection()
            extraSelections.append(selection)
        self.setExtraSelections(extraSelections)

    def find_text_in_editor(self, find_text, case_sensitive):
        if not find_text:
            return
        text = self.toPlainText()
        import re
        flags = 0 if case_sensitive else re.IGNORECASE
        self.search_matches = [m for m in re.finditer(re.escape(find_text), text, flags)]
        self.current_match_index = 0 if self.search_matches else -1
        count = len(self.search_matches)

        # Highlight all matches
        selections = []
        for m in self.search_matches:
            selection = QTextEdit.ExtraSelection()
            selection.format.setBackground(QColor(200, 255, 200))
            selection.format.setProperty(QTextFormat.FullWidthSelection, True)
            cursor = self.textCursor()
            cursor.setPosition(m.start())
            cursor.setPosition(m.end(), QTextCursor.KeepAnchor)
            selection.cursor = cursor
            selections.append(selection)
        self.setExtraSelections(selections)

        # Move to the first match if available
        if self.search_matches:
            self.navigate_to_match(0)

    def on_text_changed(self):
        """Handle text change events."""
        # This can be used for syntax highlighting or other text change responses
        pass

    def next_match(self):
        if self.current_match_index + 1 < len(self.search_matches):
            self.current_match_index += 1
            self.navigate_to_match(self.current_match_index)

    def previous_match(self):
        if self.current_match_index - 1 >= 0:
            self.current_match_index -= 1
            self.navigate_to_match(self.current_match_index)

    def navigate_to_match(self, index):
        if not self.search_matches or index < 0 or index >= len(self.search_matches):
            return

        self.current_match_index = index
        match = self.search_matches[index]
        cursor = self.textCursor()
        cursor.setPosition(match.start())
        cursor.setPosition(match.end(), QTextCursor.KeepAnchor)
        self.setTextCursor(cursor)
        
        # Update status bar if available
        if hasattr(self, 'main_window') and self.main_window:
            self.main_window.status_bar.showMessage(f"Match {index + 1} of {len(self.search_matches)}")

    def refresh_breakpoints(self):
        """Refresh breakpoint display in the editor."""
        # Update the line number area to show/hide breakpoint indicators
        if hasattr(self, 'lineNumberArea'):
            self.lineNumberArea.update()
        # Also use the highlight_breakpoints method from the main window
        if hasattr(self, 'main_window') and self.main_window:
            self.main_window.highlight_breakpoints()


class DebugThread(QThread):
    output_received = pyqtSignal(str)
    debug_stopped = pyqtSignal()
    
    def __init__(self, file_path, breakpoints=None):
        super().__init__()
        self.file_path = file_path
        self.process = None
        self.is_debugging = False
        self.debug_paused = False
        self.breakpoints = breakpoints or set()
        
    def run(self):
        try:
            # Use VG_EXECUTABLE_PATH environment variable to locate vg.exe
            vg_executable = os.getenv("VG_EXECUTABLE_PATH", "vg.exe")
            abs_file_path = os.path.abspath(self.file_path)
            cmd = [vg_executable, "--debug", abs_file_path]
            if self.breakpoints:
                bp_string = ",".join(map(str, sorted(self.breakpoints)))
                cmd.append(bp_string)
                print(f"IDE: Using user breakpoints: {bp_string}")
            else:
                cmd.append("4")
                print("IDE: No breakpoints set, using default line 4")
            print(f"IDE: Starting debug with command: {' '.join(cmd)}")

            self.process = subprocess.Popen(
                cmd,
                stdin=subprocess.PIPE,
                stdout=subprocess.PIPE,
                stderr=subprocess.STDOUT,
                universal_newlines=True,
                bufsize=0
            )

            self.is_debugging = True

            while self.process.poll() is None:
                output = self.process.stdout.readline()
                if output:
                    output = output.strip()
                    self.output_received.emit(output)
                    if "Debug: Paused at line" in output:
                        self.debug_paused = True

        except Exception as e:
            self.output_received.emit(f"Debug error: {e}")

        self.debug_stopped.emit()
        
    def stop_debug(self):
        if self.process:
            self.process.terminate()
            self.is_debugging = False

class VGEditor(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("VG Language IDE")
        self.resize(1200, 800)
        self.current_file = None
        self.debug_thread = None
        self.breakpoints = set()
        self.is_debugging = False
        self.debug_paused = False
        self.parsing_variables = False
        self.parsing_functions = False
        self.current_variables = {}
        self.current_functions = []
        
        # Initialize settings storage
        self.settings_dir = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'config')
        self.settings_file = os.path.join(self.settings_dir, 'ide_settings.json')
        self.default_settings = {
            'shortcuts': {
                'start_debug': 'F6',
                'continue': 'F8', 
                'step_into': 'F11',
                'step_over': 'F10',
                'step_out': 'Shift+F11',
                'stop_debug': 'Shift+F5',
                'toggle_breakpoint': 'F9',
                'run': 'F5',
                'find': 'Ctrl+F',
                'find_replace': 'Ctrl+H'
            },
            'theme': {
                'ide_background': '#f0f0f0',
                'editor_background': '#ffffff',
                'text_color': '#000000',
                'line_number_background': '#f0f0f0',
                'breakpoint_color': '#ff0000',
                'breakpoint_line_background': '#ffc8c8',
                'selection_background': '#3399ff',
                'current_line_highlight': '#ffffcc',
                'ui_text_color': '#000000',
                'panel_background': '#f0f0f0'
            },
            'text': {
                'font_family': 'Consolas',
                'font_size': 10,
                'tab_size': 4,
                'word_wrap': False,
                'show_line_numbers': True
            }
        }
        
        self.setup_ui()
        self.setup_lsp()
        self.load_settings()
        
        # Initialize project path
        self.current_project_path = None
        
        # Initialize current line highlight color
        self.current_line_color_value = self.current_settings['theme'].get('current_line_highlight', '#ffffcc')
        
        # Initialize process tracking for interactive programs
        self.current_process = None
        
    def load_project(self, project_path):
        """Load a project directory into the IDE."""
        self.current_project_path = project_path
        self.setWindowTitle(f"VG Language IDE - {os.path.basename(project_path)}")
        
        # Clear current file list
        self.file_explorer.clear()
        
        # Load project files
        try:
            for root, dirs, files in os.walk(project_path):
                for file in files:
                    if file.endswith(('.vg', '.vglib', '.txt', '.md')):
                        file_path = os.path.join(root, file)
                        relative_path = os.path.relpath(file_path, project_path)
                        item = QListWidgetItem(relative_path)
                        item.setData(Qt.UserRole, file_path)  # Store full path
                        self.file_explorer.addItem(item)
                        
            # Connect file explorer double-click to open files
            self.file_explorer.itemDoubleClicked.connect(self.open_project_file)
            
        except Exception as e:
            self.status_bar.showMessage(f"Error loading project: {e}")
            
    def open_project_file(self, item):
        """Open a file from the project explorer."""
        file_path = item.data(Qt.UserRole)
        if file_path and os.path.exists(file_path):
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                self.editor.setPlainText(content)
                self.current_file = file_path
                self.setWindowTitle(f"VG Language IDE - {os.path.basename(file_path)}")
                self.status_bar.showMessage(f"Opened: {file_path}")
            except Exception as e:
                QMessageBox.critical(self, "Error", f"Could not open file: {e}")
        
    def setup_ui(self):
        # Create central widget and layout
        central_widget = QWidget()
        self.setCentralWidget(central_widget)
        main_layout = QHBoxLayout(central_widget)

        # Splitter: left file explorer | editor | right panels
        splitter = QSplitter()
        main_layout.addWidget(splitter)

        # Left: File explorer
        explorer_panel = QWidget()
        explorer_layout = QVBoxLayout(explorer_panel)
        explorer_layout.addWidget(QLabel("Files:"))
        self.file_explorer = QListWidget()
        self.file_explorer.setMaximumWidth(300)
        explorer_layout.addWidget(self.file_explorer)
        splitter.addWidget(explorer_panel)

        # Middle: editor panel
        editor_panel = QWidget()
        editor_layout = QVBoxLayout(editor_panel)

        # Toolbar + editor
        toolbar = QToolBar()
        self.setup_toolbar(toolbar)
        editor_layout.addWidget(toolbar)

        self.editor = CodeEditor(self)
        self.editor.main_window = self  # Give editor reference to main window for breakpoints
        self.editor.setFont(QFont("Consolas", 10))
        
        editor_layout.addWidget(self.editor)

        splitter.addWidget(editor_panel)

        # Right: output / variables / breakpoints
        right_panel = QWidget()
        right_layout = QVBoxLayout(right_panel)

        right_layout.addWidget(QLabel("Output:"))
        
        # Create console container
        console_container = QWidget()
        console_layout = QVBoxLayout(console_container)
        console_layout.setContentsMargins(0, 0, 0, 0)
        
        # Console (will be read-only unless running IO programs)
        self.console = InteractiveConsole(self)
        self.console.setMaximumHeight(200)
        self.console.setReadOnly(True)
        console_layout.addWidget(self.console)
        
        console_layout.addWidget(QLabel(""))  # Small spacer
        right_layout.addWidget(console_container)

        right_layout.addWidget(QLabel("Variables:"))
        self.variables_list = QListWidget()
        self.variables_list.setMaximumHeight(150)
        right_layout.addWidget(self.variables_list)

        # Debug controls are available via keyboard shortcuts (menu actions).
        # Shortcuts: F6 Start, F8 Continue, F11 Step Into, F10 Step Over,
        # Shift+F11 Step Out, Shift+F5 Stop, F9 Toggle Breakpoint
        dbg_shortcuts_label = QLabel(
            "Debug via shortcuts: F6 Start, F8 Continue, F11 Step Into, F10 Step Over, Shift+F11 Step Out, Shift+F5 Stop"
        )
        dbg_shortcuts_label.setWordWrap(True)
        right_layout.addWidget(dbg_shortcuts_label)


        right_layout.addWidget(QLabel("Breakpoints:"))
        self.breakpoints_list = QListWidget()
        self.breakpoints_list.setMaximumHeight(100)
        right_layout.addWidget(self.breakpoints_list)

        splitter.addWidget(right_panel)
        splitter.setSizes([200, 800, 400])

        # Status bar and menu
        self.status_bar = QStatusBar()
        self.setStatusBar(self.status_bar)
        self.status_bar.showMessage("Ready")
        self.setup_menu()

        # Wire file explorer interactions and populate
        self.file_explorer.itemDoubleClicked.connect(self._open_file_from_explorer)
        self.update_file_explorer()
        
    def setup_toolbar(self, toolbar):
        # Minimal toolbar: File actions + Run + Debug + Breakpoint
        self.new_action = QPushButton("New")
        self.new_action.clicked.connect(self.new_file)
        toolbar.addWidget(self.new_action)

        self.open_action = QPushButton("Open")
        self.open_action.clicked.connect(self.open_file)
        toolbar.addWidget(self.open_action)

        self.save_action = QPushButton("Save")
        self.save_action.clicked.connect(self.save_file)
        toolbar.addWidget(self.save_action)

        toolbar.addSeparator()

        # Run and Debug quick buttons (minimal)
        self.run_button = QPushButton("Run")
        self.run_button.clicked.connect(self.run_code)
        toolbar.addWidget(self.run_button)

        self.debug_button = QPushButton("Debug")
        self.debug_button.clicked.connect(self.start_debug)
        toolbar.addWidget(self.debug_button)

        toolbar.addSeparator()

        # Breakpoint toggle kept in toolbar for quick access
        self.breakpoint_button = QPushButton("Toggle Breakpoint")
        self.breakpoint_button.clicked.connect(self.toggle_breakpoint)
        toolbar.addWidget(self.breakpoint_button)

    def setup_menu(self):
        menubar = self.menuBar()

        # File menu
        file_menu = menubar.addMenu('File')
        file_menu.addAction('New', self.new_file)
        file_menu.addAction('Open', self.open_file)
        file_menu.addAction('Save', self.save_file)
        file_menu.addAction('Save As', self.save_as_file)
        file_menu.addSeparator()
        file_menu.addAction('Exit', self.close)

        # Edit menu
        edit_menu = menubar.addMenu('Edit')
        edit_menu.addAction('Cut', self.editor.cut)
        edit_menu.addAction('Copy', self.editor.copy)
        edit_menu.addAction('Paste', self.editor.paste)
        edit_menu.addSeparator()

        # Add keyboard shortcut for toggle breakpoint
        toggle_bp_action = QAction('Toggle Breakpoint', self)
        toggle_bp_action.setShortcut('F9')
        toggle_bp_action.triggered.connect(self.toggle_breakpoint)
        edit_menu.addAction(toggle_bp_action)

        # Find actions (shortcuts only, dialog opens)
        find_action = QAction('Find', self)
        find_action.setShortcut('Ctrl+F')
        find_action.triggered.connect(self.show_find_dialog)
        edit_menu.addAction(find_action)

        find_replace_action = QAction('Find/Replace', self)
        find_replace_action.setShortcut('Ctrl+H')
        find_replace_action.triggered.connect(self.show_find_replace_dialog)
        edit_menu.addAction(find_replace_action)

        # Run menu
        run_menu = menubar.addMenu('Run')
        run_act = QAction('Run (F5)', self)
        run_act.setShortcut('F5')
        run_act.triggered.connect(self.run_code)
        run_menu.addAction(run_act)
        # keep reference so enable/disable can target the menu action as well
        self.run_action = run_act

        run_no_debug_act = QAction('Run Without Debug', self)
        # no extra shortcut assigned; can be added if desired
        run_no_debug_act.triggered.connect(self.run_code)
        run_menu.addAction(run_no_debug_act)
        self.run_no_debug_action = run_no_debug_act

        # Debug menu
        debug_menu = menubar.addMenu('Debug')

        start_debug_act = QAction('Start Debug (F6)', self)
        start_debug_act.setShortcut('F6')
        start_debug_act.triggered.connect(self.start_debug)
        debug_menu.addAction(start_debug_act)
        self.start_debug_action = start_debug_act

        continue_act = QAction('Continue (F8)', self)
        continue_act.setShortcut('F8')
        continue_act.triggered.connect(self.debug_continue)
        debug_menu.addAction(continue_act)
        self.continue_action = continue_act

        step_into_act = QAction('Step Into (F11)', self)
        step_into_act.setShortcut('F11')
        step_into_act.triggered.connect(self.debug_step_into)
        debug_menu.addAction(step_into_act)
        self.step_into_action = step_into_act

        step_over_act = QAction('Step Over (F10)', self)
        step_over_act.setShortcut('F10')
        step_over_act.triggered.connect(self.debug_step_over)
        debug_menu.addAction(step_over_act)
        self.step_over_action = step_over_act

        step_out_act = QAction('Step Out (Shift+F11)', self)
        step_out_act.setShortcut('Shift+F11')
        step_out_act.triggered.connect(self.debug_step_out)
        debug_menu.addAction(step_out_act)
        self.step_out_action = step_out_act

        stop_debug_act = QAction('Stop Debug (Shift+F5)', self)
        stop_debug_act.setShortcut('Shift+F5')
        stop_debug_act.triggered.connect(self.stop_debug)
        debug_menu.addAction(stop_debug_act)
        self.stop_debug_action = stop_debug_act

        vars_act = QAction('Show Variables', self)
        vars_act.triggered.connect(self.debug_variables)
        debug_menu.addAction(vars_act)
        self.vars_action = vars_act

        debug_menu.addSeparator()
        addbreak_act = QAction('Add Breakpoint at Cursor', self)
        addbreak_act.triggered.connect(self.toggle_breakpoint)
        debug_menu.addAction(addbreak_act)

        # View menu: toggle panels
        view_menu = menubar.addMenu('View')
        toggle_output_act = QAction('Toggle Output Panel', self)
        toggle_output_act.triggered.connect(lambda: self.console.setVisible(not self.console.isVisible()))
        view_menu.addAction(toggle_output_act)

        toggle_variables_act = QAction('Toggle Variables Panel', self)
        toggle_variables_act.triggered.connect(lambda: self.variables_list.setVisible(not self.variables_list.isVisible()))
        view_menu.addAction(toggle_variables_act)

        toggle_breakpoints_act = QAction('Toggle Breakpoints Panel', self)
        toggle_breakpoints_act.triggered.connect(lambda: self.breakpoints_list.setVisible(not self.breakpoints_list.isVisible()))
        view_menu.addAction(toggle_breakpoints_act)

        # Package Manager menu
        pkg_menu = menubar.addMenu('Package Manager')
        pkg_install_act = QAction('Install Package', self)
        pkg_install_act.triggered.connect(self.show_package_install_dialog)
        pkg_menu.addAction(pkg_install_act)
        pkg_remove_act = QAction('Remove Package', self)
        pkg_remove_act.triggered.connect(self.show_package_remove_dialog)
        pkg_menu.addAction(pkg_remove_act)

        # Settings menu
        settings_menu = menubar.addMenu('Settings')
        settings_act = QAction('Preferences', self)
        settings_act.triggered.connect(self.show_settings_dialog)
        settings_menu.addAction(settings_act)

    def show_find_dialog(self):
        ensure_qapplication()
        dialog = QDialog(self)
        dialog.setWindowTitle("Find")
        layout = QFormLayout(dialog)

        find_input = QLineEdit(dialog)
        case_checkbox = QCheckBox("Case sensitive", dialog)
        layout.addRow("Find:", find_input)
        layout.addRow("", case_checkbox)

        buttons = QDialogButtonBox(QDialogButtonBox.Ok | QDialogButtonBox.Cancel, dialog)
        layout.addWidget(buttons)

        def on_ok():
            find_text = find_input.text()
            case_sensitive = case_checkbox.isChecked()
            self.editor.find_text_in_editor(find_text, case_sensitive)
            dialog.accept()

        buttons.accepted.connect(on_ok)
        buttons.rejected.connect(dialog.reject)
        dialog.exec_()

    def show_find_replace_dialog(self):
        ensure_qapplication()
        dialog = QDialog(self)
        dialog.setWindowTitle("Find and Replace")
        layout = QFormLayout(dialog)

        find_input = QLineEdit(dialog)
        replace_input = QLineEdit(dialog)
        case_checkbox = QCheckBox("Case sensitive", dialog)
        layout.addRow("Find:", find_input)
        layout.addRow("Replace:", replace_input)
        layout.addRow("", case_checkbox)

        buttons = QDialogButtonBox(QDialogButtonBox.Ok | QDialogButtonBox.Cancel, dialog)
        layout.addWidget(buttons)

        def on_ok():
            find_text = find_input.text()
            replace_text = replace_input.text()
            case_sensitive = case_checkbox.isChecked()
            self.find_and_replace(find_text, replace_text, case_sensitive)
            dialog.accept()

        buttons.accepted.connect(on_ok)
        buttons.rejected.connect(dialog.reject)
        dialog.exec_()

    def find_and_replace(self, find_text, replace_text, case_sensitive):
        """Find and replace text in the editor."""
        if not find_text:
            return
        text = self.editor.toPlainText()
        import re
        flags = 0 if case_sensitive else re.IGNORECASE
        new_text = re.sub(re.escape(find_text), replace_text, text, flags=flags)
        self.editor.setPlainText(new_text)

    def show_settings_dialog(self):
        """Show the settings dialog with side menu for Shortcuts, Theme, and Text."""
        ensure_qapplication()
        dialog = QDialog(self)
        dialog.setWindowTitle("Settings")
        dialog.setModal(True)
        dialog.resize(800, 600)
        
        # Main dialog layout
        dialog_layout = QVBoxLayout(dialog)
        
        # Content layout (horizontal split)
        content_layout = QHBoxLayout()
        
        # Left side menu
        menu_list = QListWidget()
        menu_list.setMaximumWidth(150)
        menu_list.addItem("Shortcuts")
        menu_list.addItem("Theme")
        menu_list.addItem("Text")
        menu_list.setCurrentRow(0)
        content_layout.addWidget(menu_list)
        
        # Right content area with tab widget (hidden tabs, controlled by menu)
        content_widget = QTabWidget()
        content_widget.tabBar().setVisible(False)  # Hide tab bar
        content_layout.addWidget(content_widget)
        
        # Shortcuts tab
        shortcuts_widget = QWidget()
        shortcuts_layout = QVBoxLayout(shortcuts_widget)
        shortcuts_layout.addWidget(QLabel("Debug Shortcuts (click to edit):"))
        
        shortcuts_form = QFormLayout()
        
        # Store shortcut inputs for later access
        self.shortcut_inputs = {}
        
        # Debug shortcuts - load from saved settings
        self.shortcut_inputs['start_debug'] = QLineEdit(self.current_settings['shortcuts']['start_debug'])
        shortcuts_form.addRow("Start Debug:", self.shortcut_inputs['start_debug'])
        
        self.shortcut_inputs['continue'] = QLineEdit(self.current_settings['shortcuts']['continue'])
        shortcuts_form.addRow("Continue:", self.shortcut_inputs['continue'])
        
        self.shortcut_inputs['step_into'] = QLineEdit(self.current_settings['shortcuts']['step_into'])
        shortcuts_form.addRow("Step Into:", self.shortcut_inputs['step_into'])
        
        self.shortcut_inputs['step_over'] = QLineEdit(self.current_settings['shortcuts']['step_over'])
        shortcuts_form.addRow("Step Over:", self.shortcut_inputs['step_over'])
        
        self.shortcut_inputs['step_out'] = QLineEdit(self.current_settings['shortcuts']['step_out'])
        shortcuts_form.addRow("Step Out:", self.shortcut_inputs['step_out'])
        
        self.shortcut_inputs['stop_debug'] = QLineEdit(self.current_settings['shortcuts']['stop_debug'])
        shortcuts_form.addRow("Stop Debug:", self.shortcut_inputs['stop_debug'])
        
        self.shortcut_inputs['toggle_breakpoint'] = QLineEdit(self.current_settings['shortcuts']['toggle_breakpoint'])
        shortcuts_form.addRow("Toggle Breakpoint:", self.shortcut_inputs['toggle_breakpoint'])
        
        shortcuts_layout.addLayout(shortcuts_form)
        
        shortcuts_layout.addWidget(QLabel("\nFile Shortcuts:"))
        file_shortcuts_form = QFormLayout()
        
        self.shortcut_inputs['run'] = QLineEdit(self.current_settings['shortcuts']['run'])
        file_shortcuts_form.addRow("Run:", self.shortcut_inputs['run'])
        
        self.shortcut_inputs['find'] = QLineEdit(self.current_settings['shortcuts']['find'])
        file_shortcuts_form.addRow("Find:", self.shortcut_inputs['find'])
        
        self.shortcut_inputs['find_replace'] = QLineEdit(self.current_settings['shortcuts']['find_replace'])
        file_shortcuts_form.addRow("Find/Replace:", self.shortcut_inputs['find_replace'])
        
        shortcuts_layout.addLayout(file_shortcuts_form)
        shortcuts_layout.addStretch()
        content_widget.addTab(shortcuts_widget, "Shortcuts")
        
        # Theme tab
        theme_widget = QWidget()
        theme_layout = QVBoxLayout(theme_widget)
        theme_layout.addWidget(QLabel("Theme Settings:"))
        
        theme_form = QFormLayout()
        
        # IDE background color
        ide_bg_layout = QHBoxLayout()
        self.ide_bg_color = QLabel(self.current_settings['theme']['ide_background'])
        self.ide_bg_color.setStyleSheet(f"background-color: {self.current_settings['theme']['ide_background']}; border: 1px solid gray; padding: 5px;")
        ide_bg_btn = QPushButton("Choose Color")
        ide_bg_btn.clicked.connect(lambda: self.choose_color(self.ide_bg_color, "ide_bg"))
        ide_bg_layout.addWidget(self.ide_bg_color)
        ide_bg_layout.addWidget(ide_bg_btn)
        ide_bg_layout.addStretch()
        theme_form.addRow("IDE Background:", ide_bg_layout)
        
        # Editor background color
        editor_bg_layout = QHBoxLayout()
        self.editor_bg_color = QLabel(self.current_settings['theme']['editor_background'])
        self.editor_bg_color.setStyleSheet(f"background-color: {self.current_settings['theme']['editor_background']}; border: 1px solid gray; padding: 5px;")
        editor_bg_btn = QPushButton("Choose Color")
        editor_bg_btn.clicked.connect(lambda: self.choose_color(self.editor_bg_color, "editor_bg"))
        editor_bg_layout.addWidget(self.editor_bg_color)
        editor_bg_layout.addWidget(editor_bg_btn)
        editor_bg_layout.addStretch()
        theme_form.addRow("Editor Background:", editor_bg_layout)
        
        # Text color
        text_color_layout = QHBoxLayout()
        self.text_color = QLabel(self.current_settings['theme']['text_color'])
        self.text_color.setStyleSheet(f"background-color: {self.current_settings['theme']['text_color']}; color: white; border: 1px solid gray; padding: 5px;")
        text_color_btn = QPushButton("Choose Color")
        text_color_btn.clicked.connect(lambda: self.choose_color(self.text_color, "text_color"))
        text_color_layout.addWidget(self.text_color)
        text_color_layout.addWidget(text_color_btn)
        text_color_layout.addStretch()
        theme_form.addRow("Text Color:", text_color_layout)
        
        # Line number background color
        line_bg_layout = QHBoxLayout()
        self.line_bg_color = QLabel(self.current_settings['theme']['line_number_background'])
        self.line_bg_color.setStyleSheet(f"background-color: {self.current_settings['theme']['line_number_background']}; border: 1px solid gray; padding: 5px;")
        line_bg_btn = QPushButton("Choose Color")
        line_bg_btn.clicked.connect(lambda: self.choose_color(self.line_bg_color, "line_bg"))
        line_bg_layout.addWidget(self.line_bg_color)
        line_bg_layout.addWidget(line_bg_btn)
        line_bg_layout.addStretch()
        theme_form.addRow("Line Number Background:", line_bg_layout)
        
        # Breakpoint color
        bp_color_layout = QHBoxLayout()
        self.bp_color = QLabel(self.current_settings['theme']['breakpoint_color'])
        self.bp_color.setStyleSheet(f"background-color: {self.current_settings['theme']['breakpoint_color']}; border: 1px solid gray; padding: 5px;")
        bp_color_btn = QPushButton("Choose Color")
        bp_color_btn.clicked.connect(lambda: self.choose_color(self.bp_color, "breakpoint"))
        bp_color_layout.addWidget(self.bp_color)
        bp_color_layout.addWidget(bp_color_btn)
        bp_color_layout.addStretch()
        theme_form.addRow("Breakpoint Color:", bp_color_layout)
        
        # Breakpoint line background color
        bp_line_bg_layout = QHBoxLayout()
        self.bp_line_bg_color = QLabel(self.current_settings['theme']['breakpoint_line_background'])
        self.bp_line_bg_color.setStyleSheet(f"background-color: {self.current_settings['theme']['breakpoint_line_background']}; border: 1px solid gray; padding: 5px;")
        bp_line_bg_btn = QPushButton("Choose Color")
        bp_line_bg_btn.clicked.connect(lambda: self.choose_color(self.bp_line_bg_color, "breakpoint_line_bg"))
        bp_line_bg_layout.addWidget(self.bp_line_bg_color)
        bp_line_bg_layout.addWidget(bp_line_bg_btn)
        bp_line_bg_layout.addStretch()
        theme_form.addRow("Breakpoint Line Background:", bp_line_bg_layout)
        
        # Selection background color
        selection_bg_layout = QHBoxLayout()
        self.selection_bg_color = QLabel(self.current_settings['theme']['selection_background'])
        self.selection_bg_color.setStyleSheet(f"background-color: {self.current_settings['theme']['selection_background']}; border: 1px solid gray; padding: 5px;")
        selection_bg_btn = QPushButton("Choose Color")
        selection_bg_btn.clicked.connect(lambda: self.choose_color(self.selection_bg_color, "selection_bg"))
        selection_bg_layout.addWidget(self.selection_bg_color)
        selection_bg_layout.addWidget(selection_bg_btn)
        selection_bg_layout.addStretch()
        theme_form.addRow("Selection Background:", selection_bg_layout)
        
        # Current line highlight color
        current_line_layout = QHBoxLayout()
        self.current_line_color = QLabel(self.current_settings['theme']['current_line_highlight'])
        self.current_line_color.setStyleSheet(f"background-color: {self.current_settings['theme']['current_line_highlight']}; border: 1px solid gray; padding: 5px;")
        current_line_btn = QPushButton("Choose Color")
        current_line_btn.clicked.connect(lambda: self.choose_color(self.current_line_color, "current_line"))
        current_line_layout.addWidget(self.current_line_color)
        current_line_layout.addWidget(current_line_btn)
        current_line_layout.addStretch()
        theme_form.addRow("Current Line Highlight:", current_line_layout)
        
        # UI Text Color
        ui_text_layout = QHBoxLayout()
        self.ui_text_color = QLabel(self.current_settings['theme']['ui_text_color'])
        self.ui_text_color.setStyleSheet(f"background-color: {self.current_settings['theme']['ui_text_color']}; color: white; border: 1px solid gray; padding: 5px;")
        ui_text_btn = QPushButton("Choose Color")
        ui_text_btn.clicked.connect(lambda: self.choose_color(self.ui_text_color, "ui_text"))
        ui_text_layout.addWidget(self.ui_text_color)
        ui_text_layout.addWidget(ui_text_btn)
        ui_text_layout.addStretch()
        theme_form.addRow("UI Text Color:", ui_text_layout)
        
        # Panel Background Color
        panel_bg_layout = QHBoxLayout()
        self.panel_bg_color = QLabel(self.current_settings['theme']['panel_background'])
        self.panel_bg_color.setStyleSheet(f"background-color: {self.current_settings['theme']['panel_background']}; border: 1px solid gray; padding: 5px;")
        panel_bg_btn = QPushButton("Choose Color")
        panel_bg_btn.clicked.connect(lambda: self.choose_color(self.panel_bg_color, "panel_bg"))
        panel_bg_layout.addWidget(self.panel_bg_color)
        panel_bg_layout.addWidget(panel_bg_btn)
        panel_bg_layout.addStretch()
        theme_form.addRow("Panel Background:", panel_bg_layout)
        
        theme_layout.addLayout(theme_form)
        theme_layout.addStretch()
        content_widget.addTab(theme_widget, "Theme")
        
        # Text tab
        text_widget = QWidget()
        text_layout = QVBoxLayout(text_widget)
        text_layout.addWidget(QLabel("Text Settings:"))
        
        text_form = QFormLayout()
        
        # Font family selection
        font_family_layout = QHBoxLayout()
        self.font_family_combo = QComboBox()
        # Add common programming fonts
        fonts = ["Consolas", "Courier New", "Monaco", "Menlo", "Source Code Pro", 
                "Fira Code", "JetBrains Mono", "Roboto Mono", "Ubuntu Mono", "DejaVu Sans Mono"]
        self.font_family_combo.addItems(fonts)
        self.font_family_combo.setCurrentText(self.current_settings['text']['font_family'])
        font_family_layout.addWidget(self.font_family_combo)
        font_family_layout.addStretch()
        text_form.addRow("Font Family:", font_family_layout)
        
        # Font size
        font_size_layout = QHBoxLayout()
        self.font_size_spin = QSpinBox()
        self.font_size_spin.setRange(6, 72)
        self.font_size_spin.setValue(self.current_settings['text']['font_size'])
        font_size_layout.addWidget(self.font_size_spin)
        font_size_layout.addStretch()
        text_form.addRow("Font Size:", font_size_layout)
        
        # Font preview
        font_preview_layout = QHBoxLayout()
        self.font_preview_label = QLabel("Sample text: def hello_world():")
        self.font_preview_label.setStyleSheet("border: 1px solid gray; padding: 10px; background-color: white;")
        font_preview_btn = QPushButton("Choose Font")
        font_preview_btn.clicked.connect(self.choose_font)
        font_preview_layout.addWidget(self.font_preview_label)
        font_preview_layout.addWidget(font_preview_btn)
        font_preview_layout.addStretch()
        text_form.addRow("Font Preview:", font_preview_layout)
        
        # Connect font changes to preview update
        self.font_family_combo.currentTextChanged.connect(self.update_font_preview)
        self.font_size_spin.valueChanged.connect(self.update_font_preview)
        
        # Tab size
        self.tab_size_spin = QSpinBox()
        self.tab_size_spin.setRange(1, 16)
        self.tab_size_spin.setValue(self.current_settings['text']['tab_size'])
        text_form.addRow("Tab Size:", self.tab_size_spin)
        
        # Word wrap
        self.word_wrap = QCheckBox("Enable word wrap")
        self.word_wrap.setChecked(self.current_settings['text']['word_wrap'])
        text_form.addRow("", self.word_wrap)
        
        # Show line numbers
        self.show_line_numbers = QCheckBox("Show line numbers")
        self.show_line_numbers.setChecked(self.current_settings['text']['show_line_numbers'])
        text_form.addRow("", self.show_line_numbers)
        
        text_layout.addLayout(text_form)
        text_layout.addStretch()
        content_widget.addTab(text_widget, "Text")
        
        # Connect menu selection to tab change
        menu_list.currentRowChanged.connect(content_widget.setCurrentIndex)
        
        # Initialize font preview
        self.update_font_preview()
        
        # Add content to dialog
        dialog_layout.addLayout(content_layout)
        
        # Dialog buttons
        button_layout = QHBoxLayout()
        button_layout.addStretch()
        
        apply_btn = QPushButton("Apply")
        apply_btn.clicked.connect(lambda: self.apply_settings(dialog))
        
        ok_btn = QPushButton("OK")
        ok_btn.clicked.connect(lambda: self.apply_settings_and_close(dialog))
        
        cancel_btn = QPushButton("Cancel")
        cancel_btn.clicked.connect(dialog.reject)
        
        button_layout.addWidget(apply_btn)
        button_layout.addWidget(ok_btn)
        button_layout.addWidget(cancel_btn)
        
        dialog_layout.addLayout(button_layout)
        
        dialog.exec_()

    def choose_color(self, label_widget, color_type):
        """Open color chooser and update the label."""
        color = QColorDialog.getColor()
        if color.isValid():
            if color_type == "text_color":
                # For text color, show it with white background for better visibility
                label_widget.setStyleSheet(f"background-color: white; color: {color.name()}; border: 1px solid gray; padding: 5px;")
            else:
                label_widget.setStyleSheet(f"background-color: {color.name()}; border: 1px solid gray; padding: 5px;")
            label_widget.setText(color.name())

    def update_font_preview(self):
        """Update the font preview when family or size changes."""
        if hasattr(self, 'font_preview_label'):
            family = self.font_family_combo.currentText()
            size = self.font_size_spin.value()
            font = QFont(family, size)
            self.font_preview_label.setFont(font)
            self.font_preview_label.setText(f"Sample text: def hello_world(): # {family} {size}pt")

    def choose_font(self):
        """Open font chooser and update the font controls."""
        current_font = QFont(self.font_family_combo.currentText(), self.font_size_spin.value())
        font, ok = QFontDialog.getFont(current_font, self)
        if ok:
            # Update the combo box and spin box
            self.font_family_combo.setCurrentText(font.family())
            self.font_size_spin.setValue(font.pointSize())
            self.update_font_preview()

    def apply_settings(self, dialog):
        """Apply the current settings."""
        # Apply keyboard shortcuts
        if hasattr(self, 'shortcut_inputs'):
            shortcuts = self.shortcut_inputs
            
            # Update debug shortcuts
            if hasattr(self, 'start_debug_action'):
                self.start_debug_action.setShortcut(shortcuts['start_debug'].text())
            if hasattr(self, 'continue_action'):
                self.continue_action.setShortcut(shortcuts['continue'].text())
            if hasattr(self, 'step_into_action'):
                self.step_into_action.setShortcut(shortcuts['step_into'].text())
            if hasattr(self, 'step_over_action'):
                self.step_over_action.setShortcut(shortcuts['step_over'].text())
            if hasattr(self, 'step_out_action'):
                self.step_out_action.setShortcut(shortcuts['step_out'].text())
            if hasattr(self, 'stop_debug_action'):
                self.stop_debug_action.setShortcut(shortcuts['stop_debug'].text())
            
            # Update other shortcuts
            if hasattr(self, 'run_action'):
                self.run_action.setShortcut(shortcuts['run'].text())
            
            # Find shortcuts
            for action in self.menuBar().actions():
                for sub_action in action.menu().actions() if action.menu() else []:
                    if sub_action.text() == 'Find':
                        sub_action.setShortcut(shortcuts['find'].text())
                    elif sub_action.text() == 'Find/Replace':
                        sub_action.setShortcut(shortcuts['find_replace'].text())
                    elif sub_action.text() == 'Toggle Breakpoint':
                        sub_action.setShortcut(shortcuts['toggle_breakpoint'].text())
        
        # Apply font settings
        if hasattr(self, 'font_family_combo') and hasattr(self, 'font_size_spin'):
            family = self.font_family_combo.currentText()
            size = self.font_size_spin.value()
            font = QFont(family, size)
            self.editor.setFont(font)
        
        # Apply text color and editor background
        if hasattr(self, 'text_color') and hasattr(self, 'editor_bg_color'):
            text_color = self.text_color.text()
            editor_bg = self.editor_bg_color.text()
            selection_bg = self.selection_bg_color.text() if hasattr(self, 'selection_bg_color') else '#3399ff'
            
            # Use stylesheet for better color application
            editor_style = f"""
            QPlainTextEdit {{
                color: {text_color};
                background-color: {editor_bg};
                selection-background-color: {selection_bg};
                border: none;
            }}
            """
            self.editor.setStyleSheet(editor_style)
        
        # Apply IDE background and UI colors
        if hasattr(self, 'ide_bg_color'):
            color = self.ide_bg_color.text()
            text_color = self.text_color.text() if hasattr(self, 'text_color') else '#000000'
            editor_bg = self.editor_bg_color.text() if hasattr(self, 'editor_bg_color') else '#ffffff'
            selection_bg = self.selection_bg_color.text() if hasattr(self, 'selection_bg_color') else '#3399ff'
            ui_text_color = self.ui_text_color.text() if hasattr(self, 'ui_text_color') else '#000000'
            panel_bg = self.panel_bg_color.text() if hasattr(self, 'panel_bg_color') else '#f0f0f0'
            
            if color.startswith('#'):
                self.setStyleSheet(f"""
                    QMainWindow {{ 
                        background-color: {color}; 
                    }}
                    QMainWindow QPlainTextEdit {{
                        color: {text_color};
                        background-color: {editor_bg};
                        selection-background-color: {selection_bg};
                        border: none;
                    }}
                    QListWidget {{
                        background-color: {panel_bg};
                        color: {ui_text_color};
                        border: 1px solid gray;
                    }}
                    QListWidget::item {{
                        color: {ui_text_color};
                        padding: 2px;
                    }}
                    QListWidget::item:hover {{
                        background-color: {selection_bg};
                    }}
                    QTextEdit {{
                        background-color: {panel_bg};
                        color: {ui_text_color};
                        border: 1px solid gray;
                    }}
                    QLabel {{
                        color: {ui_text_color};
                    }}
                    QPushButton {{
                        background-color: white;
                        color: black;
                        border: none;
                        padding: 6px 12px;
                        border-radius: 3px;
                    }}
                    QPushButton:hover {{
                        background-color: {selection_bg};
                        color: white;
                    }}
                    QPushButton:pressed {{
                        background-color: {selection_bg};
                    }}
                    QMenuBar {{
                        background-color: {panel_bg};
                    }}
                    QMenuBar::item {{
                        background-color: {panel_bg};
                    }}
                    QMenuBar::item:selected {{
                        background-color: {selection_bg};
                    }}
                    QMenu {{
                        background-color: {panel_bg};
                        border: 1px solid gray;
                    }}
                    QMenu::item:selected {{
                        background-color: {selection_bg};
                    }}
                    QToolBar {{
                        background-color: {panel_bg};
                        border: none;
                        spacing: 2px;
                    }}
                    QToolBar QPushButton {{
                        background-color: white;
                        color: black;
                        border: none;
                        padding: 8px 12px;
                        margin: 2px;
                        border-radius: 3px;
                    }}
                    QToolBar QPushButton:hover {{
                        background-color: {selection_bg};
                        color: white;
                    }}
                    QStatusBar {{
                        background-color: {panel_bg};
                        color: {ui_text_color};
                    }}
                """)
        
        # Apply word wrap
        if hasattr(self, 'word_wrap'):
            if self.word_wrap.isChecked():
                self.editor.setLineWrapMode(QPlainTextEdit.WidgetWidth)
            else:
                self.editor.setLineWrapMode(QPlainTextEdit.NoWrap)
        
        # Apply line number visibility
        if hasattr(self, 'show_line_numbers'):
            if hasattr(self.editor, 'lineNumberArea'):
                self.editor.lineNumberArea.setVisible(self.show_line_numbers.isChecked())
                self.editor.updateLineNumberAreaWidth(0)
        
        # Force editor refresh
        self.editor.update()
        if hasattr(self.editor, 'lineNumberArea'):
            self.editor.lineNumberArea.update()
        
        # Apply current line highlight color
        if hasattr(self, 'current_line_color'):
            current_line_color = self.current_line_color.text()
            self.apply_current_line_highlight(current_line_color)
        
        self.status_bar.showMessage("Settings applied")
        # Save settings to file
        self.save_settings()

    def load_settings(self):
        """Load settings from the config file and apply them to the IDE."""
        try:
            print(f"Looking for settings file: {self.settings_file}")
            if os.path.exists(self.settings_file):
                print("Settings file found, loading...")
                with open(self.settings_file, 'r') as f:
                    settings = json.load(f)
                    
                # Merge with defaults to ensure all keys exist
                self.current_settings = self.default_settings.copy()
                for category in settings:
                    if category in self.current_settings:
                        self.current_settings[category].update(settings[category])
                        
                # Load recent projects separately
                self.recent_projects = settings.get('recent_projects', [])
                        
                print(f"Loaded settings: {self.current_settings}")
            else:
                print("Settings file not found, using defaults")
                self.current_settings = self.default_settings.copy()
                self.recent_projects = []
                
            # Apply loaded settings to the IDE
            self.apply_loaded_settings()
            
        except Exception as e:
            print(f"Error loading settings: {e}")
            import traceback
            traceback.print_exc()
            self.current_settings = self.default_settings.copy()
            self.recent_projects = []
            
    def apply_current_line_highlight(self, color):
        """Apply current line highlight color to the editor."""
        try:
            if hasattr(self, 'editor'):
                # Store the color for the editor to use
                self.current_line_color_value = color
                
                # Trigger immediate re-highlighting of current line
                self.editor.highlightCurrentLine()
                print(f"Applied current line highlight: {color}")
        except Exception as e:
            print(f"Error applying current line highlight: {e}")

    def save_settings(self):
        """Save current settings to the config file."""
        try:
            # Ensure config directory exists
            os.makedirs(self.settings_dir, exist_ok=True)
            
            # Collect current settings from UI if settings dialog was opened
            if hasattr(self, 'shortcut_inputs'):
                for key, widget in self.shortcut_inputs.items():
                    self.current_settings['shortcuts'][key] = widget.text()
                    
            if hasattr(self, 'font_family_combo'):
                self.current_settings['text']['font_family'] = self.font_family_combo.currentText()
                
            if hasattr(self, 'font_size_spin'):
                self.current_settings['text']['font_size'] = self.font_size_spin.value()
                
            if hasattr(self, 'tab_size_spin'):
                self.current_settings['text']['tab_size'] = self.tab_size_spin.value()
                
            if hasattr(self, 'word_wrap'):
                self.current_settings['text']['word_wrap'] = self.word_wrap.isChecked()
                
            if hasattr(self, 'show_line_numbers'):
                self.current_settings['text']['show_line_numbers'] = self.show_line_numbers.isChecked()
                
            # Color settings
            if hasattr(self, 'ide_bg_color'):
                self.current_settings['theme']['ide_background'] = self.ide_bg_color.text()
            if hasattr(self, 'editor_bg_color'):
                self.current_settings['theme']['editor_background'] = self.editor_bg_color.text()
            if hasattr(self, 'text_color'):
                self.current_settings['theme']['text_color'] = self.text_color.text()
            if hasattr(self, 'line_bg_color'):
                self.current_settings['theme']['line_number_background'] = self.line_bg_color.text()
            if hasattr(self, 'bp_color'):
                self.current_settings['theme']['breakpoint_color'] = self.bp_color.text()
            if hasattr(self, 'bp_line_bg_color'):
                self.current_settings['theme']['breakpoint_line_background'] = self.bp_line_bg_color.text()
            if hasattr(self, 'selection_bg_color'):
                self.current_settings['theme']['selection_background'] = self.selection_bg_color.text()
            if hasattr(self, 'current_line_color'):
                self.current_settings['theme']['current_line_highlight'] = self.current_line_color.text()
            if hasattr(self, 'ui_text_color'):
                self.current_settings['theme']['ui_text_color'] = self.ui_text_color.text()
            if hasattr(self, 'panel_bg_color'):
                self.current_settings['theme']['panel_background'] = self.panel_bg_color.text()
            
            # Add recent projects to settings
            if hasattr(self, 'recent_projects'):
                self.current_settings['recent_projects'] = self.recent_projects
            
            # Write to file
            with open(self.settings_file, 'w') as f:
                json.dump(self.current_settings, f, indent=4)
                
        except Exception as e:
            print(f"Error saving settings: {e}")
            
    def add_to_recent_projects(self, project_path):
        """Add a project to the recent projects list."""
        if not hasattr(self, 'recent_projects'):
            self.recent_projects = []
            
        # Remove if already in list
        if project_path in self.recent_projects:
            self.recent_projects.remove(project_path)
            
        # Add to front
        self.recent_projects.insert(0, project_path)
        
        # Keep only last 10
        self.recent_projects = self.recent_projects[:10]
        
        # Save settings
        self.save_settings()

    def apply_loaded_settings(self):
        """Apply the loaded settings to the IDE components."""
        try:
            print(f"Applying loaded settings: {self.current_settings}")
            
            # Apply font settings
            font_family = self.current_settings['text']['font_family']
            font_size = self.current_settings['text']['font_size']
            font = QFont(font_family, font_size)
            if hasattr(self, 'editor'):
                self.editor.setFont(font)
                print(f"Applied font: {font_family} {font_size}")
            
            # Apply colors using stylesheet for better compatibility
            if hasattr(self, 'editor'):
                text_color = self.current_settings['theme']['text_color']
                editor_bg = self.current_settings['theme']['editor_background']
                selection_bg = self.current_settings['theme'].get('selection_background', '#3399ff')
                current_line_bg = self.current_settings['theme'].get('current_line_highlight', '#ffffcc')
                
                # Use stylesheet to set colors
                editor_style = f"""
                QPlainTextEdit {{
                    color: {text_color};
                    background-color: {editor_bg};
                    selection-background-color: {selection_bg};
                    border: none;
                }}
                """
                self.editor.setStyleSheet(editor_style)
                print(f"Applied editor colors - Text: {text_color}, Background: {editor_bg}, Selection: {selection_bg}")
                
                # Apply current line highlight
                self.apply_current_line_highlight(current_line_bg)
                
                # Word wrap
                if self.current_settings['text']['word_wrap']:
                    self.editor.setLineWrapMode(QPlainTextEdit.WidgetWidth)
                else:
                    self.editor.setLineWrapMode(QPlainTextEdit.NoWrap)
                print(f"Applied word wrap: {self.current_settings['text']['word_wrap']}")
            
            # Apply IDE background and UI colors
            ide_bg = self.current_settings['theme']['ide_background']
            ui_text_color = self.current_settings['theme'].get('ui_text_color', '#000000')
            panel_bg = self.current_settings['theme'].get('panel_background', '#f0f0f0')
            
            # Comprehensive stylesheet for all UI elements
            self.setStyleSheet(f"""
                QMainWindow {{ 
                    background-color: {ide_bg}; 
                }}
                QMainWindow QPlainTextEdit {{
                    color: {text_color};
                    background-color: {editor_bg};
                    selection-background-color: {selection_bg};
                    border: none;
                }}
                QListWidget {{
                    background-color: {panel_bg};
                    color: {ui_text_color};
                    border: 1px solid gray;
                }}
                QListWidget::item {{
                    color: {ui_text_color};
                    padding: 2px;
                }}
                QListWidget::item:hover {{
                    background-color: {selection_bg};
                }}
                QTextEdit {{
                    background-color: {panel_bg};
                    color: {ui_text_color};
                    border: 1px solid gray;
                }}
                QLabel {{
                    color: {ui_text_color};
                }}
                QPushButton {{
                    background-color: white;
                    color: black;
                    border: none;
                    padding: 6px 12px;
                    border-radius: 3px;
                }}
                QPushButton:hover {{
                    background-color: {selection_bg};
                    color: white;
                }}
                QPushButton:pressed {{
                    background-color: {selection_bg};
                }}
                QMenuBar {{
                    background-color: {panel_bg};
                }}
                QMenuBar::item {{
                    background-color: {panel_bg};
                }}
                QMenuBar::item:selected {{
                    background-color: {selection_bg};
                }}
                QMenu {{
                    background-color: {panel_bg};
                    border: 1px solid gray;
                }}
                QMenu::item:selected {{
                    background-color: {selection_bg};
                }}
                QToolBar {{
                    background-color: {panel_bg};
                    border: none;
                    spacing: 2px;
                }}
                QToolBar QPushButton {{
                    background-color: white;
                    color: black;
                    border: none;
                    padding: 8px 12px;
                    margin: 2px;
                    border-radius: 3px;
                }}
                QToolBar QPushButton:hover {{
                    background-color: {selection_bg};
                    color: white;
                }}
                QStatusBar {{
                    background-color: {panel_bg};
                    color: {ui_text_color};
                }}
            """)
            print(f"Applied IDE background: {ide_bg}, UI text: {ui_text_color}, Panel background: {panel_bg}")
            
            # Force update
            if hasattr(self, 'editor'):
                self.editor.update()
                if hasattr(self.editor, 'lineNumberArea'):
                    self.editor.lineNumberArea.update()
            
        except Exception as e:
            print(f"Error applying loaded settings: {e}")
            import traceback
            traceback.print_exc()

    def apply_settings_and_close(self, dialog):
        """Apply settings and close the dialog."""
        self.apply_settings(dialog)
        dialog.accept()

    def setup_lsp(self):
        """Initialize LSP-related attributes and start the language server."""
        self.lsp_process = None
        self.lsp_stdin = None
        self.lsp_stdout = None
        
        # Disable automatic diagnostics for now to reduce timer usage
        # self.diagnostics_timer = QTimer()
        # self.diagnostics_timer.timeout.connect(self.send_diagnostics)
        # self.diagnostics_timer.start(5000)  # Increased to 5 seconds to reduce load
        
        # Single timer for breakpoint highlighting to avoid creating multiple timers
        self.highlight_timer = QTimer()
        self.highlight_timer.setSingleShot(True)
        self.highlight_timer.timeout.connect(self.highlight_breakpoints)
        
        self.start_lsp()

    def start_lsp(self):
        try:
            # Use VG_SERVER_EXECUTABLE_PATH environment variable to locate vg-server.exe
            vg_server_executable = os.getenv("VG_SERVER_EXECUTABLE_PATH", "vg-server.exe")
            self.lsp_process = subprocess.Popen(
                [vg_server_executable],
                stdin=subprocess.PIPE,
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                bufsize=0
            )
            self.lsp_stdin = self.lsp_process.stdin
            self.lsp_stdout = self.lsp_process.stdout
            self.status_bar.showMessage("Language server started")
        except Exception as e:
            self.status_bar.showMessage(f"Failed to start language server: {e}")

    def send_diagnostics(self):
        if not self.lsp_stdin:
            return
            
        # Minimal LSP: send didOpen and request diagnostics
        text = self.editor.toPlainText()
        # Compose a didOpen notification (JSON-RPC)
        did_open = {
            "jsonrpc": "2.0",
            "method": "textDocument/didOpen",
            "params": {
                "textDocument": {
                    "uri": "file:///dummy.vg",
                    "languageId": "vg",
                    "version": 1,
                    "text": text
                }
            }
        }
        msg = json.dumps(did_open)
        content_length = len(msg.encode("utf-8"))
        header = f"Content-Length: {content_length}\r\n\r\n"
        try:
            self.lsp_stdin.write(header.encode("utf-8"))
            self.lsp_stdin.write(msg.encode("utf-8"))
            self.lsp_stdin.flush()
        except Exception:
            pass

    # File operations
    def new_file(self):
        self.editor.clear()
        self.current_file = None
        self.setWindowTitle("VG Language IDE - New File")
        self.status_bar.showMessage("New file created")

    def open_file(self):
        file_path, _ = QFileDialog.getOpenFileName(self, "Open File", "", "VG Files (*.vg);;All Files (*)")
        if file_path:
            try:
                with open(file_path, 'r', encoding='utf-8') as file:
                    content = file.read()
                    self.editor.setPlainText(content)
                    self.current_file = file_path
                    self.setWindowTitle(f"VG Language IDE - {os.path.basename(file_path)}")
                    self.status_bar.showMessage(f"Opened: {file_path}")
                    # update explorer to show files in this directory
                    self.update_file_explorer()
            except Exception as e:
                QMessageBox.critical(self, "Error", f"Could not open file: {e}")

    def save_file(self):
        if self.current_file:
            try:
                with open(self.current_file, 'w', encoding='utf-8') as file:
                    file.write(self.editor.toPlainText())
                    self.status_bar.showMessage(f"Saved: {self.current_file}")
                    # refresh explorer in case new file created/changed
                    self.update_file_explorer()
            except Exception as e:
                QMessageBox.critical(self, "Error", f"Could not save file: {e}")
        else:
            self.save_as_file()

    def save_as_file(self):
        file_path, _ = QFileDialog.getSaveFileName(self, "Save File", "", "VG Files (*.vg);;All Files (*)")
        if file_path:
            try:
                with open(file_path, 'w', encoding='utf-8') as file:
                    file.write(self.editor.toPlainText())
                    self.current_file = file_path
                    self.setWindowTitle(f"VG Language IDE - {os.path.basename(file_path)}")
                    self.status_bar.showMessage(f"Saved: {file_path}")
                    # update explorer after save as
                    self.update_file_explorer()
            except Exception as e:
                QMessageBox.critical(self, "Error", f"Could not save file: {e}")

    # Run operations
    def run_code(self):
        if not self.current_file:
            QMessageBox.warning(self, "Warning", "Please save the file before running")
            return

        self.console.clear()
        self.status_bar.showMessage("Running...")

        try:
            # Check if the program uses IO to determine if console should be interactive
            with open(self.current_file, 'r') as file:
                file_content = file.read()
            
            # Check for IO usage patterns
            uses_io = any(pattern in file_content for pattern in [
                'IO.readLine',
                'IO.read',
                'Input',
                'input',
                'readLine',
                'Scanner',
                'BufferedReader'
            ])
            
            # Make console interactive if program uses IO
            if uses_io:
                self.console.setReadOnly(False)
                self.console.setPlaceholderText("Program is waiting for input. Type here and press Enter...")
                self.console.append("🟢 Interactive console enabled - Program can receive input")
            else:
                self.console.setReadOnly(True)
                self.console.setPlaceholderText("")

            # Use VG_EXECUTABLE_PATH environment variable to locate vg.exe
            vg_executable = os.getenv("VG_EXECUTABLE_PATH", "vg.exe")
            print(f"IDE: VG_EXECUTABLE_PATH is set to: {vg_executable}")

            # Resolve full path if vg_executable is a relative path
            if not os.path.isabs(vg_executable):
                vg_executable = os.path.abspath(vg_executable)
                print(f"IDE: Resolved VG_EXECUTABLE_PATH to absolute path: {vg_executable}")

            if not os.path.exists(vg_executable):
                print(f"IDE: File does not exist at path: {vg_executable}")
                self.console.append(f"VG Language executable not found at: {vg_executable}")
                self.status_bar.showMessage("Executable file not found")
                return

            self.console.append("Using VG Language executable...")

            # Run the VG interpreter using the executable
            abs_file_path = os.path.abspath(self.current_file)

            cmd = [vg_executable, abs_file_path]
            print(f"IDE: Running command: {' '.join(cmd)}")

            # Start process with stdin support for interactive programs
            self.current_process = subprocess.Popen(
                cmd,
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,  # Separate stderr to capture errors
                stdin=subprocess.PIPE,
                universal_newlines=False,  # Use binary mode
                bufsize=0  # Unbuffered
            )
            
            print(f"IDE: Process started with PID: {self.current_process.pid}")

            self.console.append("\n--- Program Output ---")
            
            # Handle program output in real-time
            self.read_program_output()
            
        except Exception as e:
            print(f"IDE: Error occurred: {e}")
            self.console.setPlainText(f"Error running code: {e}")
            self.status_bar.showMessage("Execution failed")
            
    def read_program_output(self):
        """Read program output in real-time while allowing input."""
        if hasattr(self, 'current_process') and self.current_process:
            try:
                import threading
                import queue
                
                # Create a queue to safely pass data between threads
                self.output_queue = queue.Queue()
                
                def read_stdout():
                    try:
                        while True:
                            # Read a chunk of data
                            data = self.current_process.stdout.read(1024)
                            if not data:
                                break
                            # Decode and put in queue
                            try:
                                text = data.decode('utf-8', errors='replace')
                                self.output_queue.put(('output', text))
                            except Exception as e:
                                self.output_queue.put(('error', f"Decode error: {e}"))
                    except Exception as e:
                        self.output_queue.put(('error', f"Read error: {e}"))
                
                def read_stderr():
                    try:
                        while True:
                            # Read stderr if available (though we're redirecting to stdout)
                            data = self.current_process.stderr.read(1024) if self.current_process.stderr else b''
                            if not data:
                                break
                            try:
                                text = data.decode('utf-8', errors='replace')
                                self.output_queue.put(('error', text))
                            except Exception as e:
                                self.output_queue.put(('error', f"Stderr decode error: {e}"))
                    except Exception as e:
                        self.output_queue.put(('error', f"Stderr read error: {e}"))
                
                # Start output reading threads
                self.stdout_thread = threading.Thread(target=read_stdout, daemon=True)
                self.stdout_thread.start()
                
                # Create a timer to check the queue periodically
                self.output_timer = QTimer()
                self.output_timer.timeout.connect(self.process_output_queue)
                self.output_timer.start(50)  # Check every 50ms
                
                # Check process status periodically
                self.check_process_timer = QTimer()
                self.check_process_timer.timeout.connect(self.check_process_status)
                self.check_process_timer.start(100)  # Check every 100ms
                
            except Exception as e:
                print(f"IDE: Error setting up output reading: {e}")
                self.console.append(f"Error setting up output reading: {e}")
    
    def process_output_queue(self):
        """Process output from the queue"""
        try:
            while not self.output_queue.empty():
                msg_type, text = self.output_queue.get_nowait()
                if msg_type == 'output':
                    self.append_program_text(text)
                elif msg_type == 'error':
                    self.append_program_text(f"[ERROR] {text}")
        except queue.Empty:
            pass
        except Exception as e:
            print(f"Error processing output queue: {e}")
    
    def append_program_text(self, text):
        """Safely append program output text to console"""
        try:
            # Move cursor to end and insert text
            cursor = self.console.textCursor()
            cursor.movePosition(cursor.End)
            self.console.setTextCursor(cursor)
            self.console.insertPlainText(text)
            # Auto-scroll to bottom
            scrollbar = self.console.verticalScrollBar()
            scrollbar.setValue(scrollbar.maximum())
        except Exception as e:
            print(f"Error appending text: {e}")
    
    def append_program_char(self, char):
        """Safely append a character to console output"""
        try:
            # Move cursor to end and insert character
            cursor = self.console.textCursor()
            cursor.movePosition(cursor.End)
            self.console.setTextCursor(cursor)
            self.console.insertPlainText(char)
        except Exception as e:
            print(f"Error appending character: {e}")
    
    def flush_console_output(self):
        """Flush console output and scroll to bottom"""
        try:
            # Auto-scroll to bottom
            scrollbar = self.console.verticalScrollBar()
            scrollbar.setValue(scrollbar.maximum())
        except Exception as e:
            print(f"Error flushing output: {e}")
    
    def append_program_output(self, text):
        """Safely append program output to console"""
        try:
            self.console.append(text)
            # Auto-scroll to bottom
            scrollbar = self.console.verticalScrollBar()
            scrollbar.setValue(scrollbar.maximum())
        except Exception as e:
            print(f"Error appending output: {e}")
                
    def check_process_status(self):
        """Check if the running process has finished."""
        try:
            if hasattr(self, 'current_process') and self.current_process:
                if self.current_process.poll() is not None:
                    # Process has finished
                    self.check_process_timer.stop()
                    return_code = self.current_process.returncode
                    self.console.append(f"\n--- Program finished (exit code: {return_code}) ---")
                    # Make console read-only again when program finishes
                    self.console.setReadOnly(True)
                    self.console.setPlaceholderText("")
                    self.status_bar.showMessage("Execution completed")
                    self.current_process = None
        except Exception as e:
            print(f"IDE: Error checking process status: {e}")
            if hasattr(self, 'check_process_timer'):
                self.check_process_timer.stop()

    # Debug operations
    def start_debug(self):
        if not self.current_file:
            QMessageBox.warning(self, "Warning", "Please save the file before debugging")
            return
            
        if self.is_debugging:
            return
            
        self.console.clear()
        self.status_bar.showMessage("Starting debug session...")
        
        self.debug_thread = DebugThread(self.current_file, self.breakpoints.copy())
        self.debug_thread.output_received.connect(self.on_debug_output)
        self.debug_thread.debug_stopped.connect(self.on_debug_stopped)
        self.debug_thread.start()
        
        self.is_debugging = True
        self.enable_debug_controls(True)

    def debug_continue(self):
        if self.debug_thread and self.debug_thread.process:
            self.debug_thread.process.stdin.write("continue\n")
            self.debug_thread.process.stdin.flush()

    def debug_step_into(self):
        if self.debug_thread and self.debug_thread.process:
            self.debug_thread.process.stdin.write("step_into\n")
            self.debug_thread.process.stdin.flush()

    def debug_step_over(self):
        if self.debug_thread and self.debug_thread.process:
            self.debug_thread.process.stdin.write("step_over\n")
            self.debug_thread.process.stdin.flush()

    def debug_step_out(self):
        if self.debug_thread and self.debug_thread.process:
            self.debug_thread.process.stdin.write("step_out\n")
            self.debug_thread.process.stdin.flush()

    def debug_variables(self):
        if self.debug_thread and self.debug_thread.process:
            self.debug_thread.process.stdin.write("variables\n")
            self.debug_thread.process.stdin.flush()

    def stop_debug(self):
        if self.debug_thread:
            self.debug_thread.stop_debug()
            self.is_debugging = False
            self.enable_debug_controls(False)
            self.status_bar.showMessage("Debug session stopped")

    def on_debug_output(self, output):
        self.console.append(output)
        
        # Debug: print what we're parsing
        if "DEBUG_" in output:
            print(f"IDE: Parsing debug output: {output}")
        
        # Parse structured debug output
        if output == "DEBUG_VARIABLES_START":
            self.parsing_variables = True
            self.current_variables = {}
            print("IDE: Started parsing variables")
        elif output == "DEBUG_VARIABLES_END":
            self.parsing_variables = False
            self.update_variables_panel()
            print(f"IDE: Finished parsing variables: {self.current_variables}")
        elif output == "DEBUG_FUNCTIONS_START":
            self.parsing_functions = True
            self.current_functions = []
            print("IDE: Started parsing functions")
        elif output == "DEBUG_FUNCTIONS_END":
            self.parsing_functions = False
            self.update_variables_panel()
            print(f"IDE: Finished parsing functions: {self.current_functions}")
        elif self.parsing_variables and "=" in output:
            # Parse variable: name=value
            name, value = output.split("=", 1)
            self.current_variables[name] = value
            print(f"IDE: Parsed variable: {name} = {value}")
        elif self.parsing_functions and output.strip():
            # Parse function name
            self.current_functions.append(output.strip())
            print(f"IDE: Parsed function: {output.strip()}")
        elif "Debug: Paused at line" in output:
            self.debug_paused = True
            print("IDE: Debug paused, waiting for user input")
            # Don't auto-continue - wait for user to use debug controls

    def update_variables_panel(self):
        # Update the variables panel with current variables and functions
        self.variables_list.clear()
        
        # Add variables
        if self.current_variables:
            self.variables_list.addItem("=== VARIABLES ===")
            for name, value in self.current_variables.items():
                self.variables_list.addItem(f"{name} = {value}")
        
        # Add functions
        if self.current_functions:
            self.variables_list.addItem("=== FUNCTIONS ===")
            for func_name in self.current_functions:
                self.variables_list.addItem(f"{func_name}()")
        
        if not self.current_variables and not self.current_functions:
            self.variables_list.addItem("No variables or functions defined")

    def continue_debug(self):
        # Continue debugging execution
        if self.debug_thread and self.debug_thread.process:
            self.debug_thread.process.stdin.write("c\n")
            self.debug_thread.process.stdin.flush()

    def step_into_debug(self):
        # Step into next line
        if self.debug_thread and self.debug_thread.process:
            self.debug_thread.process.stdin.write("s\n")
            self.debug_thread.process.stdin.flush()

    def step_over_debug(self):
        # Step over function calls
        if self.debug_thread and self.debug_thread.process:
            self.debug_thread.process.stdin.write("so\n")
            self.debug_thread.process.stdin.flush()

    def step_out_debug(self):
        # Step out of current function
        if self.debug_thread and self.debug_thread.process:
            self.debug_thread.process.stdin.write("sout\n")
            self.debug_thread.process.stdin.flush()

    def show_variables(self):
        # Show current variables and functions
        if self.debug_thread and self.debug_thread.process:
            self.debug_thread.process.stdin.write("v\n")
            self.debug_thread.process.stdin.flush()

    def on_debug_stopped(self):
        self.is_debugging = False
        self.enable_debug_controls(False)
        self.status_bar.showMessage("Debug session ended")

    def enable_debug_controls(self, enabled):
        # Widgets: some may not exist depending on UI state; guard attribute access
        for attr in ('continue_button', 'step_into_button', 'step_over_button', 'step_out_button', 'stop_button', 'variables_button'):
            if hasattr(self, attr):
                try:
                    getattr(self, attr).setEnabled(enabled)
                except Exception:
                    pass
            # Toggle toolbar buttons if they exist (defensive)
            for btn_attr in ('run_button', 'debug_button', 'breakpoint_button'):
                if hasattr(self, btn_attr):
                    try:
                        getattr(self, btn_attr).setEnabled(not enabled if btn_attr == 'run_button' else True)
                    except Exception:
                        pass

            # Toggle QAction menu items if they exist
            for act_name, want_enabled in (('continue_action', enabled), ('step_into_action', enabled),
                                          ('step_over_action', enabled), ('step_out_action', enabled),
                                          ('stop_debug_action', enabled), ('vars_action', enabled),
                                          ('start_debug_action', not enabled), ('run_action', not enabled)):
                act = getattr(self, act_name, None)
                if isinstance(act, QAction):
                    try:
                        act.setEnabled(want_enabled)
                    except Exception:
                        pass
        # Buttons created in toolbar
        if hasattr(self, 'debug_button'):
            try:
                self.debug_button.setEnabled(not enabled)
            except Exception:
                pass
        if hasattr(self, 'run_button'):
            try:
                self.run_button.setEnabled(not enabled)
            except Exception:
                pass

        # Also toggle menu actions if they exist (so keyboard shortcuts reflect state)
        for act_name, desired in (('continue_action', enabled), ('step_into_action', enabled), ('step_over_action', enabled), ('step_out_action', enabled), ('stop_debug_action', enabled), ('vars_action', enabled), ('start_debug_action', not enabled), ('run_action', not enabled)):
            if hasattr(self, act_name):
                try:
                    getattr(self, act_name).setEnabled(desired)
                except Exception:
                    pass

    def on_text_changed(self):
        # Use the existing timer instead of creating new ones
        if hasattr(self, 'highlight_timer'):
            self.highlight_timer.stop()  # Stop any pending highlight
            self.highlight_timer.start(200)  # Start with 200ms delay

    # Breakpoint operations
    def toggle_breakpoint(self):
        cursor = self.editor.textCursor()
        line_number = cursor.blockNumber() + 1
        
        if line_number in self.breakpoints:
            self.breakpoints.remove(line_number)
            self.breakpoints_list.takeItem(
                next(i for i in range(self.breakpoints_list.count()) 
                     if self.breakpoints_list.item(i).text() == f"Line {line_number}")
            )
            # Send remove breakpoint command to running debug process
            if self.is_debugging and self.debug_thread and self.debug_thread.process:
                try:
                    command = f"removebreak {line_number}\n"
                    self.debug_thread.process.stdin.write(command)
                    self.debug_thread.process.stdin.flush()
                    print(f"IDE: Sent removebreak command for line {line_number}")
                except Exception as e:
                    print(f"IDE: Failed to send removebreak command: {e}")
        else:
            self.breakpoints.add(line_number)
            self.breakpoints_list.addItem(f"Line {line_number}")
            # Send add breakpoint command to running debug process
            if self.is_debugging and self.debug_thread and self.debug_thread.process:
                try:
                    command = f"addbreak {line_number}\n"
                    self.debug_thread.process.stdin.write(command)
                    self.debug_thread.process.stdin.flush()
                    print(f"IDE: Sent addbreak command for line {line_number}")
                except Exception as e:
                    print(f"IDE: Failed to send addbreak command: {e}")
        
        # Directly call highlight instead of using timer
        self.highlight_breakpoints()
        
        # Update visual breakpoint dots
        self.editor.refresh_breakpoints()

    def highlight_breakpoints(self):
        # More efficient breakpoint highlighting for QPlainTextEdit
        if not self.breakpoints:
            return
            
        # Get the document
        document = self.editor.document()
        
        # Use customizable breakpoint line background color
        bp_line_color = QColor(255, 200, 200)  # Default light red
        if hasattr(self, 'bp_line_bg_color'):
            try:
                color_text = self.bp_line_bg_color.text()
                if color_text.startswith('#'):
                    bp_line_color = QColor(color_text)
            except:
                pass
        
        # Create selections for each breakpoint
        selections = []
        for line_number in self.breakpoints:
            if line_number <= document.blockCount():
                selection = QTextEdit.ExtraSelection()
                selection.format.setBackground(bp_line_color)
                selection.format.setProperty(QTextFormat.FullWidthSelection, True)
                
                cursor = QTextCursor(document.findBlockByLineNumber(line_number - 1))
                selection.cursor = cursor
                selections.append(selection)
        
        # Get current line selection and add breakpoint selections
        current_selections = self.editor.extraSelections()
        
        # Filter out old breakpoint selections and keep current line highlight
        non_breakpoint_selections = []
        for sel in current_selections:
            # Keep selections that are not the breakpoint background color
            if sel.format.background().color() != bp_line_color:
                non_breakpoint_selections.append(sel)
        
        # Combine current line highlight with breakpoint highlights
        all_selections = non_breakpoint_selections + selections
        self.editor.setExtraSelections(all_selections)

    def show_package_install_dialog(self):
        ensure_qapplication()
        from PyQt5.QtWidgets import QDialog, QVBoxLayout, QListWidget, QListWidgetItem, QPushButton, QLabel, QHBoxLayout, QMessageBox
        dialog = QDialog(self)
        dialog.setWindowTitle("Install Packages")
        layout = QVBoxLayout(dialog)
        note = QLabel("Fetching available packages...")
        layout.addWidget(note)
        pkg_list = QListWidget(dialog)
        layout.addWidget(pkg_list)
        btn_layout = QHBoxLayout()
        install_btn = QPushButton("Install", dialog)
        cancel_btn = QPushButton("Cancel", dialog)
        btn_layout.addWidget(cancel_btn)
        btn_layout.addWidget(install_btn)
        layout.addLayout(btn_layout)

        # helper: normalize package output lines
        def normalize_line(line):
            if not line:
                return None
            raw = line.strip()
            lower = raw.lower()
            # ignore obvious non-package lines
            ignore_keywords = ['package not found', 'error', 'installed packages', 'available packages', 'installing', 'removing', 'fetching', 'no packages found']
            if any(k in lower for k in ignore_keywords):
                return None
            # ignore lines that look like filesystem paths
            if re.search(r'[A-Za-z]:\\\\|/', raw):
                return None
            # ignore headings/lines that end with ':':
            if raw.endswith(':'):
                return None
            # remove common bullet prefixes like '- ', '• ', '* ', numbering like '1) '
            raw = re.sub(r'^[\-\u2022\*\s\d\)]+', '', raw)
            # remove surrounding quotes
            raw = raw.strip('"\'')
            raw = raw.strip()
            return raw if raw else None

        # Fetch available packages using vgpkg available
        try:
            proc = subprocess.Popen(["vgpkg", "available"], stdout=subprocess.PIPE, stderr=subprocess.STDOUT, universal_newlines=True)
            out, _ = proc.communicate(timeout=15)
            raw_lines = [line for line in out.splitlines()]
            packages = []
            for line in raw_lines:
                name = normalize_line(line)
                if name:
                    packages.append(name)
        except Exception as e:
            QMessageBox.critical(self, "Error", f"Failed to run 'vgpkg available': {e}")
            return

        if not packages:
            note.setText("No available packages found.")
        else:
            note.setText("Select packages to install:")
            seen = set()
            for p in packages:
                # avoid duplicates
                display_name = p
                # if the available output lists filenames, prefer stripping extension for display
                cmd_name = p
                if p.lower().endswith('.vglib'):
                    display_name = p
                    cmd_name = p[:-7]
                if display_name in seen:
                    continue
                seen.add(display_name)
                item = QListWidgetItem(display_name)
                item.setFlags(item.flags() | Qt.ItemIsUserCheckable)
                item.setCheckState(Qt.Unchecked)
                # store actual command name used by vgpkg (without .vglib)
                item.setData(Qt.UserRole, cmd_name)
                pkg_list.addItem(item)

        def do_install():
            # get the actual QListWidgetItem objects for selected entries
            selected_items = [pkg_list.item(i) for i in range(pkg_list.count()) if pkg_list.item(i).checkState() == Qt.Checked]
            if not selected_items:
                QMessageBox.information(dialog, "No selection", "No packages selected.")
                return
            def _extract_cmd(it):
                # support both QListWidgetItem and legacy string entries
                try:
                    # QListWidgetItem normally
                    return it.data(Qt.UserRole) or it.text()
                except Exception:
                    # fallback if it's a simple string
                    return str(it)

            for item in selected_items:
                pkg_cmd = _extract_cmd(item)
                self.console.append(f"Installing {pkg_cmd}...")
                try:
                    proc = subprocess.Popen(['vgpkg', 'install', pkg_cmd], stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True)
                    out, _ = proc.communicate()
                    self.console.append(out)
                except Exception as e:
                    self.console.append(f"Failed to run vgpkg install: {e}")
                # attempt to reconcile any truncated filenames created by the package manager
                self._fix_installed_filename(pkg_cmd)
            QMessageBox.information(self, "Install complete", "Selected packages processed.")
            dialog.accept()

        install_btn.clicked.connect(do_install)
        cancel_btn.clicked.connect(dialog.reject)
        dialog.exec_()

    def update_file_explorer(self):
        # Determine directory to list: if current file saved, use its folder; else use cwd
        try:
            if self.current_file:
                dir_to_list = os.path.dirname(self.current_file)
            else:
                dir_to_list = os.getcwd()
            self.file_explorer.clear()
            files = sorted(os.listdir(dir_to_list))
            for fn in files:
                # only show files (not directories) and include common extensions
                full = os.path.join(dir_to_list, fn)
                if os.path.isfile(full):
                    self.file_explorer.addItem(fn)
        except Exception as e:
            # don't crash the UI if listing fails
            self.console.append(f"File explorer error: {e}")

    def _open_file_from_explorer(self, item):
        # open the selected file from the explorer
        try:
            if not item:
                return
            fn = item.text()
            if self.current_file:
                dir_to_list = os.path.dirname(self.current_file)
            else:
                dir_to_list = os.getcwd()
            path = os.path.join(dir_to_list, fn)
            if os.path.isfile(path):
                with open(path, 'r', encoding='utf-8') as f:
                    content = f.read()
                    self.editor.setPlainText(content)
                    self.current_file = path
                    self.setWindowTitle(f"VG Language IDE - {os.path.basename(path)}")
                    self.status_bar.showMessage(f"Opened: {path}")
        except Exception as e:
            QMessageBox.critical(self, "Error", f"Could not open file: {e}")

    def show_package_remove_dialog(self):
        ensure_qapplication()
        from PyQt5.QtWidgets import QDialog, QVBoxLayout, QListWidget, QListWidgetItem, QPushButton, QLabel, QHBoxLayout, QMessageBox
        dialog = QDialog(self)
        dialog.setWindowTitle("Remove Packages")
        layout = QVBoxLayout(dialog)
        note = QLabel("Fetching installed packages...")
        layout.addWidget(note)
        pkg_list = QListWidget(dialog)
        layout.addWidget(pkg_list)
        btn_layout = QHBoxLayout()
        remove_btn = QPushButton("Remove", dialog)
        cancel_btn = QPushButton("Cancel", dialog)
        btn_layout.addWidget(cancel_btn)
        btn_layout.addWidget(remove_btn)
        layout.addLayout(btn_layout)

        def normalize_line(line):
            if not line:
                return None
            raw = line.strip()
            lower = raw.lower()
            ignore_keywords = ['package not found', 'error', 'installed packages', 'available packages', 'installing', 'removing', 'fetching', 'no packages found']
            if any(k in lower for k in ignore_keywords):
                return None
            if re.search(r'[A-Za-z]:\\\\|/', raw):
                return None
            if raw.endswith(':'):
                return None
            raw = re.sub(r'^[\-\u2022\*\s\d\)]+', '', raw)
            raw = raw.strip('"\'')
            raw = raw.strip()
            return raw if raw else None

        # Fetch installed packages using vgpkg list
        try:
            proc = subprocess.Popen(["vgpkg", "list"], stdout=subprocess.PIPE, stderr=subprocess.STDOUT, universal_newlines=True)
            out, _ = proc.communicate(timeout=15)
            raw_lines = [line for line in out.splitlines()]
            # log raw output to output pane for debugging
            self.console.append("vgpkg list raw output:")
            for rl in raw_lines:
                self.console.append(rl)
            packages = []
            for line in raw_lines:
                name = normalize_line(line)
                if name:
                    packages.append((name, line))
        except Exception as e:
            QMessageBox.critical(self, "Error", f"Failed to run 'vgpkg list': {e}")
            return

        if not packages:
            note.setText("No installed packages found.")
        else:
            note.setText("Select packages to remove (you can edit names before removing):")
            seen = set()
            for p, raw in packages:
                display_name = p
                cmd_name = p
                if p.lower().endswith('.vglib'):
                    display_name = p
                    cmd_name = p[:-7]
                if display_name in seen:
                    continue
                seen.add(display_name)
                item = QListWidgetItem(display_name)
                item.setFlags(item.flags() | Qt.ItemIsUserCheckable)
                item.setCheckState(Qt.Unchecked)
                # store actual command name used by vgpkg (without .vglib)
                item.setData(Qt.UserRole, cmd_name)
                # store raw original line for reference
                item.setData(Qt.UserRole + 1, raw)
                pkg_list.addItem(item)

    # (Removed: Edit Selected button and inline edit handler)

        def do_remove():
            # get the actual QListWidgetItem objects for selected entries
            selected_items = [pkg_list.item(i) for i in range(pkg_list.count()) if pkg_list.item(i).checkState() == Qt.Checked]
            if not selected_items:
                QMessageBox.information(dialog, "No selection", "No packages selected.")
                return
            def _extract_cmd(it):
                try:
                    return it.data(Qt.UserRole) or it.text()
                except Exception:
                    return str(it)

            for item in selected_items:
                pkg_cmd = _extract_cmd(item)
                # ensure any truncated installed filename is reconciled back to the canonical name
                self._fix_installed_filename(pkg_cmd)
                self.console.append(f"Removing {pkg_cmd}...")
                try:
                    proc = subprocess.Popen(['vgpkg', 'remove', pkg_cmd], stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True)
                    out, _ = proc.communicate()
                    self.console.append(out)
                except Exception as e:
                    self.console.append(f"Failed to run vgpkg remove: {e}")
            QMessageBox.information(self, "Remove complete", "Selected packages processed.")
            dialog.accept()

        remove_btn.clicked.connect(do_remove)
        cancel_btn.clicked.connect(dialog.reject)
        dialog.exec_()

    def _ensure_packages_dir(self):
        packages_dir = os.path.join(os.path.dirname(__file__), 'packages')
        try:
            os.makedirs(packages_dir, exist_ok=True)
        except Exception:
            pass
        return packages_dir

    def _fix_installed_filename(self, cmd_name):
        # After install, try to find and rename the installed file to the canonical name.
        packages_dir = self._ensure_packages_dir()
        expected = f"{cmd_name}.vglib"
        try:
            files = os.listdir(packages_dir)
        except Exception as e:
            self.console.append(f"Could not list packages directory: {e}")
            return
        # try exact match first
        if expected in files:
            return
        # find close matches
        match = difflib.get_close_matches(expected, files, n=1, cutoff=0.6)
        if match:
            src = os.path.join(packages_dir, match[0])
            dst = os.path.join(packages_dir, expected)
            try:
                os.rename(src, dst)
                self.console.append(f"Renamed installed file '{match[0]}' to '{expected}'")
            except Exception as e:
                self.console.append(f"Failed to rename '{match[0]}' -> '{expected}': {e}")
        else:
            # also try matching by starting substring (handle truncated names)
            lc_expected = expected.lower()
            for f in files:
                if f.lower().startswith(cmd_name.lower()) or cmd_name.lower() in f.lower():
                    try:
                        os.rename(os.path.join(packages_dir, f), os.path.join(packages_dir, expected))
                        self.console.append(f"Renamed installed file '{f}' to '{expected}'")
                        return
                    except Exception as e:
                        self.console.append(f"Failed to rename '{f}' -> '{expected}': {e}")
                        return
        # if nothing matched, just log
        self.console.append(f"Could not find installed file to reconcile for '{cmd_name}'")

if __name__ == "__main__":
    app = QApplication(sys.argv)
    
    # Create a temporary IDE instance to load recent projects
    temp_ide = VGEditor()
    recent_projects = getattr(temp_ide, 'recent_projects', [])
    
    # Show startup dialog first
    startup_dialog = ProjectStartupDialog(recent_projects)
    if startup_dialog.exec_() == QDialog.Accepted:
        # User selected a project, now open the main IDE
        window = VGEditor()
        if startup_dialog.selected_project_path:
            # Set the project path in the IDE
            window.current_project_path = startup_dialog.selected_project_path
            # Add to recent projects
            window.add_to_recent_projects(startup_dialog.selected_project_path)
            # Load project files into the file explorer
            if hasattr(window, 'load_project'):
                window.load_project(startup_dialog.selected_project_path)
        
        window.show()
        sys.exit(app.exec_())
    else:
        # User cancelled or closed the dialog
        sys.exit(0)
