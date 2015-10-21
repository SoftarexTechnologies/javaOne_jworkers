/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softarex.jworkers.server;

import com.softarex.jworkers.common.client.JWorkerClient;
import com.softarex.jworkers.common.server.BaseWorker;
import com.softarex.jworkers.common.server.JWorkerFactory;
import com.softarex.jworkers.common.server.annotations.JWorkerTask;
import com.softarex.jworkers.common.task.BaseTask;
import java.util.Arrays;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import net.greghaines.jesque.worker.Worker;
import net.greghaines.jesque.worker.WorkerImpl;

/**
 * Test implementation
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class Program {

    /**
     * Send mail task example.
     */
    public static class EmailTask extends BaseTask {
        private String to;
        private String from;
        private String subject;
        
        public EmailTask() {
        }
        
        public EmailTask(String to, String from, String subject) {
            this.to      = to;
            this.from    = from;
            this.subject = subject;
        }

        @Override
        public String toString() {
            return "EmailTask{" + "to=" + to + ", from=" + from + ", subject=" + subject + '}';
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }
    
    /**
     * Worker which executes SendMail task...
     */
    @JWorkerTask(taskClass = EmailTask.class)
    public static class SendEmailWorker extends BaseWorker<EmailTask> {
        public SendEmailWorker(EmailTask task) {
            super(task);
        }

        @Override
        protected void run(EmailTask task) {
            System.out.println("Executing task: " + task);
        }
    }

    /**
     * Entry point. Test implementation
     * @param args 
     */
    public static void main(String[] args) {
        // TODO: load redis parameters from properties file
        final Config config = new ConfigBuilder().withDatabase(5).withHost("192.168.56.101").build();
        
        EmailTask task = new EmailTask("ivan@softarex.com", "ivan@infostroy.com.ua", "Support Message");
        final JWorkerClient client = new JWorkerClient(config);
        client.enqueue(task);
        
        JWorkerFactory workersFactory = new JWorkerFactory(SendEmailWorker.class);
        
        final Worker worker = new WorkerImpl(
            config,
            Arrays.asList(workersFactory.getQueues()),
            workersFactory
        );
                                                                                                                                                                                                 
        final Thread workerThread = new Thread(worker);
        workerThread.start();

        try {
            workerThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
