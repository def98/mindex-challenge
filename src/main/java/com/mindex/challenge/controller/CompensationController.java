package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    @PostMapping("/compensation")
    public Compensation create(@RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request for [{}]", compensation);
        Compensation responseCompensation;
        try {
            responseCompensation = compensationService.create(compensation);
        } catch (Exception e) {
            LOG.error("Failed to create compensation", e);
            responseCompensation = null;
        }
        return responseCompensation;
    }

    @GetMapping("/compensation/{id}")
    public Compensation read(@PathVariable String id) {
        LOG.debug("Received compensation create request for id [{}]", id);

        return compensationService.read(id);
    }
}
