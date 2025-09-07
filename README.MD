# used for testing failover & loadbalancing of spring cloud 

# how to use:
1. start the spring cloud eureka server (eureka-registry module)
2. start 2 instances of the api: 1 one without profile, one with profile "failover"
3. start the client (web-client module)
   -> go to http://localhost:8079/client/count
   -> each time you refresh the page the api is called en prints "$profile: $counter"
   -> using round robin loadbalancing it should go from one api to the other so it should print:
      ```
      No active profile found : 1
      failover: 1
      No active profile found : 2
      failover: 2
      No active profile found : 3
      failover: 3
      ...
      ```
   
Then you can expriment 
what happens if you take one api client down 
what if it comes back up 
...
