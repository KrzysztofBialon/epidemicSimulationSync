package com.epidemicSimulationSync.epidemicSimulationSync.controller;

import com.epidemicSimulationSync.epidemicSimulationSync.model.SimulationDay;
import com.epidemicSimulationSync.epidemicSimulationSync.model.SimulationRecord;
import com.epidemicSimulationSync.epidemicSimulationSync.model.SimulationSetUp;
import com.epidemicSimulationSync.epidemicSimulationSync.service.SimulationRecordService;
import com.epidemicSimulationSync.epidemicSimulationSync.service.SimulationSetUpService;
import com.epidemicSimulationSync.epidemicSimulationSync.util.Simulation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/simulation")
public class AppController
{
    private ObjectMapper objectMapper;
    private SimulationSetUpService simulationSetUpService;
    private SimulationRecordService simulationRecordService;

    @Autowired
    public AppController(ObjectMapper objectMapper, SimulationSetUpService simulationSetUpService, SimulationRecordService simulationRecordService)
    {
        this.objectMapper = objectMapper;
        this.simulationSetUpService = simulationSetUpService;
        this.simulationRecordService = simulationRecordService;
    }

    @PostMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SimulationDay> createSimulation(@RequestBody JSONObject setUpBody) throws JsonProcessingException
    {
        List<SimulationDay> list = new ArrayList<>();
        SimulationSetUp setUp = objectMapper.readValue(setUpBody.toJSONString(), SimulationSetUp.class);
        SimulationDay firstDay = new SimulationDay(setUp.getI(), setUp.getP()- setUp.getI(), 0, 0);
        Simulation simulation = new Simulation(setUp);

        list.add(firstDay);

        for(int i = 0; i< setUp.getTs(); i++)
        {
            list.add(simulation.calculate(setUp.getTm(), setUp.getTi(), list));
        }

        simulationSetUpService.save(setUp);
        SimulationRecord simulationRecord = new SimulationRecord(setUp.getId().toHexString(), list);
        simulationRecordService.save(simulationRecord);

        return simulationRecordService.getSimulationRecordByOwnerId(setUp.getId().toHexString()).getRecords();
    }
}
