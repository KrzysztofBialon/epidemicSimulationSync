package com.epidemicSimulationSync.epidemicSimulationSync.service;

import com.epidemicSimulationSync.epidemicSimulationSync.model.SimulationSetUp;
import com.epidemicSimulationSync.epidemicSimulationSync.repository.SimulationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SimulationSetUpService
{
    private final SimulationRepository repository;

    @Autowired
    public SimulationSetUpService(SimulationRepository repository)
    {
        this.repository = repository;
    }

    public void save(SimulationSetUp setUp)
    {
        repository.save(setUp);
    }
}
