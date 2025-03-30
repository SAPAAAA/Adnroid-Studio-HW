package com.example.android.architecture.blueprints.todoapp;

import java.lang.System;

/**
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 *
 * Also, sets up Timber in the DEBUG BuildConfig. Read Timber's documentation for production setups.
 */
@kotlin.Metadata(mv = {1, 5, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016\u00a8\u0006\u0005"}, d2 = {"Lcom/example/android/architecture/blueprints/todoapp/TodoApplication;", "Landroid/app/Application;", "()V", "onCreate", "", "app_debug"})
public final class TodoApplication extends android.app.Application {
    
    public TodoApplication() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
}