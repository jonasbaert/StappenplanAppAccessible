package hogent.bachelor.stappenplanappbase.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import hogent.bachelor.stappenplanappbase.domain.stapDomainToDB
import hogent.bachelor.stappenplanappbase.domain.stappenplanDomainToDB
import hogent.bachelor.stappenplanappbase.persistence.daos.StappenplanDao
import hogent.bachelor.stappenplanappbase.persistence.entities.StapEntity
import hogent.bachelor.stappenplanappbase.persistence.entities.StappenplanEntity

@Database(
    entities = [
        StappenplanEntity::class, StapEntity::class],
    version = 18,
    exportSchema = false
)
abstract class StappenplanDatabase : RoomDatabase() {
    abstract val stappenplanDao: StappenplanDao

    companion object {
        @Volatile
        private var INSTANCE: StappenplanDatabase? = null

        fun getInstance(context: Context): StappenplanDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = createInstance(context)
                }
                return INSTANCE!!
            }

        }

        private fun createInstance(context: Context) =
            Room.databaseBuilder(context.applicationContext, StappenplanDatabase::class.java, "StappenplanDatabase")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Thread(Runnable { prepopulateDb(context, getInstance(context)) }).start()
                    }
                })
                .build()


        private fun prepopulateDb(context: Context, db: StappenplanDatabase){
             DummyDataSource().getStappenplannen().map {
                it?.let {
                    val planId = it.id
                    val stappenFromPlan = mutableListOf<StapEntity>()
                     it.stappen.forEach {
                         var stap = it.stapDomainToDB()
                         stap.stappenplanId = planId
                         stappenFromPlan.add(stap)
                     }
                    db.stappenplanDao.insertStappenplanWithStappen(it.stappenplanDomainToDB(), stappenFromPlan)
                }
            }
        }
    }
}