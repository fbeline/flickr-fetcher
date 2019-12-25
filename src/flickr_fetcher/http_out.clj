(ns flickr-fetcher.http-out
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [flickr-fetcher.adapters :as adapters]))

(defn flickr-feed []
  (some-> (client/get "https://www.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1")
          :body
          (json/parse-string true)
          :items
          adapters/feed-wire->internal))
