Should be updated to use Docker

to the moment current docker-compose set extra services on localhost

.env files support required for config (not available)

right now uses localhost as simulation of "infinite" horizontal scalable services,
took that option vs replication with kafka

- PSQL database
- Redis cache
- rabbitMQ for tasks distribution

tasks distribution isn't properly tested but theory is that

node A check country value on cache or DB for IIN
if no country value verifies to be under external service limit
a) under limit
   takes info from external service
b) past the limit
   enqueue the IIN to be executed by node B (any other node under limit)
   B returns country to A

with country value A returns costs

CRUD operations

GET PUT DELETE /costs/<country_code>

PUT payload
{
 "cost": <decimal>
}

PUT executes an UPSERT

POST /costs

POST payload
{
 "country": <alpha2 code>
 "cost": <decimal>
}

post executes UPSERT

there is /costs/default

to GET and PUT default costs
{
 "cost": <decimal>
}

GET for country mapping to default returns a normal payload
{
 "country": <alpha2 code>
 "cost": <decimal>
}

but dont create cache or deb registers

lockRequests() is intended to disable external call for 1 hour (external time limit) if error 429 is returned
should never receive a 429 message cause uses counter or calls on time limit but is to be save

error handling required, just takes simple responses for the moment

- messaging was between, kafka, redis messages, rabbitMQ, or a combination