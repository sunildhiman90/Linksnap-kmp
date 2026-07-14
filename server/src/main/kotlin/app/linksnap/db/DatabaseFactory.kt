package app.linksnap.db

import app.linksnap.features.auth.data.UsersTable
import app.linksnap.features.link.data.LinksTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils

object DatabaseFactory {


    fun init() {

        val jdbcUrl = System.getenv("JDBC_DATABASE_URL") ?: "jdbc:mysql://localhost:3306/linksnap"
        val driverClassName = "com.mysql.cj.jdbc.Driver"
        val user = System.getenv("JDBC_LINKSNAP_DATABASE_USER") ?: "root"
        val password = System.getenv("JDBC_LINKSNAP_DATABASE_PASSWORD") ?: "password"

        val db = Database.connect(createHikariDataSource(
            jdbcUrl, driverClassName, user, password
        ))

        transaction(db) {
            SchemaUtils.create(UsersTable, LinksTable)

           MigrationUtils.statementsRequiredForDatabaseMigration(
               UsersTable, LinksTable
            ).forEach {
                exec(it)
            }
        }
    }


    fun createHikariDataSource(
        jdbcUrlVal: String,
        driverClassNameVal: String,
        userVal: String,
        passwordVal: String
    ): HikariDataSource {
        return HikariDataSource(HikariConfig().apply {
            jdbcUrl = jdbcUrlVal
            username = userVal
            password = passwordVal
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        })

    }

    suspend fun <T> dbQuery(block: () -> T) : T =
        suspendTransaction { withContext(Dispatchers.IO)  { block() }  }


}