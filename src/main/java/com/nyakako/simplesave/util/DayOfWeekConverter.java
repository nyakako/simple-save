package com.nyakako.simplesave.util;

import java.time.DayOfWeek;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DayOfWeekConverter implements AttributeConverter<DayOfWeek,String> {
    @Override
    public String convertToDatabaseColumn(DayOfWeek dayOfWeek) {
        if (dayOfWeek == null) {
            return null;
        }

        return dayOfWeek.toString();
    }

    @Override
    public DayOfWeek convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return DayOfWeek.valueOf(dbData);
     }
}

