package com.mindex.challenge.controller;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportingController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingController.class);

    @Autowired
    private ReportingStructureService reportingStructureService;

    @GetMapping("/reporting/{employeeId}")
    public ReportingStructure getReportingStructure(@PathVariable String employeeId) {
        LOG.debug("Received getReportingStructure for id [{}]", employeeId);
        return reportingStructureService.getReportingStructure(employeeId);
    }


}