package org.example.template.repository;

import org.example.template.domain.Placeholder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBAsyncRepository extends DBRepository{
    ExecutorService executor = Executors.newCachedThreadPool();

    public CompletableFuture<Placeholder> findByID(Integer ID) {
        return CompletableFuture.supplyAsync(() -> super.findById(ID), executor);
    }

    public CompletableFuture<List<Placeholder>> findAllAsync() {
        return CompletableFuture.supplyAsync(super::findAll, executor);
    }

// Mod de utilizare:
//    repo.findById(1)
//            .thenAccept(placeholder -> {
//        if (placeholder != null) {
//            Codul pentru cand este gasit obiectul
//        }
//    })
//            .exceptionally(ex -> {
//        Codul pentru cand nu este gasit obiectul
//        return null;
//    });
}
