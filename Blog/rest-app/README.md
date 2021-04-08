# REST ENDPOINTS

##### Here it would be shown with help of [HTTPie](https://httpie.io/), curl for humans
[Docs for HTTPie](https://httpie.io/docs)

### Show all blogs DTO
```shell
http -v GET localhost:8090/blogs-dto/
```

### Show all blogs
```shell
http -v GET localhost:8090/blogs/
```

### Create blog
```shell
http -v POST localhost:8090/blogs/ blogName=name
```

### Update blog
```shell
http -v PUT localhost:8090/blogs/ blogId=1 blogName=name
```

### Delete blog
```shell
http -v DELETE localhost:8090/blogs/3
```

### Find blog by id
```shell
http -v GET localhost:8090/blogs/1
```

## You can also use prepared in advance postman collection [here](../../doc/Postman%20collection/Blogs%20and%20posts.postman_collection.json)
