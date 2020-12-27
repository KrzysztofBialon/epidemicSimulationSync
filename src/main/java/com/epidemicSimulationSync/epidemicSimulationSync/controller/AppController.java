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
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RepositoryRestController
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
    public ResponseEntity<EntityModel<SimulationRecord>> createSimulation(@RequestBody JSONObject setUpBody) throws JsonProcessingException
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

        SimulationRecord simulationRecord = simulationRecordService.save(new SimulationRecord(setUp.getId().toHexString(), list));
        Link generalLink = linkTo(AppController.class).withSelfRel();
        Link recordOwnerLink =
                linkTo(AppController.class).slash("SimulationSetUps").slash(setUp.getId()).withSelfRel();
        Link self =
                linkTo(AppController.class).slash("SimulationRecords").slash(simulationRecord.getId()).withSelfRel();
        Link dayLink =
                linkTo(AppController.class).slash("/simulationRecords/'{id}'?day").withSelfRel();

        EntityModel<SimulationRecord> entityModel = EntityModel.of(simulationRecord, generalLink, recordOwnerLink, self, dayLink);

        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @GetMapping(value = "/simulationRecords/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimulationDay> getDay(@PathVariable(value = "id") String id, @RequestParam(value = "day") int day)
    {
        return new ResponseEntity<>(simulationRecordService.getById(id).getRecords().get(day), HttpStatus.FOUND);
    }
}
