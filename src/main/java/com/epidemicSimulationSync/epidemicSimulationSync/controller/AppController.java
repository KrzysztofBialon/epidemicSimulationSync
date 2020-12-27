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
import org.bson.types.ObjectId;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RepositoryRestController
@RequestMapping("/simulation")
@Validated
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
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<EntityModel<SimulationRecord>> createSimulation(
            @Valid @RequestBody SimulationSetUp setUpBody, Errors errors)
    {
        if(errors.hasErrors())
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        List<SimulationDay> list = new ArrayList<>();
        SimulationSetUp setUp = setUpBody;
        setUp.setId(new ObjectId());
        SimulationDay firstDay = new SimulationDay(setUp.getI(), setUp.getP()- setUp.getI(), 0, 0);
        Simulation simulation = new Simulation(setUp);

        list.add(firstDay);

        for(int i = 0; i< setUp.getTs(); i++)
        {
            list.add(simulation.calculate(setUp.getTm(), setUp.getTi(), list));
        }

        simulationSetUpService.save(setUp);

        SimulationRecord simulationRecord = simulationRecordService.save(new SimulationRecord(setUp.getId().toHexString(), list));

        Link self =
                linkTo(AppController.class).slash("simulationRecords").slash(simulationRecord.getId()).withSelfRel();
        Link recordOwnerLink =
                linkTo(AppController.class).slash("simulationSetUps").slash(setUp.getId()).withRel("simulationSetUp");
        Link dayLink =
                linkTo(AppController.class).slash("/simulationRecords/{id}/simulationDays/{day}").withRel("simulationDay");

        EntityModel<SimulationRecord> entityModel = EntityModel.of(simulationRecord, recordOwnerLink, self, dayLink);

        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @GetMapping(value = "/simulationRecords/{id}/simulationDays/{day}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<SimulationDay>> getDay(@PathVariable(value = "id") String id, @PathVariable(value = "day") int day)
    {
        SimulationDay simulationDay = simulationRecordService.getById(id).getRecords().get(day);

        if(simulationDay.equals(null))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Link selfLink =
                linkTo(methodOn(AppController.class).getDay(id, day)).withSelfRel();
        Link recordLink =
                linkTo(AppController.class).slash("simulationRecords").slash(id).withRel("simulationRecord");
        Link allLink =
                linkTo(AppController.class).slash("simulationRecords").withRel("simulationRecords");

        EntityModel entityModel =
                EntityModel.of(simulationRecordService.getById(id).getRecords().get(day), selfLink, recordLink, allLink);

        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }
}
