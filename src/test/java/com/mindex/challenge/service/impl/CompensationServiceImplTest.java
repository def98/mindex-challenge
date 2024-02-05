package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private CompensationService compensationService;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    
  @Rule
  public final ExpectedException exception = ExpectedException.none();
    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateRead() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee = employeeService.create(testEmployee);

        Compensation testComp = new Compensation();
        testComp.setEmployee(testEmployee);
        testComp.setSalary(256789.01);
        testComp.setEffectiveDate("10-03-2024");

        // Create check
         Compensation createdCompensation = restTemplate.postForEntity(
                compensationUrl, testComp, Compensation.class).getBody();

        assertNotNull(createdCompensation.getEmployee().getEmployeeId());


        // Read check
        Compensation readCompensation = restTemplate.getForEntity(
                compensationIdUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();
        assertEquals(createdCompensation.getEmployee().getFirstName(), readCompensation.getEmployee().getFirstName());
        assertEquals(createdCompensation.getEffectiveDate(), readCompensation.getEffectiveDate());
    } 

    @Test
    public void testCompensationAddWithNoId() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Emily");
        testEmployee.setLastName("Doe");

        Compensation testComp = new Compensation();
        testComp.setEmployee(testEmployee);
        testComp.setSalary(256789.01);
        testComp.setEffectiveDate("10-03-2024");

        Compensation createdCompensation;
        // Create check
        createdCompensation = restTemplate.postForEntity(
                compensationUrl, testComp, Compensation.class).getBody();
        
        assertEquals(null, createdCompensation);
    }

    @Test
    public void testCompensationAddWithNoEmployee() {
        Compensation testComp = new Compensation();
        testComp.setSalary(256789.01);
        testComp.setEffectiveDate("10-03-2024");

        assertThrows(RuntimeException.class, () -> {
            compensationService.create(testComp);
        });
    }

    @Test
    public void testInvalidCompensationRead() {
        Compensation testComp = new Compensation();
        testComp.setSalary(256789.01);
        testComp.setEffectiveDate("10-03-2024");
        Employee employee = new Employee();
        employee.setEmployeeId("fakeid");
        testComp.setEmployee(employee);

        assertThrows(RuntimeException.class, () -> {
            compensationService.read(testComp.getEmployee().getEmployeeId());
        });
    }
}
