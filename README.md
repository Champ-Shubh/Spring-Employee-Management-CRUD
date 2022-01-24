# Employee/Department Management System (using Sprint Boot)

## Entities
* Employee
    * name
    * email (UNIQUE)
    * designation
    * age
    * department (nullable, mapped by FK department_id)
    
* Department
    * departmentName
    * departmentCode

(IDs for both the entities are auto-generated)

### Base URL
_localhost:8080_

### Accessing the APIs (for Employee)
   #### GET requests
   * <i>Fetch list of all employees</i> : `/api/v1/employee`
   
   #### POST requests
   * <i>Add new employee</i> : `/api/v1/employee` (<b>Request body</b> contains Employee entity)
   * <i>Add new employee with department - {deptId}</i> : `/api/v1/emloyee/{deptId}`
   
   #### DELETE requests
   * <i>Delete employee with id - {employeeId}</i> : `/api/v1/employee/{employeeId}`
   
   #### PUT requests
   * <i>Update employee with id - {employeeId}</i> : `/api/v1/employee/{employeeId}`   (<b>Request body</b> contains Employee entity, except the department field (not updatable))
   * <i>Assign a department to employee - {employeeId}</i> : `/api/v1/employee/{employeeId}/assign`    (<b>Request param</b> :-- departmentId)

### Accessing the APIs (for Department)
   #### GET requests
   * <i>Fetch list of all departments</i> : `/api/v1/department`
   * <i>Fetch list of all employees in department - {departmentId}</i> : `/api/v1/department/{departmentId}`
   
   #### POST requests
   * <i>Add new department</i> : `/api/v1/department` (<b>Request body</b> contains Department entity)
   
   #### DELETE requests
   * <i>Delete department with id - {departmentId}</i> : `/api/v1/department/{departmentId}`
   
   #### PUT requests
   * <i>Update department with id - {departmentId}</i> : `/api/v1/department/{departmentId}`   (<b>Request body</b> contains Department entity)

   
   