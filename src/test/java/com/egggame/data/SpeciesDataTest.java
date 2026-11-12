package com.egggame.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpeciesDataTest {
    @Test
    void speciesData_containsAllSpecies() {
        SpeciesData data = new SpeciesData();
        assertEquals(10, data.getTotalCount());
    }
}