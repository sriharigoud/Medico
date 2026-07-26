package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AddressEntity::class, PrescriptionEntity::class, OrderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MedicoDatabase : RoomDatabase() {
    abstract fun medicoDao(): MedicoDao

    companion object {
        @Volatile
        private var INSTANCE: MedicoDatabase? = null

        fun getDatabase(context: Context): MedicoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedicoDatabase::class.java,
                    "medico_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
