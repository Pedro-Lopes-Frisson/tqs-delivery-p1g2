Feature: Manage the statistics from Delivera

    Scenario: Admin signs in successfully
        When I access "http://localhost:3000"
        Then I fill in the sign in with the necessary information
        And I click Sign In
        And I should be redirected to the "Dashboard" page