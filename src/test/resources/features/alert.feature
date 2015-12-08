Feature:
  As a Maxx restful service user,
  I want to use Maxx restful service to save and search alert trigger.
  So that I can notify trader/dealer when the price hit the target price.

  We break into 3 parts: Create a new alert, Update an alert, Delete an alert.

  @cleanup
  Scenario: As a Maxx restful service user, I want to use Maxx restful service call to save alert trigger.
    Given maxx user is "Johndoe", status is "WORKING", symbol is "EUR/USD", side is "BID", price is "1.2345", firm is "UOBSG", delivery is "SMS", created is "2014-11-01T13:23:12Z", expired is "2020-11-01T13:23:12Z"
    When the alert client post http post message to Maxx restful service
    Then the alert client receives http status code of 200 and Json body with "id"
    And the Maxx restful service database saved alert with Maxx user "Johndoe", status is "WORKING", side is "BID", price is "1.2345" and currency is "EUR/USD"
    When the alert client fire http get message to "/alert" with Maxx user "Johndoe"
    Then the alert server response should be maxx user is "Johndoe", status is "WORKING", side is "BID", price is "1.2345", currency is "EUR/USD" and trigger time is ""

    When the alert client fire http POST message to "/alert" with the new status is "TRIGGERED", trigger time is "2015-11-01T13:23:12Z"
    When the alert client fire http get message to "/alert" with Maxx user "Johndoe"
    Then the alert server response should be maxx user is "Johndoe", status is "TRIGGERED", side is "BID", price is "1.2345", currency is "EUR/USD" and trigger time is "2015-11-01T13:23:12Z"

    When the alert client fire http DELETE to delete the current alert
    When the alert client fire http get message to "/alert" with Maxx user "Johndoe"
    Then the alert server response should be empty
