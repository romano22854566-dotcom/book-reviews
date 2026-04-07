package com.example.bookreviews.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ConcurrencyService {

    // 1. Небезопасный счетчик (для демонстрации Race Condition)
    private int unsafeCounter = 0;

    // 2. Безопасные счетчики
    private int syncCounter = 0;
    private final AtomicInteger atomicCounter = new AtomicInteger(0);

    public void resetCounters() {
        unsafeCounter = 0;
        syncCounter = 0;
        atomicCounter.set(0);
    }

    // Обычное прибавление (вызовет потерю данных при многопоточности)
    public void incrementUnsafe() {
        unsafeCounter++;
    }

    // Решение №1: Ключевое слово synchronized (выстраивает потоки в очередь)
    public synchronized void incrementSync() {
        syncCounter++;
    }

    // Решение №2: Использование атомарной переменной (работает на уровне процессора)
    public void incrementAtomic() {
        atomicCounter.incrementAndGet();
    }

    public int getUnsafeCounter() {
        return unsafeCounter;
    }

    public int getSyncCounter() {
        return syncCounter;
    }

    public int getAtomicCounter() {
        return atomicCounter.get();
    }
}