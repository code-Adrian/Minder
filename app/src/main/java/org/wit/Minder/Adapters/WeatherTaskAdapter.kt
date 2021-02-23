package org.wit.Minder.Adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.wit.Minder.Models.TasksModel
import org.wit.Minder.Models.WeatherModel
import org.wit.Minder.R

class WeatherTaskAdapter( private val tasks: MutableList<WeatherModel>): RecyclerView.Adapter<WeatherTaskAdapter.WeatherTaskViewHolder>() {
    class WeatherTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherTaskViewHolder {
        return WeatherTaskViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.weathertask_view, parent, false
            )
        )
    }

    fun stripThroughText(tvWeatherTitle: TextView, isChecked: Boolean) {

        if (isChecked) {
            tvWeatherTitle.paintFlags = tvWeatherTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        } else {
            tvWeatherTitle.paintFlags =
                tvWeatherTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }


    private lateinit var holder2: WeatherTaskViewHolder
    private var position2: Int = 0
    private var isChecked2: Boolean = false



    override fun onBindViewHolder(holder: WeatherTaskViewHolder, position: Int) {

        var curTask = tasks[position]
        holder.itemView.apply {
            var title = findViewById(R.id.tvWeatherTitle) as TextView
            var checkbox = findViewById(R.id.cvWeatherDone) as CheckBox
            title.text = "Notify when temperature drops below " + curTask.WeatherTemp + " in (" + curTask.Country +", "+curTask.County +", "+curTask.City+")"

            holder2 = holder
            position2 =position
            isChecked2 = checkbox.isChecked

            checkbox.isChecked = curTask.isChecked
            stripThroughText(title,curTask.isChecked)


            checkbox.setOnCheckedChangeListener{_,isChecked ->
                stripThroughText(title,isChecked)
                curTask.isChecked = !curTask.isChecked

                holder2 = holder
                position2 =position
                isChecked2 = checkbox.isChecked
            }
        }


    }



    fun addTask(task: WeatherModel){
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

    fun editTask( country: TextView,county: TextView,city: TextView, weatherTemp : TextView){


        if(isChecked2){
            tasks[position2].Country = country.text.toString()
            tasks[position2].County = county.text.toString()
            tasks[position2].City = city.text.toString()
            tasks[position2].WeatherTemp = Integer.parseInt(weatherTemp.text.toString())

        }
        notifyDataSetChanged()

    }




    override fun getItemCount(): Int {
        return tasks.size
    }
}