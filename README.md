# REST API with Scala, Akka HTTP, Slick, PostgreSQL and Redis

#### Database creation

```
createdb rest_api_app -U postgres
```

```
cd /where/this/activator/template/is/located/
psql rest_api_app -U postgres -f ./src/main/resources/init.sql
```

### Running

Before starting anything make sure you have PostgreSQL and Redis up and running.