package com.cs495.battleconnect;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cs495.battleconnect.adapters.NotificationAdapter;
import com.cs495.battleconnect.responses.NotificationResponse;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NotificationSearchTest {
    List<NotificationResponse> notifications = new ArrayList();
    private Context instrumentationCtx;
    NotificationAdapter adapter;

    @Before
    public void setUp(){
        instrumentationCtx = InstrumentationRegistry.getContext();
        notifications.add(new NotificationResponse("16346", "Captain Kirk", "MEDIUM", "Incoming supply drop at 23:00"));
        notifications.add(new NotificationResponse("16346", "Captain Kirk", "MEDIUM", "Incoming Tank"));
        notifications.add(new NotificationResponse("5941", "Test", "MEDIUM", "This is a test"));
        notifications.add(new NotificationResponse("92362", "Sgt. Waters", "LOW", "Enemy force spotted south!"));
        notifications.add(new NotificationResponse("5941", "Test", "CRITICAL", "CRITICAL alert incoming!!!!"));
        notifications.add(new NotificationResponse("5941", "Test", "MEDIUM", "This is a new test"));
        notifications.add(new NotificationResponse("76264", "Private James", "HIGH", "Injured soldier and downed vehicle"));
        adapter = new NotificationAdapter(instrumentationCtx, notifications);
    }

    @Test
    public void low(){
        adapter.search("low");
        assertEquals(adapter.getFilteredList().size(), 1);
        assertEquals(adapter.getFilteredList().get(0).getPriority(), "LOW");
    }

    @Test
    public void medium(){
        adapter.search("medium");
        assertEquals(adapter.getFilteredList().size(), 4);
        assertEquals(adapter.getFilteredList().get(0).getPriority(), "MEDIUM");
        assertEquals(adapter.getFilteredList().get(1).getPriority(), "MEDIUM");
        assertEquals(adapter.getFilteredList().get(2).getPriority(), "MEDIUM");
        assertEquals(adapter.getFilteredList().get(3).getPriority(), "MEDIUM");


    }

    @Test
    public void high(){
        adapter.search("high");
        assertEquals(adapter.getFilteredList().size(), 1);
        assertEquals(adapter.getFilteredList().get(0).getPriority(), "HIGH");

    }

    @Test
    public void critical(){
        adapter.search("critical");
        assertEquals(adapter.getFilteredList().size(), 1);
        assertEquals(adapter.getFilteredList().get(0).getPriority(), "CRITICAL");

    }

}
