package com.example.android.architecture.blueprints.todoapp.tasks;

import java.lang.System;

/**
 * Display a grid of [Task]s. User can choose to view all, active or completed tasks.
 */
@kotlin.Metadata(mv = {1, 5, 1}, k = 1, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\u0018\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J$\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0018\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\u0010\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u0016J\u001a\u0010%\u001a\u00020\u00142\u0006\u0010&\u001a\u00020\u001b2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\u0010\u0010\'\u001a\u00020\u00142\u0006\u0010(\u001a\u00020)H\u0002J\b\u0010*\u001a\u00020\u0014H\u0002J\b\u0010+\u001a\u00020\u0014H\u0002J\b\u0010,\u001a\u00020\u0014H\u0002J\b\u0010-\u001a\u00020\u0014H\u0002J\b\u0010.\u001a\u00020\u0014H\u0002R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\r\u001a\u00020\u000e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006/"}, d2 = {"Lcom/example/android/architecture/blueprints/todoapp/tasks/TasksFragment;", "Landroidx/fragment/app/Fragment;", "()V", "args", "Lcom/example/android/architecture/blueprints/todoapp/tasks/TasksFragmentArgs;", "getArgs", "()Lcom/example/android/architecture/blueprints/todoapp/tasks/TasksFragmentArgs;", "args$delegate", "Landroidx/navigation/NavArgsLazy;", "listAdapter", "Lcom/example/android/architecture/blueprints/todoapp/tasks/TasksAdapter;", "viewDataBinding", "Lcom/example/android/architecture/blueprints/todoapp/databinding/TasksFragBinding;", "viewModel", "Lcom/example/android/architecture/blueprints/todoapp/tasks/TasksViewModel;", "getViewModel", "()Lcom/example/android/architecture/blueprints/todoapp/tasks/TasksViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "navigateToAddNewTask", "", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onCreateView", "Landroid/view/View;", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "onViewCreated", "view", "openTaskDetails", "taskId", "", "setupFab", "setupListAdapter", "setupNavigation", "setupSnackbar", "showFilteringPopUpMenu", "app_debug"})
public final class TasksFragment extends androidx.fragment.app.Fragment {
    private final kotlin.Lazy viewModel$delegate = null;
    private final androidx.navigation.NavArgsLazy args$delegate = null;
    private com.example.android.architecture.blueprints.todoapp.databinding.TasksFragBinding viewDataBinding;
    private com.example.android.architecture.blueprints.todoapp.tasks.TasksAdapter listAdapter;
    
    public TasksFragment() {
        super();
    }
    
    private final com.example.android.architecture.blueprints.todoapp.tasks.TasksViewModel getViewModel() {
        return null;
    }
    
    private final com.example.android.architecture.blueprints.todoapp.tasks.TasksFragmentArgs getArgs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    @java.lang.Override()
    public void onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu, @org.jetbrains.annotations.NotNull()
    android.view.MenuInflater inflater) {
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupNavigation() {
    }
    
    private final void setupSnackbar() {
    }
    
    private final void showFilteringPopUpMenu() {
    }
    
    private final void setupFab() {
    }
    
    private final void navigateToAddNewTask() {
    }
    
    private final void openTaskDetails(java.lang.String taskId) {
    }
    
    private final void setupListAdapter() {
    }
}