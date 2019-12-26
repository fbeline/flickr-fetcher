# flickr-fetcher

Download images from flickr feed.

## Getting Started

1. Start the application: `lein run`
2. Download images from flickr feed.

```bash
curl -X POST http://localhost:8080/api/flickr/feed \
  -H "Content-Type: application/json" \
  -d '{"limit": 5, "size": {"width": 50, "height": 50}}' -v
```

3. Downloaded images default location: `{project}/flickr/photos/`

## Payload attributes

There is no required attribute.

| attr        |                                                                        |
|-------------|------------------------------------------------------------------------|
| limit       | specify the number of images to return                                 |
| size        | specify a resize width and height, ex: `{"width": 120, "height": 120}` |

## HTTP status references

| status |                                                         |
|--------|---------------------------------------------------------|
| 200    | Success, but no images were saved.                      |
| 201    | Success with one or more images being created.          |
| 400    | Invalid request payload.                                |
| 413    | No space left on disk.                                  |

## Images location

You can set the enviroment variable `GALLERY_PATH` in order to choose where the
images will be saved.

## Running the tests

```
lein test
```

## Developing

1. Start a new REPL: `lein repl`
2. Start the service in dev-mode: `(def dev-serv (run-dev))`
3. Connect your editor to the running REPL session.
   Re-evaluated code will be seen immediately in the service.

### [Docker](https://www.docker.com/) container support

1. Configure your service to accept incoming connections (edit service.clj and add  ::http/host "0.0.0.0" )
2. Build an uberjar of your service: `lein uberjar`
3. Build a Docker image: `sudo docker build -t flickr-fetcher .`
4. Run your Docker image: `docker run -p 8080:8080 flickr-fetcher`

### [OSv](http://osv.io/) unikernel support with [Capstan](http://osv.io/capstan/)

1. Build and run your image: `capstan run -f "8080:8080"`

Once the image it built, it's cached.  To delete the image and build a new one:

1. `capstan rmi flickr-fetcher; capstan build`
