package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ExternalModel(
    val id : Int = 0,
    var unique_id : Long = 0,
    var email : String = "",
    var type : String = "",
    var title : String = "",
    var description : String = "",
    var is_sub_task_of : Long = 0, // store unique_id of event or goal
    var due_date : Long = 0,
    var start_date : Long = 0,
    var end_date : Long = 0, // relevant to due_date
    var reminder_date : Long = 0, // store subtracted date from due_date to schedule for notification
    var created_at : Long = 0,
    var completed_at : Long = 0,
    var is_updated : Boolean = false,
    var is_deleted : Boolean = false
)
