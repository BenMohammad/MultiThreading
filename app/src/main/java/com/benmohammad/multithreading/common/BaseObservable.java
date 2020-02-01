package com.benmohammad.multithreading.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseObservable<LISTENER_CLASS> {

    private final Object MONITOR = new Object();
    private final Set<LISTENER_CLASS> mListeners = new HashSet<>();

    public void registerListener(LISTENER_CLASS listener) {
        synchronized (MONITOR) {
            boolean hadNoListener = mListeners.size() == 0;
            mListeners.add(listener);
            if(hadNoListener && mListeners.size() == 1) {
                onFirstListenerRegistered();
            }
        }
    }

    public void unregisterListner(LISTENER_CLASS listener) {
        synchronized (MONITOR) {
            boolean hadOneListener = mListeners.size() == 1;
            mListeners.remove(listener);
            if(hadOneListener && mListeners.size() == 0) {
                onLastListenerRegsitered();
            }
        }
    }

    public Set<LISTENER_CLASS> getListener() {
        synchronized (MONITOR) {
            return Collections.unmodifiableSet(new HashSet<>(mListeners));
        }
    }

    protected abstract void onFirstListenerRegistered();

    protected abstract void onLastListenerRegsitered();

}
