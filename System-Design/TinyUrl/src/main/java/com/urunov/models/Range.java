package com.urunov.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@Component
public class Range {

    private long start;
    private long end;

    @Autowired
    public Range(@Value("0") long start, @Value("0") long end)
    {
        this.start = start;
        this.end = end;
    }

    public long getStart(){
        return start;
    }

    public long getEnd(){
        return end;
    }

    public void finalize(){
        System.out.println("I am going to end" + this.toString());
    }
}
