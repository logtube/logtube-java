package io.github.logtube.core.context;

import io.github.logtube.core.IEventContext;

public class NOPEventContext implements IEventContext {

    private static final NOPEventContext SINGLETON = new NOPEventContext();

    public static NOPEventContext getSingleton() {
        return SINGLETON;
    }

    private NOPEventContext() {
    }

    @Override
    public void restore() {
    }

}
