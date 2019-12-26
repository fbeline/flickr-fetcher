(ns flickr-fetcher.http-out
  (:require [cheshire.core :as json]
            [clj-http.client :as client]
            [flickr-fetcher.adapters :as adapters])
(:import [javax.imageio ImageIO]))

(defn get-image [url]
  (-> (client/get url {:throw-exceptions false :as :stream})
      :body
      ImageIO/read))

(defn flickr-feed []
  (-> "https://www.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1"
      (client/get {:throw-exceptions true})
      :body
      (json/parse-string true)
      adapters/feed-wire->internal))
