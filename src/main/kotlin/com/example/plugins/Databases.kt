package com.example.plugins

import com.example.model.ExternalModel
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import java.sql.*
import kotlinx.coroutines.*

fun Application.configureDatabases() {
    val dbConnection: Connection = connectToPostgres(embedded = false)
    val taskService = TaskService(dbConnection)

    routing {
        post("/tasks/add") {
            var statusCode = HttpStatusCode.Accepted
            try {
                val taskList = call.receive<List<ExternalModel>>()
                taskList.forEach {
                    if (it.is_deleted){
                        val id = taskService.deleteTask(it.unique_id)
                        println("deleted : ${id}")
                    }
                    else{
                        val inserted = taskService.insertOrUpdateTask(it)
                        println("Inserted : ${inserted}")
                    }
                }
            }
            catch (e : Exception){
                statusCode = HttpStatusCode.Conflict
                println("error : ${e.toString()}")
            }
            call.respond(statusCode)
        }

        get("/tasks/all") {

            try {
                val allTasks = taskService.getAllTasks()
                println("task : ${allTasks}")
                call.respond(HttpStatusCode.OK, allTasks)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

/**
 * Makes a connection to a Postgres database.
 *
 * In order to connect to your running Postgres process,
 * please specify the following parameters in your configuration file:
 * - postgres.url -- Url of your running database process.
 * - postgres.user -- Username for database connection
 * - postgres.password -- Password for database connection
 *
 * If you don't have a database process running yet, you may need to [download]((https://www.postgresql.org/download/))
 * and install Postgres and follow the instructions [here](https://postgresapp.com/).
 * Then, you would be able to edit your url,  which is usually "jdbc:postgresql://host:port/database", as well as
 * user and password values.
 *
 *
 * @param embedded -- if [true] defaults to an embedded database for tests that runs locally in the same process.
 * In this case you don't have to provide any parameters in configuration file, and you don't have to run a process.
 *
 * @return [Connection] that represent connection to the database. Please, don't forget to close this connection when
 * your application shuts down by calling [Connection.close]
 * */
fun Application.connectToPostgres(embedded: Boolean): Connection {
    Class.forName("org.postgresql.Driver")
    if (embedded) {
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
    } else {

        val url = System.getenv("url").toString() // "jdbc:postgresql://localhost:5432/testing"
        val user = System.getenv("username").toString() // "postgres"  //System.getenv("username")
        val password = System.getenv("password").toString() // "12345" // System.getenv("password")

        return DriverManager.getConnection(url, user, password)
    }
}
