package edu.byu.cs.tweeter.client.model.service.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Service {
    protected ExecutorService executor;

    //TODO use a threadpool?
    public Service() {
        executor = Executors.newSingleThreadExecutor();
    }
}
