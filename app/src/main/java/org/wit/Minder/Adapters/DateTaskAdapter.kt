package org.wit.Minder.Adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.wit.Minder.Models.DateModel
import org.wit.Minder.R

class DateTaskAdapter(private val tasks: MutableList<DateModel>): RecyclerView.Adapter<DateTaskAdapter.TaskViewHolder>(){

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                                R.layout.datetask_view,parent,false
                        )
        )
    }

    fun stripThroughText(tvTitle: TextView, isChecked: Boolean){

        if(isChecked){
            tvTitle.paintFlags = tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        }else{
            tvTitle.paintFlags = tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    private lateinit var holder2: DateTaskAdapter.TaskViewHolder
    private var position2: Int = 0
    private var isChecked2: Boolean = false


    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
       var curTask = tasks[position]
        holder.itemView.apply{
            var title = findViewById(R.id.tvDateTitle) as TextView
            var checkbox = findViewById(R.id.cvDateDone) as CheckBox

            holder2 = holder
            position2 =position
            isChecked2 = checkbox.isChecked

            title.text = "Notify on " + curTask.Date + " " + curTask.reminderTime
            checkbox.isChecked = curTask.isChecked
            stripThroughText(title, curTask.isChecked)

            checkbox.setOnCheckedChangeListener{_,isChecked ->
                stripThroughText(title, isChecked)
                curTask.isChecked = !curTask.isChecked
                holder2 = holder
                position2 =position
                isChecked2 = checkbox.isChecked
            }
        }
    }

    fun addTask(task : DateModel){
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

    fun editTask(date: TextView,time:TextView){

        if(isChecked2){
            tasks[position2].Date =  date.text.toString()
            tasks[position2].reminderTime = time.text.toString()

        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
return tasks.size
    }
}