# leeds-tech-coding-test
## Background
Schedulers at ITV have expressed a desire to be able to quickly calculate the cost of their scheduling decisions and help them make better decisions.

To this end the solutions architect has proposed a simple tool that can be provided with basic information on programmes and schedule entries in order to calculate the costs.

This will take the form of an API service consuming the relevant data and providing the calculation functionality and a simple web based client that will allow users to perform costing.

## Requirements
The schedule is the intention for linear playout, listing what programme is to be played out at what time, for how long and what promotions or adverts are in between. As the tool is just for costing programming decisions it can be limited to just programme schedule entries, without add breaks matching the whole slot which is a multiple of 30 mins (i.e. 30, 60, 90 mins etc.)
A programme can be present in the schedule 1 or more times, the schedule entry specifies the programme and time slot it is to be shown
Slots for schedule entries are on the 15 minute
So 13:00 for 1hr, 13:15 for 1hr, 13:15 for 30mins, 13:45 for 90mins would all be valid

The tool should be able to take programmes and schedule entries as input and allow costings to be calculated over a specific range

All data for this purpose, programmes and schedule entries, are mastered elsewhere so we can assume the id's, titles and other data are all provided

Each programme has an associated playout cost which should be used in the calculation of schedule costings. Also multiple scheduling of the same programme within a time window may result in discounted cost as follows
* a repeat within 3 days are free
* a repeat within 7 days are charged at 50%
* only 1 of each discount can be applied per full priced schedule
* discounts reset at each full priced schedule

so if a programme is scheduled every day for 4 consecutive days the first instance will be full price, second free, third 50% and fourth full price
programmes that can be discounted will be flagged appropriately in the data provided

An example of a schedule excerpt, limited to a few programmes could be
|programme|start|end|
|---|---|---|
|CEN34|01/08/2020 13:00|01/08/2020 14:00|
|YTV8083|01/08/2020 14:00|01/08/2020 14:30|
|ANG0022|01/08/2020 14:30|01/08/2020 15:00|
|GRA07548|01/08/2020 15:00|01/08/2020 16:00| first instance
...
|ANG0101|02/08/2020 21:30|02/08/2020 22:00|
|GRA07548|02/08/2020 22:00|02/08/2020 23:00| repeat in 3 days
...
|YTV8083|06/08/2020 06:30|06/08/2020 07:00| repeat in 7 days
|GRA07548|06/08/2020 07:00|06/08/2020 08:00| repeat in 7 days

## User stories

As a tool user
I can load programme and schedule data
So the tool is ready to calculate

As a tool user
I can request costing of the entire schedule

As a tool user
I can request costing of a single programme in the schedule

As a tool user
I can request costing of all programmes for a specific date range of the schedule

As a tool user
I can request costing of a single programme for a specific date range of the schedule

## Data
The service should provide REST endpoints for loading the following data
* Programme data with data model
  | | |
  |---|---|
  |Id|String|
  |Title|String|
  |Playout Cost|Integer|
  |Discountable|Boolean|
* Schedule entries with data model
  | | |
  |---|---|
  |Programme Id|String|
  |Start Date & Time|Timestamp|
  |End Date & Time|Timestamp|

## Scenarios
Some Examples of scenarios include

**Background** Single programme costing scenarios  
**Given** A programme is created with id CEN34
**And** It is discountable
**And** The playout cost is 500

**Scenario** No discount is applied for a single playout  
**Given** A schedule entry for 2021-06-13T12:00:00Z is CREATED for CEN34  
**When** A schedule request to GET programme CEN34 schedules between 2021-06-13T00:00:00Z and 2021-06-14T00:00:00Z is made  
**Then** The total playout cost is 500

**Scenario** 100% discount is applied for the 2nd playout within 3 days  
**Given** A schedule entry for 2021-06-13T12:00:00Z is CREATED for CEN34  
**And** A schedule entry for 2021-06-14T12:00:00Z is CREATED for CEN34  
**When** A schedule request to GET programme CEN34 schedules between 2021-06-13T00:00:00Z and 2021-06-15T00:00:00Z is made  
**Then** The total playout cost is 500

**Scenario** 50% discount is applied for the 2nd playout within 7 days but after 3 days  
**Given** A schedule entry for 2021-06-13T12:00:00Z is CREATED for CEN34  
**And** A schedule entry for 2021-06-17T12:00:00Z is CREATED for CEN34  
**When** A schedule request to GET programme CEN34 schedules between 2021-06-13T00:00:00Z and 2021-06-18T00:00:00Z is made  
**Then** The total playout cost is 750

**Scenario** The 100% and 50% discounts can both be applied once  
**Given** A schedule entry for 2021-06-13T12:00:00Z is CREATED for CEN34  
**And** A schedule entry for 2021-06-14T12:00:00Z is CREATED for CEN34  
**And** A schedule entry for 2021-06-17T12:00:00Z is CREATED for CEN34  
**When** A schedule request to GET programme CEN34 schedules between 2021-06-13T00:00:00Z and 2021-06-18T00:00:00Z is made  
**Then** The total playout cost is 750

**Scenario** A single discount cannot be reapplied more than once  
**Given** A schedule entry for 2021-06-13T12:00:00Z is CREATED for CEN34  
**And** A schedule entry for 2021-06-14T12:00:00Z is CREATED for CEN34  
**And** A schedule entry for 2021-06-15T12:00:00Z is CREATED for CEN34  
**And** A schedule entry for 2021-06-16T12:00:00Z is CREATED for CEN34  
**When** A schedule request to GET programme CEN34 schedules between 2021-06-13T00:00:00Z and 2021-06-17T00:00:00Z is made
**Then** The total playout cost is 1250
