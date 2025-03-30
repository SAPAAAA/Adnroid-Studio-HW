package com.example.android.architecture.blueprints.todoapp.data.source;

import java.lang.System;

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 */
@kotlin.Metadata(mv = {1, 5, 1}, k = 1, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 )2\u00020\u0001:\u0001)B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0019\u0010\n\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0011\u0010\u0012\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u0019\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0019\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0011\u0010\u0015\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u0019\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J)\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\r0\u00182\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0019\u001a\u00020\u001aH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bJ\u001f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\r0\u00182\u0006\u0010\u001d\u001a\u00020\u0010H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\'\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u001f0\u00182\b\b\u0002\u0010\u0019\u001a\u00020\u001aH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010 J\u001a\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00180\"2\u0006\u0010\u000f\u001a\u00020\u0010J\u0018\u0010#\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u001f0\u00180\"J\u0019\u0010$\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0011\u0010%\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u0019\u0010&\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0019\u0010\'\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0011\u0010(\u001a\u00020\u000bH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006*"}, d2 = {"Lcom/example/android/architecture/blueprints/todoapp/data/source/DefaultTasksRepository;", "", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "ioDispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "tasksLocalDataSource", "Lcom/example/android/architecture/blueprints/todoapp/data/source/TasksDataSource;", "tasksRemoteDataSource", "activateTask", "", "task", "Lcom/example/android/architecture/blueprints/todoapp/data/Task;", "(Lcom/example/android/architecture/blueprints/todoapp/data/Task;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "taskId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearCompletedTasks", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "completeTask", "deleteAllTasks", "deleteTask", "getTask", "Lcom/example/android/architecture/blueprints/todoapp/data/Result;", "forceUpdate", "", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTaskWithId", "id", "getTasks", "", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeTask", "Landroidx/lifecycle/LiveData;", "observeTasks", "refreshTask", "refreshTasks", "saveTask", "updateTaskFromRemoteDataSource", "updateTasksFromRemoteDataSource", "Companion", "app_debug"})
public final class DefaultTasksRepository {
    private final com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource tasksRemoteDataSource = null;
    private final com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource tasksLocalDataSource = null;
    private final kotlinx.coroutines.CoroutineDispatcher ioDispatcher = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository.Companion Companion = null;
    @kotlin.jvm.Volatile()
    private static volatile com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository INSTANCE;
    
    private DefaultTasksRepository(android.app.Application application) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTasks(boolean forceUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.android.architecture.blueprints.todoapp.data.Result<? extends java.util.List<com.example.android.architecture.blueprints.todoapp.data.Task>>> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object refreshTasks(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.android.architecture.blueprints.todoapp.data.Result<java.util.List<com.example.android.architecture.blueprints.todoapp.data.Task>>> observeTasks() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object refreshTask(@org.jetbrains.annotations.NotNull()
    java.lang.String taskId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final java.lang.Object updateTasksFromRemoteDataSource(kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.android.architecture.blueprints.todoapp.data.Result<com.example.android.architecture.blueprints.todoapp.data.Task>> observeTask(@org.jetbrains.annotations.NotNull()
    java.lang.String taskId) {
        return null;
    }
    
    private final java.lang.Object updateTaskFromRemoteDataSource(java.lang.String taskId, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * Relies on [getTasks] to fetch data and picks the task with the same ID.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTask(@org.jetbrains.annotations.NotNull()
    java.lang.String taskId, boolean forceUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.android.architecture.blueprints.todoapp.data.Result<com.example.android.architecture.blueprints.todoapp.data.Task>> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveTask(@org.jetbrains.annotations.NotNull()
    com.example.android.architecture.blueprints.todoapp.data.Task task, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object completeTask(@org.jetbrains.annotations.NotNull()
    com.example.android.architecture.blueprints.todoapp.data.Task task, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object completeTask(@org.jetbrains.annotations.NotNull()
    java.lang.String taskId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object activateTask(@org.jetbrains.annotations.NotNull()
    com.example.android.architecture.blueprints.todoapp.data.Task task, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object activateTask(@org.jetbrains.annotations.NotNull()
    java.lang.String taskId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clearCompletedTasks(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteAllTasks(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteTask(@org.jetbrains.annotations.NotNull()
    java.lang.String taskId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final java.lang.Object getTaskWithId(java.lang.String id, kotlin.coroutines.Continuation<? super com.example.android.architecture.blueprints.todoapp.data.Result<com.example.android.architecture.blueprints.todoapp.data.Task>> continuation) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 5, 1}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/android/architecture/blueprints/todoapp/data/source/DefaultTasksRepository$Companion;", "", "()V", "INSTANCE", "Lcom/example/android/architecture/blueprints/todoapp/data/source/DefaultTasksRepository;", "getRepository", "app", "Landroid/app/Application;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository getRepository(@org.jetbrains.annotations.NotNull()
        android.app.Application app) {
            return null;
        }
    }
}