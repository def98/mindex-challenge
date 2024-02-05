package com.mindex.challenge.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private static final String DATASTORE_LOCATION = "src/main/resources/static/employee_database.json";

    @Autowired
    private ReportingStructureService reportingStructureService;

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetReportingStructure() {
        
        Employee testEmployeeL1 = new Employee();
        testEmployeeL1.setFirstName("Emily");
        testEmployeeL1.setLastName("Doe");
        testEmployeeL1.setDepartment("Engineering");
        testEmployeeL1.setPosition("Developer");

        Employee testEmployeeL2 = new Employee();
        testEmployeeL2.setFirstName("Danielle");

        
        final List<Employee> emptyList = new ArrayList<Employee>();

        testEmployeeL1 = employeeService.create(testEmployeeL1);
        
        testEmployeeL2.setDirectReports(emptyList);
        testEmployeeL2.getDirectReports().add(testEmployeeL1);
        testEmployeeL2 = employeeService.create(testEmployeeL2);

        ReportingStructure response = new ReportingStructure();
        response = reportingStructureService.getReportingStructure(testEmployeeL2.getEmployeeId());

        assertNotNull(response.getEmployee());
        assertEquals(response.getEmployee().getEmployeeId(), testEmployeeL2.getEmployeeId());
        assertEquals(response.getNumberOfReports(), 1);
    }

    @Test
    public void testMultitierReports() {
        File initialFile = new File(DATASTORE_LOCATION);

        Employee[] employees = null;

        try {
            InputStream targetStream = new FileInputStream(initialFile);
            employees = objectMapper.readValue(targetStream, Employee[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Employee employee : employees) {
            employeeService.create(employee);
        }

        ReportingStructure response = reportingStructureService.getReportingStructure("16a596ae-edd3-4847-99fe-c4518e82c86f");
        assertEquals(response.getNumberOfReports(), 4);
    }
}
