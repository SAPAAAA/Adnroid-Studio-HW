package com.example.todolist.di

import android.content.Context
import com.example.todolist.data.local.dao.TaskDao
import com.example.todolist.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent // Scope for Application-level singletons
import javax.inject.Singleton

/**
 * Hilt Module responsible for providing database-related instances.
 * Installed in SingletonComponent means these dependencies live as long as the application.
 */
@Module
@InstallIn(SingletonComponent::class) // Provides dependencies for the Application scope
object DatabaseModule { // Use 'object' for modules with only @Provides functions
	
	/**
	 * Provides a singleton instance of the AppDatabase.
	 * @param appContext The application context provided by Hilt.
	 * @return A singleton AppDatabase instance.
	 */
	@Provides
	@Singleton // Ensure only one instance of the database exists for the app lifecycle
	fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
		// Use the getDatabase method previously defined in AppDatabase companion object
		return AppDatabase.getInstance(appContext)
	}
	
	/**
	 * Provides an instance of the TaskDao.
	 * @param appDatabase The singleton AppDatabase instance provided by Hilt.
	 * @return An instance of TaskDao.
	 */
	@Provides // No need for @Singleton here, as it's tied to the singleton AppDatabase
	fun provideTaskDao(appDatabase: AppDatabase): TaskDao {
		return appDatabase.taskDao()
	}
}