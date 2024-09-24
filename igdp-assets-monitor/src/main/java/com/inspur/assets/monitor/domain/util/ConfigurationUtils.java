package com.inspur.assets.monitor.domain.util;

import com.inspur.assets.monitor.domain.AssetsCurConfiguration;
import com.inspur.common.exception.job.TaskException;
import com.inspur.common.utils.StringUtils;
import org.quartz.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author 王海龙
 */
public class ConfigurationUtils {
    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, AssetsCurConfiguration job) throws SchedulerException, TaskException
    {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        if (StringUtils.isNotEmpty(job.getStatus()) && job.getStatus().equals("0")){
            String execution = job.getExecution();
            String frequency = job.getFrequency();
            TimeUnit timeUnit;
            if(frequency.equals("分")){
                timeUnit = TimeUnit.MINUTES;
            }else if(frequency.equals("小时")){
                timeUnit = TimeUnit.HOURS;
            }else {
                timeUnit = TimeUnit.DAYS;
            }
            String phoneNumber = job.getRecipient();
            try {
                scheduledExecutorService.scheduleAtFixedRate(() -> {
//                    if (job.getNotificationFrom().equals("0")){
//                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:" + phoneNumber));
//                        context.startActivity(intent);
//                    }

                },1,Integer.parseInt(execution), timeUnit);
            }catch (Exception e){
                e.getMessage();
            }

        }
    }
}
