package com.example.plugins

import com.example.model.ExternalModel
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.Statement

@Serializable
class TaskService(private val connection: Connection) {
    companion object {

        private const val CREATE_TABLE_TASK = """CREATE TABLE TASK IF NOT EXISTS (ID SERIAL PRIMARY KEY,
            UNIQUE_ID BIGINT UNIQUE NOT NULL DEFAULT 0,
            TYPE VARCHAR(10),
            TITLE VARCHAR(255),
            DESCRIPTION VARCHAR(255),
            IS_SUB_TASK_OF BIGINT NOT NULL DEFAULT 0,
            DUE_DATE BIGINT NOT NULL DEFAULT 0,
            START_DATE BIGINT NOT NULL DEFAULT 0,
            END_DATE BIGINT NOT NULL DEFAULT 0,
            REMINDER_DATE BIGINT NOT NULL DEFAULT 0,
            CREATED_AT BIGINT NOT NULL DEFAULT 0,
            COMPLETED_AT BIGINT NOT NULL DEFAULT 0);"""
        private const val INSERT_OR_UPDATE_TASK = "INSERT INTO TASK (UNIQUE_ID, TYPE, TITLE, DESCRIPTION, IS_SUB_TASK_OF, DUE_DATE, START_DATE, END_DATE, REMINDER_DATE, CREATED_AT, COMPLETED_AT) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (UNIQUE_ID) DO UPDATE SET TYPE = EXCLUDED.TYPE, TITLE = EXCLUDED.TITLE, DESCRIPTION = EXCLUDED.DESCRIPTION, IS_SUB_TASK_OF = EXCLUDED.IS_SUB_TASK_OF, DUE_DATE = EXCLUDED.DUE_DATE" +
                ", START_DATE = EXCLUDED.START_DATE, END_DATE = EXCLUDED.END_DATE, REMINDER_DATE = EXCLUDED.REMINDER_DATE, CREATED_AT = EXCLUDED.CREATED_AT, COMPLETED_AT = EXCLUDED.COMPLETED_AT;"
        private const val DELETE_TASK = "DELETE FROM TASK WHERE UNIQUE_ID = ?;"

/*
        private const val CREATE_TABLE_EVENT = """CREATE TABLE EVENT (ID SERIAL PRIMARY KEY,
            UNIQUE_ID BIGINT UNIQUE NOT NULL DEFAULT 0,
            TYPE VARCHAR(10),
            TITLE VARCHAR(255),
            DESCRIPTION VARCHAR(255),
            IS_SUB_TASK_OF BIGINT NOT NULL DEFAULT 0,
            START_DATE BIGINT NOT NULL DEFAULT 0,
            END_DATE BIGINT NOT NULL DEFAULT 0,
            REMINDER_DATE BIGINT NOT NULL DEFAULT 0,
            CREATED_AT BIGINT NOT NULL DEFAULT 0,
            COMPLETED_AT BIGINT NOT NULL DEFAULT 0);"""
        private const val INSERT_OR_UPDATE_EVENT = "INSERT INTO EVENT (UNIQUE_ID, TYPE, TITLE, DESCRIPTION, IS_SUB_TASK_OF, START_DATE, END_DATE, REMINDER_DATE, CREATED_AT, COMPLETED_AT) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (UNIQUE_ID) DO UPDATE SET TYPE = EXCLUDED.TYPE, TITLE = EXCLUDED.TITLE, DESCRIPTION = EXCLUDED.DESCRIPTION, IS_SUB_TASK_OF = EXCLUDED.IS_SUB_TASK_OF, START_DATE = EXCLUDED.START_DATE" +
                ", END_DATE = EXCLUDED.END_DATE, REMINDER_DATE = EXCLUDED.REMINDER_DATE, CREATED_AT = EXCLUDED.CREATED_AT, COMPLETED_AT = EXCLUDED.COMPLETED_AT;"
        private const val DELETE_EVENT = "DELETE FROM EVENT WHERE UNIQUE_ID = ?;"

        private const val CREATE_TABLE_GOAL = """CREATE TABLE GOAL (ID SERIAL PRIMARY KEY,
            UNIQUE_ID BIGINT UNIQUE NOT NULL DEFAULT 0,
            TYPE VARCHAR(10),
            TITLE VARCHAR(255),
            DESCRIPTION VARCHAR(255),
            IS_SUB_TASK_OF BIGINT NOT NULL DEFAULT 0,
            START_DATE BIGINT NOT NULL DEFAULT 0,
            END_DATE BIGINT NOT NULL DEFAULT 0,
            REMINDER_DATE BIGINT NOT NULL DEFAULT 0,
            CREATED_AT BIGINT NOT NULL DEFAULT 0,
            COMPLETED_AT BIGINT NOT NULL DEFAULT 0);"""
        private const val INSERT_OR_UPDATE_GOAL = "INSERT INTO GOAL (UNIQUE_ID, TYPE, TITLE, DESCRIPTION, IS_SUB_TASK_OF, START_DATE, END_DATE, REMINDER_DATE, CREATED_AT, COMPLETED_AT) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (UNIQUE_ID) DO UPDATE SET TYPE = EXCLUDED.TYPE, TITLE = EXCLUDED.TITLE, DESCRIPTION = EXCLUDED.DESCRIPTION, IS_SUB_TASK_OF = EXCLUDED.IS_SUB_TASK_OF, START_DATE = EXCLUDED.START_DATE" +
                ", END_DATE = EXCLUDED.END_DATE, REMINDER_DATE = EXCLUDED.REMINDER_DATE, CREATED_AT = EXCLUDED.CREATED_AT, COMPLETED_AT = EXCLUDED.COMPLETED_AT;"
        private const val DELETE_GOAL = "DELETE FROM GOAL WHERE UNIQUE_ID = ?;"
*/

        private const val GET_ALL_TASKS = "SELECT * FROM TASK;"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_TASK)
    }



    suspend fun insertOrUpdateTask(task : ExternalModel): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_OR_UPDATE_TASK, Statement.RETURN_GENERATED_KEYS)
        statement.setLong(1, task.unique_id)
        statement.setString(2, task.type)
        statement.setString(3, task.title)
        statement.setString(4, task.description)
        statement.setLong(5, task.is_sub_task_of)
        statement.setLong(6, task.due_date)
        statement.setLong(7, task.start_date)
        statement.setLong(8, task.end_date)
        statement.setLong(9, task.reminder_date)
        statement.setLong(10, task.created_at)
        statement.setLong(11, task.completed_at)

        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted task")
        }
    }

