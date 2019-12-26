(ns flickr-fetcher.service-test
  (:require [clojure.test :refer :all]
            [flickr-fetcher.test-helper :refer [flow] :as th]))

(defn flickr-feed-request [payload]
  (th/http-request :post "/api/flickr/feed" payload))

(defn flickr-feed-request-resize []
  (->> (flickr-feed-request {:n 2 :size {:height 10 :width 10}})
       :body
       :images
       (map :size)))

(deftest flickr-feed-test
  (flow "Request with empty payload"
        (is (= 201
               (:status (flickr-feed-request {}))))
        (is (pos? (th/images-count))))
  (flow "Invalid payload returns bad request"
        (is (= 400
               (:status (flickr-feed-request {:size {:height 10}})))))
  (flow "Save only 1 images"
        (is (= 201
               (:status (flickr-feed-request {:n 1}))))
        (is (= 1 (th/images-count))))
  (flow "Save and resize images to 10x10"
        (is (= [{:width 10 :height 10}
                {:width 10 :height 10}]
               (flickr-feed-request-resize))))
  (flow "Save no images if n = 0"
        (is (= 201
               (:status (flickr-feed-request {:n 0}))))
        (is (zero? (th/images-count))))
  (flow "Save no images if n is negative"
        (is (= 201
               (:status (flickr-feed-request {:n -100}))))
        (is (zero? (th/images-count)))))
