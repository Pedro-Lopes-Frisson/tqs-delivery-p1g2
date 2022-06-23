Feature: Order an ice cream from Frostini

    Background: User accesses website
        Given I access "http://localhost:3000"

    Scenario: User register successfully
        When I navigate to the User register page
        Then I fill in the necessary information
        And I click Register
        And I should see a popup with "Register successfully"

    Scenario: User login successfully
        When I navigate to the User login page
        Then I fill in the login necessary information
        And I click Login
        And I should be redirected to the "Menu" page

    Scenario: User makes an order
        When I navigate to the Menu page
        Then I add a product to the order
        Then I click "Confirm New Order"
        Then I click "Confirm Order"
        Then I click "Menu"
        Then I go to "My Orders"
        Then I click "Check More About The Order"
        Then I insert the credit car information
        And I click "Confirm Payment"
        

