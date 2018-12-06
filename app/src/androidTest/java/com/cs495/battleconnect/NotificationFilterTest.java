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

/**
 * This class tests the ability to filter alerts.
 */
@RunWith(AndroidJUnit4.class)
public class NotificationFilterTest {
    List<NotificationResponse> notifications = new ArrayList();
    private Context instrumentationCtx;
    NotificationAdapter adapter;

    /**
     * Add example alerts.
     */
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

    /**
     * Test filtering by low priority.
     */
    @Test
    public void low(){
        adapter.filter("LOW");
        assertEquals(adapter.getFilteredList().size(), 1);
        assertEquals(adapter.getFilteredList().get(0).getPriority(), "LOW");
    }

    /**
     * Test filtering by medium priority.
     */
    @Test
    public void medium(){
        adapter.filter("MEDIUM");
        assertEquals(adapter.getFilteredList().size(), 4);
        assertEquals(adapter.getFilteredList().get(0).getPriority(), "MEDIUM");
        assertEquals(adapter.getFilteredList().get(1).getPriority(), "MEDIUM");
        assertEquals(adapter.getFilteredList().get(2).getPriority(), "MEDIUM");
        assertEquals(adapter.getFilteredList().get(3).getPriority(), "MEDIUM");


    }

    /**
     * Test filtering by high priority.
     */
    @Test
    public void high(){
        adapter.filter("HIGH");
        assertEquals(adapter.getFilteredList().size(), 1);
        assertEquals(adapter.getFilteredList().get(0).getPriority(), "HIGH");

    }

    /**
     * Test filtering by critical priority.
     */
    @Test
    public void critical(){
        adapter.filter("CRITICAL");
        assertEquals(adapter.getFilteredList().size(), 1);
        assertEquals(adapter.getFilteredList().get(0).getPriority(), "CRITICAL");

    }


    /**
     * Tests that all the alerts are shown when no priority is selected.
     */
    @Test
    public void none(){
        adapter.filter("NONE");
        assertEquals(adapter.getFilteredList().size(), 7);
    }

}
