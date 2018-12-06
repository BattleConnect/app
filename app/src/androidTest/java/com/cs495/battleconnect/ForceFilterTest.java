package com.cs495.battleconnect;

import android.support.test.runner.AndroidJUnit4;

import com.cs495.battleconnect.adapters.ForceAdapter;
import com.cs495.battleconnect.adapters.SensorAdapter;
import com.cs495.battleconnect.fragments.ForceRecyclerViewFragment;
import com.cs495.battleconnect.fragments.SensorRecyclerViewFragment;
import com.cs495.battleconnect.responses.ForceResponse;
import com.cs495.battleconnect.responses.SensorResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Tests the ability to filter force data when exploring force data.
 */
@RunWith(AndroidJUnit4.class)
public class ForceFilterTest {
    ForceRecyclerViewFragment fragment;
    List<ForceResponse> forceList = new ArrayList();
    List<String> filterList = new ArrayList();
    ForceAdapter adapter;

    /**
     * Adds example force data.
     */
    @Before
    public void setUp(){
        fragment = new ForceRecyclerViewFragment();

        forceList.add(new ForceResponse(1541060296, 33.81604, 66.7767, "85", "Platoon","platoon1", "Under fire"));
        forceList.add(new ForceResponse(341060296, 33.81604, 66.7767, "83", "Squad","squad3", "Moving towards target"));
        forceList.add(new ForceResponse(154143296, 33.81604, 66.7767, "1", "Preplanned Target","target1", "Captured"));
        forceList.add(new ForceResponse(1569370296, 33.81604, 66.7767, "2", "Enemy Unit","enemy2", "Standing still"));
        forceList.add(new ForceResponse(1519365296, 33.81604, 66.7767, "38", "Company HQ","Command Center", "Under fire"));
        forceList.add(new ForceResponse(341060296, 33.81604, 66.7767, "26", "Squad","squad1", "On standby"));
        forceList.add(new ForceResponse(1519365296, 33.81604, 66.7767, "99", "Squad","squad2", "Under fire"));
        forceList.add(new ForceResponse(1519365296, 33.81604, 66.7767, "105", "Preplanned Target","target2", "In contention"));
        forceList.add(new ForceResponse(154143296, 33.81604, 66.7767, "2245", "Enemy Unit","enemy1", "Mobilizing"));

        adapter = new ForceAdapter(fragment, forceList);

    }

    /**
     * Tests filtering for the company HQ.
     */
    @Test
    public void testCompanyHQ(){
        filterList.add("Company HQ");
        adapter.filter(filterList);
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getForce_Type(), "Company HQ");
    }

    /**
     * Tests filtering for platoons.
     */
    @Test
    public void testPlatoon(){
        filterList.add("Platoon");
        adapter.filter(filterList);
        assertEquals(1, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getForce_Type(), "Platoon");
    }

    /**
     * Tests filtering for squads.
     */
    @Test
    public void testSquad(){
        filterList.add("Squad");
        adapter.filter(filterList);
        assertEquals(3, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getForce_Type(), "Squad");
        assertEquals(adapter.getFilteredList().get(1).getForce_Type(), "Squad");
        assertEquals(adapter.getFilteredList().get(2).getForce_Type(), "Squad");
    }

    /**
     * Tests filtering for preplanned targets.
     */
    @Test
    public void testTarget(){
        filterList.add("Preplanned Target");
        adapter.filter(filterList);
        assertEquals(2, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getForce_Type(), "Preplanned Target");
        assertEquals(adapter.getFilteredList().get(0).getForce_Type(), "Preplanned Target");

    }

    /**
     * Tests filtering for enemy units.
     */
    @Test
    public void testEnemy(){
        filterList.add("Enemy Unit");
        adapter.filter(filterList);
        assertEquals(2, adapter.getFilteredList().size());
        assertEquals(adapter.getFilteredList().get(0).getForce_Type(), "Enemy Unit");
        assertEquals(adapter.getFilteredList().get(1).getForce_Type(), "Enemy Unit");
    }
}