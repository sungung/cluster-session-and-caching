# Java method caching & HTTP Session with REDIS

## Motivation
Cluster session with REDIS allows sharing session between micro-services. Server side caching and server side caching will improve performance to reduce database traffic.

## Getting Started
```
mvn spring-boot:run
```
## Prerequisites
Presume local redis sever installed
## Test

### Check redis session entry
* Create HTTP session with curl
```
curl -I -u user:password localhost:8080
```
* Check session in redis
```
redis-cli
127.0.0.1:6379> keys *
```
* Delete session

Create new session
```
$ curl -I -u user:password localhost:8080
Set-Cookie: SESSION=2378612d-b8da-448e-b2bd-daf100087ebe; Path=/; HttpOnly
```

Invoke API with given session
```
$ curl -H "Cookie: SESSION=2378612d-b8da-448e-b2bd-daf100087ebe" localhost:8080
Hello user
```

Get session belongs to user
```
$ curl -H "Cookie: SESSION=2378612d-b8da-448e-b2bd-daf100087ebe" localhost:8080/sessions
[{"id":"2378612d-b8da-448e-b2bd-daf100087ebe","creationTime":1516503694899,"attributeNames":["SESSION_DETAILS","SPRING_SECURITY_CONTEXT"],"new":false,"expired":false,"maxInactiveIntervalInSeconds":3600,"lastAccessedTime":1516503718139}]
```

Delete session
```
$ curl -H "Cookie: SESSION=2378612d-b8da-448e-b2bd-daf100087ebe" -XDELETE localhost:8080/sessions/2378612d-b8da-448e-b2bd-daf100087ebe
```

Session becomes invalidated
```
$ curl -H "Cookie: SESSION=2378612d-b8da-448e-b2bd-daf100087ebe" localhost:8080/sessions                     {"timestamp":1516503740628,"status":401,"error":"Unauthorized","message":"Full authentication is required to access this resource","path":"/sessions"}
```

### Test caching java method
Read live data will take a time
```
$ curl -I -u user:password localhost:8080/city/MEL -w %{time_total}
HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Set-Cookie: SESSION=de703247-7ba7-47f8-a4bb-a38b59a54443; Path=/; HttpOnly
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 21 Jan 2018 21:57:40 GMT

**3.143827**
```
Read cached data which is fast
```
$ curl -I -H "Cookie: SESSION=de703247-7ba7-47f8-a4bb-a38b59a54443" localhost:8080/city/MEL -w %{time_total}
HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Set-Cookie: SESSION=0f26221a-e055-4e68-b3c5-d121e53c12f1; Path=/; HttpOnly
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 21 Jan 2018 21:57:48 GMT

**0.070626**
```
Bust cache
```
$ curl -I -H "Cookie: SESSION=de703247-7ba7-47f8-a4bb-a38b59a54443" localhost:8080/cities/cache-bust
HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Set-Cookie: SESSION=309e6f30-ca89-4f46-94d9-cf9298b8222d; Path=/; HttpOnly
Content-Length: 0
Date: Sun, 21 Jan 2018 21:57:50 GMT
```
Read live data which is slow
```
$ curl -I -H "Cookie: SESSION=de703247-7ba7-47f8-a4bb-a38b59a54443" localhost:8080/city/MEL -w %{time_total}
HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Set-Cookie: SESSION=48de0e15-288c-48ab-ae9d-7fa77e9d275d; Path=/; HttpOnly
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 21 Jan 2018 21:57:55 GMT

**3.082411**
```


