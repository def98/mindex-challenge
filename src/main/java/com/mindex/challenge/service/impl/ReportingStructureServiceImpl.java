package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ReportingStructure getReportingStructure(String id) {
        LOG.debug("Getting Reporting structure [{}]", id);

        Employee mainEmployee = employeeService.read(id);

        ReportingStructure response = new ReportingStructure();
        response.setEmployee(mainEmployee);

        int numOfReports = 0;

        numOfReports = countDirectReports(mainEmployee);
        response.setNumberOfReports(numOfReports);
        return response;
    }

    private int countDirectReports(Employee nextReport) {
        int reportCount = 0;
        if(null != findDirectReports(nextReport)) {
            for(Employee report: nextReport.getDirectReports()) {
                // Ensure full info is present 
                Employee reportsFullInfo = employeeService.read(report.getEmployeeId());
                reportCount += 1;
                reportCount += countDirectReports(reportsFullInfo);
            }
        } 
        return reportCount;
    }

    private List<Employee> findDirectReports(Employee employee) {
        return employee.getDirectReports() != null ? 
            employee.getDirectReports() : null;
    }

}
