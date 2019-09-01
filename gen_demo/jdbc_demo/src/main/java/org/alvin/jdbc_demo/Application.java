package org.alvin.jdbc_demo;

import org.alvin.mini_inject.InjectContext;
import org.alvin.mini_inject.annotations.MiniServiceScan;

@MiniServiceScan("org.alvin.jdbc_demo")
public class Application {

    public static void main(String[] args) {
        InjectContext.run(Application.class, args);
    }
}
