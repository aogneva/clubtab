# clubtab
Clubtab is a simple universal application helping to schedule some events and register customers to them. 
Applied technologies:
* Spring Boot
* PostgreSQL
* Hibernate
* Liquibase scripts in *.yaml
* JUnit
* MockMvc
* Hamcrest

Events scheduled in clubtab are called service types. There are some examples of service types: 
* manicure session 
* yoga-class 
* meeting 
* tutor lessons... 

*Slot* is a unit of schedule. It has attributes: 
* state
* service type
* start date and time
* duration
* capacity (max count of registered people)
* executor
* customers
 
Both *executors* and *customers* are united in entity *Person* at this time. Entity *Person* contains information of: 
* first name
* second name 
* last name 
* phone

