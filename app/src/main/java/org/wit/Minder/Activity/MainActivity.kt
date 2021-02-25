package org.wit.Minder.Activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.wit.Minder.Adapters.DateTaskAdapter
import org.wit.Minder.Adapters.TaskAdapter
import org.wit.Minder.Adapters.WeatherTaskAdapter
import org.wit.Minder.Models.DateModel
import org.wit.Minder.Models.TasksModel
import org.wit.Minder.Models.WeatherModel
import org.wit.Minder.R

//Adapters
private lateinit var taskAdapter: TaskAdapter
private lateinit var taskWeatherAdapter: WeatherTaskAdapter
private lateinit var taskDateAdapter: DateTaskAdapter
//Notifications
private val channel_ID = "Channel ID"
private val notificationId = 101


class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // var doc = Jsoup.connect("https://www.google.com/search?q=weather+forcast+ireland+tipperary").get()
     //   println(doc.getElementsByClass("wob_t TVtOme").text())

        //Starts the listeneres
        touchListeners()
        //Creates Notification - Only once
        createNotification()
// Multi Threading is here. Starts a background thread to keep track of notifications
                //  Thread(Runnable {
          //  runner()
      //  }).start()
    }



    fun touchListeners(){
        //Main menu
        val tasks = findViewById(R.id.tasks) as CardView
        val weather = findViewById(R.id.weather) as CardView
        val calendar = findViewById(R.id.calendar) as CardView

        tasks.setOnClickListener{
            //Setting the content view
            setContentView(R.layout.tasks_layout)
            //Applying Task adapter a specific list ---- This is used to load the the task list --- mutableListof() is a function to add a clear list.
            taskAdapter = TaskAdapter(mutableListOf())
            val taskView = findViewById(R.id.rvTaskItems) as RecyclerView
            //Giving taskView the list.
            taskView.adapter = taskAdapter
            //setting the layout for the taskView
            taskView.layoutManager = LinearLayoutManager(this)

taskAdapter.firebasePull()
            //Listeners - Temporarly
            val addTask = findViewById(R.id.btnAddTask) as Button
            val editTask = findViewById(R.id.btnEditTask) as Button
            val removeTask = findViewById(R.id.btnDeleteTask) as Button
            val goBack = findViewById(R.id.taskGoBack) as Button
            val taskTime = findViewById(R.id.editTextTime) as EditText
            val taskDescription = findViewById(R.id.editTaskDesc) as EditText

            addTask.setOnClickListener{
                if(taskTime.text.isNotBlank()) {
                    if(taskDescription.text.isNotBlank()) {
                        if (taskTime.text.toString().length <= 5) {

                            val time = taskTime.text.toString()
                            val description = taskDescription.text.toString()
                            val task = TasksModel(description, time)
                            taskAdapter.addTask(task)
taskAdapter.firebaseSave()
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
                taskAdapter.firebaseSave()
            }

            removeTask.setOnClickListener{

                taskAdapter.deleteDoneTasks()
                taskAdapter.firebaseSave()
            }

            goBack.setOnClickListener{
                setContentView(R.layout.activity_main)
                touchListeners()
            }

        }




        weather.setOnClickListener{
           setContentView(R.layout.weathertask_layout)

            taskWeatherAdapter = WeatherTaskAdapter(mutableListOf())
            val taskWeatherView = findViewById(R.id.rvTaskWeatherItems) as RecyclerView
            taskWeatherView.adapter = taskWeatherAdapter
            taskWeatherView.layoutManager = LinearLayoutManager(this)

taskWeatherAdapter.firebasePull()
            //Buttons
            val btnWeatherAdd = findViewById(R.id.btnAddWeatherTask) as Button
            val btnWeatherDelete = findViewById(R.id.btnDeleteWeatherTask) as Button
            val btnWeatherEdit = findViewById(R.id.btnEditWeatherTask) as Button
            val goBack = findViewById(R.id.weatherGoBack) as Button
            //Text Views
            val Country = findViewById(R.id.editTaskWeatherCountry) as EditText
            val County = findViewById(R.id.editTaskWeatherCounty) as EditText
            val City = findViewById(R.id.editTaskWeatherCity) as EditText
            val WeatherTemp = findViewById(R.id.editTextWeatherTemp) as EditText

            btnWeatherAdd.setOnClickListener{
                val Weathermodel = WeatherModel(Country.text.toString(),County.text.toString(),City.text.toString(),Integer.parseInt(WeatherTemp.text.toString()))
                taskWeatherAdapter.addTask(Weathermodel)
                Country.text.clear()
                County.text.clear()
                City.text.clear()
                WeatherTemp.text.clear()
                taskWeatherAdapter.firebaseSave()
            }
            btnWeatherDelete.setOnClickListener{
taskWeatherAdapter.deleteDoneTasks()
                taskWeatherAdapter.firebaseSave()
            }
            btnWeatherEdit.setOnClickListener{
taskWeatherAdapter.editTask(Country,County,City,WeatherTemp)
                taskWeatherAdapter.firebaseSave()
            }

            goBack.setOnClickListener{
                setContentView(R.layout.activity_main)
                touchListeners()
            }
        }

        calendar.setOnClickListener{
            setContentView(R.layout.datetask_layout)

            taskDateAdapter = DateTaskAdapter(mutableListOf())
            val taskDateView = findViewById(R.id.rvTaskDateItems) as RecyclerView

            taskDateView.adapter = taskDateAdapter
            taskDateView.layoutManager = LinearLayoutManager(this)

            //Buttons
            val btnDateAdd = findViewById(R.id.btnAddDateTask) as Button
            val btnDateDelete = findViewById(R.id.btnDeleteDateTask) as Button
            val btnDateEdit = findViewById(R.id.btnEditDateTask) as Button
            val goBack = findViewById(R.id.dateGoBack) as Button
            //TextView
            val time = findViewById(R.id.editTextDateTime) as EditText
            val date = findViewById(R.id.editTextDateTask) as EditText
            taskDateAdapter.firebasePull()

            btnDateAdd.setOnClickListener{
                val dateModel = DateModel(date.text.toString(),time.text.toString())
                taskDateAdapter.addTask(dateModel)
taskDateAdapter.firebaseSave()
            }

            btnDateDelete.setOnClickListener{
                taskDateAdapter.deleteDoneTasks()
                taskDateAdapter.firebaseSave()
            }

            btnDateEdit.setOnClickListener{

                taskDateAdapter.editTask(date,time)
                taskDateAdapter.firebaseSave()
            }

            goBack.setOnClickListener{
                setContentView(R.layout.activity_main)
                touchListeners()
            }

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





    fun runner(){

        while(true) {
           Thread.sleep(3000)
            sendNotification("This is test", "You have a reminder!")
        }
    }

}


