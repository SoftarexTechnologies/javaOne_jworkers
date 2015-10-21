package com.softarex.jworkers.common.server;

import com.softarex.jworkers.common.server.annotations.JWorkerQueue;
import com.softarex.jworkers.common.server.annotations.JWorkerTask;
import com.softarex.jworkers.common.task.BaseTask;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.utils.ReflectionUtils;
import net.greghaines.jesque.worker.MapBasedJobFactory;
import net.greghaines.jesque.worker.UnpermittedJobException;

/**
 *
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class JWorkerFactory extends MapBasedJobFactory {
    public static final String GENERAL_QUEUE = "general";
    
    private Set<String> queues = new HashSet<>();
            
    public JWorkerFactory(Class<?>... workers) {
        this(Arrays.asList(workers));
    }
    
    public JWorkerFactory(List<Class<?>> workers) {
        super(new HashMap<String, Class<?>>());
         
        for (Class<?> workerClazz : workers) {
            this.registerWorker(workerClazz);
        }
    }
    
    public final void registerWorker(Class<?> workerClazz) {
        JWorkerTask taskDef = (JWorkerTask) workerClazz.getAnnotation(JWorkerTask.class);
            
        if (taskDef == null || !BaseTask.class.isAssignableFrom(taskDef.taskClass())) {
            throw new IllegalArgumentException("Workers should have tasks inherited from " + BaseTask.class.getName()
                + ". Annotate your class by using JWorkerTask annotation");
        }

        this.addJobType(taskDef.taskClass().getName(), workerClazz);
        
        JWorkerQueue queue = (JWorkerQueue) workerClazz.getAnnotation(JWorkerQueue.class);
        if (queue != null) {
            this.queues.add(queue.value());
        } else {
            this.queues.add(GENERAL_QUEUE);
        }
    }
    
    public String[] getQueues() {
        return this.queues.toArray(new String[0]);
    }

    @Override
    public Object materializeJob(Job job) throws Exception {
        final Class<?> taskClazz = ReflectionUtils.forName(job.getClassName());
        if (taskClazz == null) {
            throw new UnpermittedJobException(job.getClassName());
        }

        if (!BaseTask.class.isAssignableFrom(taskClazz)) {
            throw new ClassCastException("task must be inherited from BaseTask: " + taskClazz.getName() + " - " + job);
        }

        Object[] args = job.getArgs();
        // TODO: validate args, the arguments list could be empty..
        Map<String, Object> taskArgsOrig = (Map) args[0];
        Map<String, Object> taskArgsDest = new HashMap<>();

        for (Map.Entry<String, Object> entry : taskArgsOrig.entrySet()) {
            String key = entry.getKey();
            java.lang.Object value = entry.getValue();
            taskArgsDest.put(key, value);
        }

        // first init task and do some validation
        BaseTask task = (BaseTask) ReflectionUtils.createObject(taskClazz, null, taskArgsDest);
        task.validate(); // throws exception if task is invalid

        // ok, task created. not we need construct the worker for this task
        if (!this.getJobTypes().containsKey(taskClazz.getName())) {
            throw new UnpermittedJobException("Can't find worker class name for task: " + taskClazz.getName());
        }

        Class<?> workerClazz = this.getJobTypes().get(taskClazz.getName());

        // A bit redundant since we check when the job type is added...
        if (!Runnable.class.isAssignableFrom(workerClazz) && !Callable.class.isAssignableFrom(workerClazz)) {
            throw new ClassCastException("jobs must be a Runnable or a Callable: " + workerClazz.getName() + " - " + job);
        }

        return ReflectionUtils.createObject(workerClazz, task);
    }
}
