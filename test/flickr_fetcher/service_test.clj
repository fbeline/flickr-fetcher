(ns flickr-fetcher.service-test
  (:require [clojure.test :refer :all]
            [flickr-fetcher.test-helper :refer [flow] :as th]))

(defn flickr-feed-request [payload]
  (th/http-request :post "/api/flickr/feed" payload))

(defn flickr-feed-request-resize []
  (->> (flickr-feed-request {:n 2 :size {:height 10 :width 10}})
       :body
       (map :size)))

(deftest flickr-feed-test
  (flow "Request with empty payload"
        (is (= 201
               (:status (flickr-feed-request {}))))
        (is (pos? (th/images-count))))
  (flow "Invalid payload returns bad request"
        (is (= 400
               (:status (flickr-feed-request {:size {:height 10}})))))
  (flow "Save only 3 images"
        (is (= 201
               (:status (flickr-feed-request {:n 3}))))
        (is (= 3 (th/images-count))))
  (flow "Resize all images to 10x10"
        (is (= [{:width 10 :height 10}
                {:width 10 :height 10}]
               (flickr-feed-request-resize)))))
