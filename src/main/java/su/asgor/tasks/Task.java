package su.asgor.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Task {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron="*/30 * * * * 4")
    public void reportCurrentTime() {
    	log.info("scheduled task triggered");      
    }
}
