package com.suru.springbatch.listenersapplication.listeners;


import org.springframework.batch.core.scope.context.ChunkContext;

public class MyChunkListener implements org.springframework.batch.core.ChunkListener {
    @Override
    public void beforeChunk(ChunkContext context) {
        System.out.println(">>> before chunk: " + context);
    }

    @Override
    public void afterChunk(ChunkContext context) {
        System.out.println(">>> after chunk: " + context);
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        System.out.println(">>> exception: " + context);
    }
}

/*
* we can use annotations insteadof implementing listener interface
* all annotated methods will have ChunkContext object as parameter
* @BeforeChunk
* @AfterChunkError
* @AfterChunk
* */