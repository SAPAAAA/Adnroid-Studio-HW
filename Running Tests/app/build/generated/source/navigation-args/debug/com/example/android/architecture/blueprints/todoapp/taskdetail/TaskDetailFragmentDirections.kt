package com.example.android.architecture.blueprints.todoapp.taskdetail

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.android.architecture.blueprints.todoapp.R
import kotlin.Int
import kotlin.String

public class TaskDetailFragmentDirections private constructor() {
  private data class ActionTaskDetailFragmentToAddEditTaskFragment(
    public val taskId: String?,
    public val title: String
  ) : NavDirections {
    public override fun getActionId(): Int = R.id.action_taskDetailFragment_to_addEditTaskFragment

    public override fun getArguments(): Bundle {
      val result = Bundle()
      result.putString("taskId", this.taskId)
      result.putString("title", this.title)
      return result
    }
  }

  private data class ActionTaskDetailFragmentToTasksFragment(
    public val userMessage: Int = 0
  ) : NavDirections {
    public override fun getActionId(): Int = R.id.action_taskDetailFragment_to_tasksFragment

    public override fun getArguments(): Bundle {
      val result = Bundle()
      result.putInt("userMessage", this.userMessage)
      return result
    }
  }

  public companion object {
    public fun actionTaskDetailFragmentToAddEditTaskFragment(taskId: String?, title: String):
        NavDirections = ActionTaskDetailFragmentToAddEditTaskFragment(taskId, title)

    public fun actionTaskDetailFragmentToTasksFragment(userMessage: Int = 0): NavDirections =
        ActionTaskDetailFragmentToTasksFragment(userMessage)
  }
}
