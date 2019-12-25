(ns flickr-fetcher.service-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [flickr-fetcher.service :as service]
            [cheshire.core :as json]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(defn flickr-feed-request
  [payload]
  (response-for service
                :post "/flickr/feed"
                :headers {"Content-Type" "application/json"}
                :body (json/generate-string payload)))

(deftest flickr-feed-test
  (is (=
       (:body (flickr-feed-request {}))
       "{}")))
