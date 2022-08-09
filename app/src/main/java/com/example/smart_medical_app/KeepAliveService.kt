package com.example.smart_medical_app

import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

class KeepAliveService : JobService() {
    private lateinit var LocalServiceUntils:ServiceUtils
    private lateinit var RemoteServiceUntils:ServiceUtils
    private lateinit var MQTTServiceUntils:ServiceUtils
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.e("JAMES","JobService onStartJob")
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
            startJob(this)
        }
        LocalServiceUntils= ServiceUtils(this,MyLocalService::class.java.name)
        val isLocalServiceRunning: Boolean = LocalServiceUntils.isServiceRunning()
        if (!isLocalServiceRunning) {
            startService(Intent(this, MyLocalService::class.java))
        }
        RemoteServiceUntils= ServiceUtils(this,MyRemoteService::class.java.name)
        val isRemoteServiceRunning: Boolean = RemoteServiceUntils.isServiceRunning()
        if (!isRemoteServiceRunning) {
            startService(Intent(this, MyRemoteService::class.java))
        }
        MQTTServiceUntils= ServiceUtils(this,MyMQTTService::class.java.name)

        return false
    }
    companion object{
        fun startJob(context: Context) {
            val jobScheduler: JobScheduler =context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val jobInfoBuilder = JobInfo.Builder(
                10,
                ComponentName(context.packageName, KeepAliveService::class.java.name)
            ).setPersisted(true)
            // 7.0 以下的版本, 可以每隔 5000 毫秒执行一次任务
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
                jobInfoBuilder.setPeriodic(5_000);

            }else{
                // 7.0 以上的版本 , 设置延迟 5 秒执行
                // 该时间不能小于 JobInfo.getMinLatencyMillis 方法获取的最小值
                jobInfoBuilder.setMinimumLatency(5_000)
            }
            jobScheduler.schedule(jobInfoBuilder.build())
        }
    }
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i("JAMES", "JobService onStopJob 关闭");
        return false
    }
}