package com.fastshipmentsdev.backend_fastshipments.support.classi;

import java.time.LocalDateTime;

public class DateTime {

    LocalDateTime dataLocale;

    public LocalDateTime getDataLocale() {
        return dataLocale;
    }

    public void setDataLocale(LocalDateTime dataLocale) {
        this.dataLocale = dataLocale;
    }

    @Override
    public String toString() {
        return "DateTime{" +
                "dataLocale=" + dataLocale +
                '}';
    }
}
