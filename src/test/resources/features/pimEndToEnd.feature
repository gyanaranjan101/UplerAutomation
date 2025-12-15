Feature: OrangeHRM Employee Lifecycle Management

  @EmployeeLifecycle
  Scenario: End to end employee lifecycle management
    Given user is on OrangeHRM login page
    When user enters username and password and click login
    Then dashboard should be displayed
    When user clicks on PIM module
    Then user navigates to PIM module
    When user clicks on Add button
    Then Validate user navigates to add user page
    When user enters employee details
    And user clicks on Save button
    Then Validate employee should be added successfully
    Then User moves to Employeelist page
    Then Validate employee should be added successfully in Backend
    Then User filter the employee using employeeid
    When User click on the edit button to update the user details
    Then Validate user moves to user details page
    When User clicks on the Job link
    And Select and update the Job Title and Employement Status
    Then Validate employee should be saved successfully
    Then User moves to Employeelist page
    Then User filter the employee using employeeid
    Then Validate updated data reflects to the BE for the user
    When User deletes the employee
    Then Validate employee should be deleted successfully from UI
    Then Validate employee is deleted from Backend
    Then User logs out from application
    Then Validate user is redirected to login page
    Then Validate backend session is invalidated


 	