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

        _Example_ - `{
        "name": "some name here",
        "email": "unique email here",
        "designation": "some designation here",
        "age": __
        }`
   * <i>Add new employee with a department - {deptId}</i> : `/api/v1/emloyee/{deptId}`
   
   #### DELETE requests
   * <i>Delete employee with id - {employeeId}</i> : `/api/v1/employee/{employeeId}`
   
   #### PUT requests
   * <i>Update employee with id - {employeeId}</i> : `/api/v1/employee/{employeeId}`   (<b>Request body</b> contains Employee entity, except the department field (not updatable))
   * <i>Assign a department to employee - {employeeId}</i> : `/api/v1/employee/{employeeId}/assign`    (<b>Request param</b> :-- departmentId)
   
        _Param name_ - `department_id`
   
   #### Uploading file
   * Supported file format - CSV
   * <b>Request Body</b> contains file to be uploaded - `/api/v1/employee/upload`

### Accessing the APIs (for Department)
   #### GET requests
   * <i>Fetch list of all departments</i> : `/api/v1/department`
   * <i>Get single department</i> : `/api/v1/department/{departmentId}`
   * <i>Fetch list of all employees in department - {departmentId}</i> : `/api/v1/department/{departmentId}/employees`
   
   #### POST requests
   * <i>Add new department</i> : `/api/v1/department` (<b>Request body</b> contains Department entity)
   
        _Example_: `{
                "departmentName": "some name here",
                "departmentCode": "some code here"
                }`
   
   #### DELETE requests
   * <i>Delete department with id - {departmentId}</i> : `/api/v1/department/{departmentId}`
   
   #### PUT requests
   * <i>Update department with id - {departmentId}</i> : `/api/v1/department/{departmentId}`   (<b>Request body</b> contains Department entity)   

   