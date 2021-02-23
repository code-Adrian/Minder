package org.wit.Minder.Activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.Minder.Adapters.TaskAdapter
import org.wit.Minder.Models.TasksModel
import org.wit.Minder.R
import kotlin.concurrent.thread

private lateinit var taskAdapter: TaskAdapter

private val channel_ID = "Channel ID"
private val notificationId = 101
class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


            //Starts the listeneres
          touchListeners()
        //Creates Notification - Only once
       createNotification()

    }



    fun touchListeners(){
        //Main menu
        var tasks = findViewById(R.id.tasks) as CardView
        var weather = findViewById(R.id.weather) as CardView
        var calendar = findViewById(R.id.calendar) as CardView

        tasks.setOnClickListener{
            //Setting the content view
            setContentView(R.layout.tasks_layout)
            //Applying Task adapter a specific list ---- This is used to load the the task list --- mutableListof() is a function to add a clear list.
            taskAdapter = TaskAdapter(mutableListOf())
            var taskView = findViewById(R.id.rvTaskItems) as RecyclerView
            //Giving taskView the list.
            taskView.adapter = taskAdapter
            //setting the layout for the taskView
            taskView.layoutManager = LinearLayoutManager(this)


            //Listeners - Temporarly
            var addTask = findViewById(R.id.btnAddTask) as Button
            var editTask = findViewById(R.id.btnEditTask) as Button
            var removeTask = findViewById(R.id.btnDeleteTask) as Button
            var taskTime = findViewById(R.id.editTextTime) as EditText
            var taskDescription = findViewById(R.id.editTaskDesc) as EditText

            addTask.setOnClickListener{
                if(taskTime.text.isNotBlank()) {
                    if(taskDescription.text.isNotBlank()) {
                        if (taskTime.text.toString().length <= 5) {

                            val time = taskTime.text.toString()
                            val description = taskDescription.text.toString()
                            val task = TasksModel(time + " " + description, time)
                            taskAdapter.addTask(task)

                            taskTime.text.clear()
                            taskDescription.text.clear()

                        } else {
                            toast("Time is invalid")
                            taskTime.text.clear()
                            taskDescription.text.clear()
                        }
                    }else{
                        toast("Task description is empty")
                    }
                }else{
                    toast("Task time is empty")
                }
            }

            editTask.setOnClickListener{
             taskAdapter.editTask(taskDescription,taskTime)

                }

            removeTask.setOnClickListener{

                taskAdapter.deleteDoneTasks()
            }

        }

        weather.setOnClickListener{
            toast("Test2")


        }

        calendar.setOnClickListener{
            toast("Test3")
        }
    }


    fun createNotification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val name = "Notification Title"
            val descriptiontxt = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channel_ID,name,importance).apply {

                description = descriptiontxt
            }


            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)

        }
    }

   fun sendNotification(title: String, description: String){

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(this,channel_ID);
        builder.setSmallIcon(R.drawable.background)
        builder.setContentTitle(title)
        builder.setContentText(description)
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId,builder.build())
        }
    }

    


}


