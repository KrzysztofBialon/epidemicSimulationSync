package com.epidemicSimulationSync.epidemicSimulationSync.service;

import com.epidemicSimulationSync.epidemicSimulationSync.model.SimulationRecord;
import com.epidemicSimulationSync.epidemicSimulationSync.model.SimulationSetUp;
import com.epidemicSimulationSync.epidemicSimulationSync.repository.RecordRepository;
import com.epidemicSimulationSync.epidemicSimulationSync.repository.SimulationRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimulationRecordService
{
    private final RecordRepository repository;

    @Autowired
    public SimulationRecordService(RecordRepository repository)
    {
        this.repository = repository;
    }
    public SimulationRecord getById(String id)
    {
        return repository.findById(id).get();
    }
    public SimulationRecord save(SimulationRecord record)
    {
        return repository.save(record);
    }
    public SimulationRecord getSimulationRecordByOwnerId(String id)
    {
        return repository.findByOwnerId(id);
    }
}
