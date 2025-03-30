package com.example.android.architecture.blueprints.todoapp.statistics

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.android.architecture.blueprints.todoapp.R
import kotlin.Int

public class StatisticsFragmentDirections private constructor() {
  private data class ActionStatisticsFragmentToTasksFragment(
    public val userMessage: Int = 0
  ) : NavDirections {
    public override fun getActionId(): Int = R.id.action_statisticsFragment_to_tasksFragment

    public override fun getArguments(): Bundle {
      val result = Bundle()
      result.putInt("userMessage", this.userMessage)
      return result
    }
  }

  public companion object {
    public fun actionStatisticsFragmentToTasksFragment(userMessage: Int = 0): NavDirections =
        ActionStatisticsFragmentToTasksFragment(userMessage)
  }
}
