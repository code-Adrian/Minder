package org.wit.Minder.Adapters

import android.app.Activity
import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.wit.Minder.Models.DateModel
import org.wit.Minder.Models.TasksModel
import org.wit.Minder.R
import java.lang.Exception

class TaskAdapter( private val tasks: MutableList<TasksModel>): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    //This will hold the "task_view" in a holder.
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val uniqueFirebaseID: String = (android.os.Build.MODEL.toString() + " " + android.os.Build.ID+ " " + android.os.Build.USER + " --TASKS").replace(".","")




//This will inflate the class "TaskViewHolder" with task_view. The class TaskViewHolder accepts a view as a parameter which we use to inflate the class.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        //This gets the view from the "task_view" and converts it to a class that we can use
    return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(
                    R.layout.task_view,parent,false
            )
    )
    }

    fun stripThroughText(tvTitle: TextView, isChecked: Boolean){

        if(isChecked){
            tvTitle.paintFlags = tvTitle.paintFlags or STRIKE_THRU_TEXT_FLAG

        }else{
            tvTitle.paintFlags = tvTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    private lateinit var holder2: TaskViewHolder
    private var position2: Int = 0
    private var isChecked2: Boolean = false

    fun firebas(){

        var ref = FirebaseDatabase.getInstance("https://minder-c3142-default-rtdb.firebaseio.com/").getReference().child("This is a test")
        ref.push().setValue("Tsfdgdfsg")
    }

    // Called by RecyclerView to display the data at the specified position.
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
var curTask = tasks[position]
       holder.itemView.apply {

           var title = findViewById(R.id.tvTitle) as TextView
           var checkbox = findViewById(R.id.cvDone) as CheckBox

           holder2 = holder
                   position2 =position
           isChecked2 = checkbox.isChecked

           title.text =  curTask.Time + " " +curTask.taskTitle
           checkbox.isChecked = curTask.isChecked
           stripThroughText(title, curTask.isChecked)


           checkbox.setOnCheckedChangeListener { _, isChecked ->

               stripThroughText(title, isChecked)
               curTask.isChecked = !curTask.isChecked
               holder2 = holder
               position2 =position
               isChecked2 = checkbox.isChecked
           }


       }


    }

    override fun getItemCount(): Int {
        return tasks.size
    }


    fun addTask(task: TasksModel){
        //Adds
        tasks.add(task)
        //Makes it visible to the recycler view
        notifyItemInserted(itemCount-1)

    }

fun deleteDoneTasks(){
    tasks.removeAll{task ->
        task.isChecked

    }
    notifyDataSetChanged()
}

  fun editTask( description: TextView, time : TextView){


        if(isChecked2){
             tasks[position2].taskTitle =  description.text.toString()
            tasks[position2].Time = time.text.toString()

        }
      notifyDataSetChanged()

    }




    fun firebaseSave(){

        var ref = FirebaseDatabase.getInstance("https://minder-82d22-default-rtdb.firebaseio.com/").getReference().child(uniqueFirebaseID)
        ref.setValue(tasks)

    }

    fun firebasePull(){

        var ref = FirebaseDatabase.getInstance("https://minder-82d22-default-rtdb.firebaseio.com/").getReference().child(uniqueFirebaseID)
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (p0: DataSnapshot in p0.children) {


                    if (p0 != null) {
                        val title = (p0.child("taskTitle").getValue().toString())
                        val time = (p0.child("time").getValue().toString())
                        val checked = (p0.child("checked").getValue().toString())

                        val taskModel = TasksModel(title, time, checked.toBoolean())
                        tasks.add(taskModel)
                    }

                }
                notifyDataSetChanged()
            }
        })
    }

}