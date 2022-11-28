package com.maveric.accountservice.util;

import java.time.LocalDateTime;

public class Common {
    private Common()
    {

    }
    public static LocalDateTime getCurrentDateTime() {
        return (LocalDateTime.now());
    }
}
