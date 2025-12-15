Feature: OrangeHRM Login

  Scenario: Valid login with correct credentials
    Given user is on OrangeHRM login page
    When user enters username and password and click login
    Then dashboard should be displayed
 	