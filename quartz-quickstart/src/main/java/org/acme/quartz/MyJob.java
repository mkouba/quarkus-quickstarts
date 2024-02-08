package org.acme.quartz;

import java.util.function.Consumer;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.ScheduledExecution;
import io.quarkus.scheduler.Scheduler;
import jakarta.enterprise.event.Observes;

public class MyJob implements Consumer<ScheduledExecution> {

    @Override
    public void accept(ScheduledExecution se) {
        Log.infof("Executed! Next fire time: %s", se.getTrigger().getNextFireTime());
    }

    void onStart(@Observes StartupEvent e, Scheduler scheduler) {
        if (scheduler.getScheduledJob("myJob") == null) {
            Log.info("MyJob scheduled!");
            scheduler.newJob("myJob").setInterval("3s").setTask(MyJob.class).schedule();
        } else {
            Log.infof("MyJob already exists!");
        }
    }

}
