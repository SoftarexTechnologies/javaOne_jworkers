package com.softarex.jworker.demo.client;

import com.softarex.jworker.core.notifications.NotificationType;
import com.softarex.jworker.core.task.BaseTask;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class MainFrame extends JFrame {
    private JTextField  tfDelay;
    private JTextField  tfScheduledTasks;
    private JTextField  tfExecutedTasks;
    private StartAction startAction;
    private StopAction  stopAction;
    
    private TasksTableModel tasksTableModel;
    private JScrollPane scrollPane;
    
    private Timer timerScheduler;
    
    public MainFrame() throws HeadlessException {
        super("Tasks Management - JavaOne 2015");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.initUI();
    }
    
    public void rise(ActionListener scheduleTaskListener) {
        this.initTimer(scheduleTaskListener);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public void addTask(BaseTask task) {
        this.tasksTableModel.addTask(task);
        this.tfScheduledTasks.setText(String.valueOf(this.tasksTableModel.getRowCount()));
    }
    
    public void updateTask(BaseTask task, NotificationType nt) {
        this.tasksTableModel.updateTask(task, nt);
        this.tfExecutedTasks.setText(
            String.valueOf(this.tasksTableModel.getExecutedTasksCount()));
        JScrollBar vertical = this.scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }
    
    private void initUI() {
        JPanel contentPanel = new JPanel(new BorderLayout(2, 2));
        contentPanel.add(this.initControlBlock(), BorderLayout.NORTH);
        contentPanel.add(this.initTasksListBlock(), BorderLayout.CENTER);
        contentPanel.add(this.initSummaryBlock(), BorderLayout.SOUTH);
        this.setContentPane(contentPanel);
    }
    
    private JPanel initControlBlock() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("Interval (ms)"));
        this.tfDelay = new JTextField("1000", 4);
        controlPanel.add(this.tfDelay);
        this.startAction = new StartAction();
        this.stopAction = new StopAction();
        
        controlPanel.add(new JButton(startAction));
        controlPanel.add(new JButton(stopAction));
        
        return controlPanel;
    }
    
    private Component initTasksListBlock() {
        this.tasksTableModel = new TasksTableModel();
        
        JTable table = new JTable();
        table.setModel(this.tasksTableModel);
        
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(2).setMaxWidth(150);
        
        this.scrollPane = new JScrollPane(table);
        return this.scrollPane;
    }
    
    private JPanel initSummaryBlock() {
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.add(new JLabel("Scheduled:"));
        this.tfScheduledTasks = new JTextField("0", 4);
        this.tfScheduledTasks.setEnabled(false);
        summaryPanel.add(this.tfScheduledTasks);
        
        summaryPanel.add(new JLabel("Executed: "));
        this.tfExecutedTasks = new JTextField("0", 4);
        this.tfExecutedTasks.setEnabled(false);
        summaryPanel.add(this.tfExecutedTasks);
        
        return summaryPanel;
    }

    protected void startScheduling() {
        int delay = Integer.valueOf(this.tfDelay.getText());
        this.timerScheduler.setDelay(delay);
        this.timerScheduler.start();
        
        this.updateControls(false);
    }
    
    protected void stopScheduling() {
        this.updateControls(true);
        this.timerScheduler.stop();
    }
    
    protected void updateControls(boolean canStart) {
        this.startAction.setEnabled(canStart);
        this.stopAction.setEnabled(!canStart);
        this.tfDelay.setEditable(canStart);
    }

    private void initTimer(ActionListener scheduleTaskListener) {
        this.timerScheduler = new Timer(1000, scheduleTaskListener);
    }

    //<editor-fold defaultstate="collapsed" desc="TasksTableModel">
    private static class TasksTableModel extends AbstractTableModel {
        private final List<TaskInfo> tasks = new ArrayList<>();
        
        @Override
        public int getRowCount() {
            return this.tasks.size();
        }
        
        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "#";
                    
                case 1:
                    return "Task";
                    
                case 2:
                    return "Status";
            }
            return super.getColumnName(column);
        }
        
        @Override
        public int getColumnCount() {
            return 3;
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            TaskInfo ti = this.tasks.get(rowIndex);
            
            switch(columnIndex) {
                case 0:
                    return rowIndex + 1;
                    
                case 1:
                    return ti.getTitle();
                    
                case 2:
                    return ti.getStatus();
            }
            
            return "";
        }
        
        public int addTask(BaseTask task) {
            int index = this.tasks.size();
            this.tasks.add(new TaskInfo(task, null));
            
            this.fireTableRowsInserted(index, index);
            
            return this.tasks.size();
        }
        
        public boolean updateTask(BaseTask oldTask, NotificationType nt) {
            for (int i=0; i<tasks.size(); i++) {
                TaskInfo ti = this.tasks.get(i);
                
                if(ti.task.equals(oldTask)) {
                    boolean res = ti.setStatus(nt);
                    this.fireTableRowsUpdated(i, i);
                    return res;
                }
            }
            
            return false;
        }
        
        private int getExecutedTasksCount() {
            int count = 0;
            for (TaskInfo ti : tasks) {
                if (ti.isExecuted()) {
                    count++;
                }
            }
            
            return count;
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TaskInfo">
    private static class TaskInfo {
        private final BaseTask task;
        private NotificationType status;
        
        public TaskInfo(BaseTask task, NotificationType status) {
            this.task = task;
            this.status = status;
        }
        
        public String getTitle() {
            return this.task.getClass().getName();
        }
        
        public String getStatus() {
            return this.status == null ? "" : this.status.toString();
        }
        
        public boolean setStatus(NotificationType status) {
            boolean res = this.status != status;
            this.status = status;
            return res;
        }
        
        private boolean isExecuted() {
            return this.status != null && this.status != NotificationType.TASK_IN_PROGRESS;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Actions">
    private class StartAction extends AbstractAction {
        public StartAction() {
            super("Start");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            startScheduling();
        }
    }
    
    private class StopAction extends AbstractAction {
        public StopAction() {
            super("Stop");
            this.setEnabled(false);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            stopScheduling();
        }
    }
    //</editor-fold>
}
