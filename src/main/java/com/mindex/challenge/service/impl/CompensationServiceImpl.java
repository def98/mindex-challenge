package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);
        if(null == compensation.getEmployee()) {
            throw new RuntimeException("Request must include employee");
        } else if(null == compensation.getEmployee().getEmployeeId()) {
            throw new RuntimeException("Request must include employee ID");
        } else {
            compensationRepository.insert(compensation);
        }
        return compensation;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Creating compensation with id [{}]", id);
        Employee employee = employeeService.read(id);
        Compensation compensation = compensationRepository.findByEmployee(employee);

        return compensation;
    }

}
