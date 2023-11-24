package com.brokenkeyboard.usefulspyglass.platform;

import java.util.concurrent.ConcurrentMap;

public interface IPlatformHelper {
    
    ConcurrentMap<String, String> getModList();
}