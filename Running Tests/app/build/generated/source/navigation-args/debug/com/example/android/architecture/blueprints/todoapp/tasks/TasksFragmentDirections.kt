package com.example.android.architecture.blueprints.todoapp.tasks

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.android.architecture.blueprints.todoapp.R
import kotlin.Int
import kotlin.String

public class TasksFragmentDirections private constructor() {
  private data class ActionTasksFragmentToTaskDetailFragment(
    public val taskId: String
  ) : NavDirections {
    public override fun getActionId(): Int = R.id.action_tasksFragment_to_taskDetailFragment

    public override fun getArguments(): Bundle {
      val result = Bundle()
      result.putString("taskId", this.taskId)
      return result
    }
  }

  private data class ActionTasksFragmentToAddEditTaskFragment(
    public val taskId: String?,
    public val title: String
  ) : NavDirections {
    public override fun getActionId(): Int = R.id.action_tasksFragment_to_addEditTaskFragment

    public override fun getArguments(): Bundle {
      val result = Bundle()
      result.putString("taskId", this.taskId)
      result.putString("title", this.title)
      return result
    }
  }

  public companion object {
    public fun actionTasksFragmentToStatisticsFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_tasksFragment_to_statisticsFragment)

    public fun actionTasksFragmentToTaskDetailFragment(taskId: String): NavDirections =
        ActionTasksFragmentToTaskDetailFragment(taskId)

    public fun actionTasksFragmentToAddEditTaskFragment(taskId: String?, title: String):
        NavDirections = ActionTasksFragmentToAddEditTaskFragment(taskId, title)
  }
}
