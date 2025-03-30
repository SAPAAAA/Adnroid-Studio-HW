package com.example.android.architecture.blueprints.todoapp.tasks

import android.os.Bundle
import androidx.navigation.NavArgs
import kotlin.Int
import kotlin.jvm.JvmStatic

public data class TasksFragmentArgs(
  public val userMessage: Int = 0
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putInt("userMessage", this.userMessage)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): TasksFragmentArgs {
      bundle.setClassLoader(TasksFragmentArgs::class.java.classLoader)
      val __userMessage : Int
      if (bundle.containsKey("userMessage")) {
        __userMessage = bundle.getInt("userMessage")
      } else {
        __userMessage = 0
      }
      return TasksFragmentArgs(__userMessage)
    }
  }
}
