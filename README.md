# Backend for -> https://saurabhrana.com
# What the app does?
1. A scheduled task is executed daily to retrieve the latest movie entries from the S3 storage.

2. Upon retrieval, the Openflix API identifies and compiles a list of files that require processing.

3. The identified files are then queued into RabbitMQ for further processing.

4. The OMDBService subsequently dequeues these files from RabbitMQ, fetches the corresponding IMDB metadata, and persists this data into the database.

5. Following any database update, the Redis cache is promptly refreshed to reflect the latest changes.

6. User requests are efficiently handled by retrieving data directly from the Redis cache, ensuring swift response times.

# High Level Design 
![image](https://github.com/saurabh2816/openflixapi/assets/4894058/3f9d0dfb-2909-4f16-aa2e-c68abe7d9f22)

# Tech Stack 
- Spring Boot
- RabbitMQ
- Redis
- MySQL

# How It Looks?
![image](https://github.com/saurabh2816/openflixapi/assets/4894058/dddafc39-c622-47c2-b954-7dbe5495e26e)

