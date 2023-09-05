package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id : Int = 0,
    var unique_id : Long = 0,
    var type : String = "",
    var title : String = "",
    var description : String = "",
    var is_sub_task_of : Long = 0, // store unique_id of event or goal
    var due_date : Long = 0,
    var reminder_date : Long = 0, // store subtracted date from due_date to schedule for notification
    var created_at : Long = 0,
    var completed_at : Long = 0
)

public fun Task.toExternalModel(
) = ExternalModel(
    id = this.id,
    unique_id = this.unique_id,
    type = this.type,
    title = this.title,
    description = this.description,
    is_sub_task_of = this.is_sub_task_of,
    due_date = this.due_date,
    reminder_date = this.reminder_date,
    created_at = this.created_at,
    completed_at = this.completed_at
)

@Serializable
data class Event(
    val id : Int = 0,
    var unique_id : Long = 0,
    var type : String = "",
    var title : String = "",
    var description : String = "",
    var start_date : Long = 0,
    var end_date : Long = 0,
    var reminder_date : Long = 0, // store subtracted date from due_date to schedule for notification
    var created_at : Long = 0,
    var completed_at : Long = 0
)

fun Event.toExternalModel(
) = ExternalModel(
    id = this.id,
    unique_id = this.unique_id,
    type = this.type,
    title = this.title,
    description = this.description,
    start_date = this.start_date,
    end_date = this.end_date,
    reminder_date = this.reminder_date,
    created_at = this.created_at,
    completed_at = this.completed_at
)

@Serializable
data class Goal(
    val id : Int = 0,
    var unique_id : Long = 0,
    var type : String = "",
    var title : String = "",
    var description : String = "",
    var start_date : Long = 0,
    var end_date : Long = 0, // relevant to due_date
    var reminder_date : Long = 0, // store subtracted date from due_date to schedule for notification
    var created_at : Long = 0,
    var completed_at : Long = 0
)

fun Goal.toExternalModel(
) = ExternalModel(
    id = this.id,
    unique_id = this.unique_id,
    type = this.type,
    title = this.title,
    description = this.description,
    start_date = this.start_date,
    end_date = this.end_date,
    reminder_date = this.reminder_date,
    created_at = this.created_at,
    completed_at = this.completed_at
)