package com.epidemicSimulationSync.epidemicSimulationSync.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SimulationDay
{
    @JsonProperty(value = "pi")
    private final int pi; //number of infected
    @JsonProperty(value = "pv")
    private final int pv; //number of susceptible
    @JsonProperty(value = "pm")
    private final int pm; //number of death
    @JsonProperty(value = "pr")
    private final int pr; //number of recovered & resistant
}
