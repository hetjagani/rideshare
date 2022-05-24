## User Info Service

### Entities
* User
* Places
* Licence
* User Reports

### APIs
* `GET /users` - Get list of users
* `GET /users/{id}` - Get userinfo by id
* `POST /users` - Add userinfo
* `PUT /users/{id}` - Update userinfo
* `DELETE /users/{id}` - Delete userinfo

* `GET /users/{userId}/places` - Get all saved places for user
* `GET /users/{userId}/places/{placeId}` - Get place by id for a user
* `POST /users/{userId}/places` - Save place for user
* `PUT /users/{userId}/places/{placeId}` - Update place by id for a user
* `DELETE /users/{userId}/places/{placeId}` - Delete place by id for a user

* `GET /users/{userId}/license` - Get license info of a user
* `POST /users/{userId}/license` - Upload license for a user
* `DELETE /users/{userId}/license` - Delete license of a user

* `GET /users/{userId}/reports` - Get reports for a user
* `GET /users/{userId}/reports/{reportId}` - Get report by id for a user
* `POST /users/{userId}/reports` - Create report for a user
* `PUT /users/{userId}/reports/{reportId}` - Update report of a user
* `DELETE /users/{userId}/reports/{reportId}` - Delete report of a user