/*

    suspend fun insertOrUpdateEvent(event : ExternalModel) : Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_OR_UPDATE_EVENT, Statement.RETURN_GENERATED_KEYS)
        statement.setLong(1, event.unique_id)
        statement.setString(2, event.type)
        statement.setString(3, event.title)
        statement.setString(4, event.description)
        statement.setLong(5, event.is_sub_task_of)
        statement.setLong(6, event.start_date)
        statement.setLong(7, event.end_date)
        statement.setLong(8, event.reminder_date)
        statement.setLong(9, event.created_at)
        statement.setLong(10, event.completed_at)

        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted event")
        }
    }

    suspend fun insertOrUpdateGoal(goal : ExternalModel) : Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_OR_UPDATE_GOAL, Statement.RETURN_GENERATED_KEYS)
        statement.setLong(1, goal.unique_id)
        statement.setString(2, goal.type)
        statement.setString(3, goal.title)
        statement.setString(4, goal.description)
        statement.setLong(5, goal.is_sub_task_of)
        statement.setLong(6, goal.due_date)
        statement.setLong(7, goal.reminder_date)
        statement.setLong(8, goal.created_at)
        statement.setLong(9, goal.completed_at)

        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted event")
        }
    }
*/

    suspend fun getAllTasks(): List<ExternalModel> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(GET_ALL_TASKS)
        val resultSet = statement.executeQuery()

        val list = mutableListOf<ExternalModel>()
        while(resultSet.next()){
            val externalModel = ExternalModel(
                unique_id = resultSet.getLong("UNIQUE_ID"),
                type = resultSet.getString("TYPE"),
                title = resultSet.getString("TITLE"),
                description = resultSet.getString("DESCRIPTION"),
                is_sub_task_of = resultSet.getLong("IS_SUB_TASK_OF"),
                due_date = resultSet.getLong("DUE_DATE"),
                start_date = resultSet.getLong("START_DATE"),
                end_date = resultSet.getLong("END_DATE"),
                reminder_date = resultSet.getLong("REMINDER_DATE"),
                created_at = resultSet.getLong("CREATED_AT"),
                completed_at = resultSet.getLong("COMPLETED_AT")
            )
            list.add(externalModel)
        }
        return@withContext list
    }

    suspend fun deleteTask(unique_id : Long) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_TASK)
        statement.setLong(1, unique_id)
        return@withContext statement.executeUpdate()
    }
}
