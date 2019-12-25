(ns flickr-fetcher.service-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [flickr-fetcher.service :as service]
            [cheshire.core :as json]
            [flickr-fetcher.test-helper :refer [flow]]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(defn flickr-feed-request
  [payload]
  (response-for service
                :post "/flickr/feed"
                :headers {"Content-Type" "application/json"}
                :body (json/generate-string payload)))

(deftest flickr-feed-test
  (flow "Request with empty payload"
        (is (= (:status (flickr-feed-request {}))
               200)))
  (flow "Invalid payload returns bad request"
        (is (= (:status (flickr-feed-request {:size {:height 10}}))
               400)))
  (flow "Resize all images to 10x10"
        (is (= (:status (flickr-feed-request {:size {:height 10 :width 10}}))
               200)))
  (flow "Returns only 3 images"
        (is (= (:status (flickr-feed-request {:n 3}))
               200))))
