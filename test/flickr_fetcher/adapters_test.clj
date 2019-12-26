(ns flickr-fetcher.adapters-test
  (:require [clojure.test :refer :all]
            [flickr-fetcher.adapters :as adapters])
  (:import java.awt.image.BufferedImage))

(def flickr-response
  {:items [{:author "nobody@flickr.com (\"noelbussang\")",
            :author_id "186192312@N04",
            :date_taken "2019-12-25T10:08:13-08:00",
            :description " <p><a href=\"https://www.flickr.com/people/186192312@N04/\">noelbussang</a> posted a photo:</p> <p><a href=\"https://www.flickr.com/photos/186192312@N04/49274621432/\" title=\"FaceApp_1527885430098\"><img src=\"https://live.staticflickr.com/65535/49274621432_358e024ec0_m.jpg\" width=\"240\" height=\"240\" alt=\"FaceApp_1527885430098\" /></a></p> ",
            :link "https://www.flickr.com/photos/186192312@N04/49274621432/",
            :media {:m "https://live.staticflickr.com/65535/49274621432_358e024ec0_m.jpg"},
            :published "2019-12-25T18:08:13Z",
            :tags "",
            :title "FaceApp_1527885430098"}]})

(deftest feed-wire->internal-test
  (testing "On feed wire to internal"
    (is (= (adapters/feed-wire->internal flickr-response)
           [{:title "FaceApp_1527885430098"
             :file-name "faceapp_1527885430098.jpg"
             :media "https://live.staticflickr.com/65535/49274621432_358e024ec0_m.jpg"
             :published "2019-12-25T18:08:13Z"}]))))

(def internal-feed
  [{:file-name    "foo.jpg"
    :media-binary (BufferedImage. 16 16 BufferedImage/TYPE_INT_ARGB)}])

(deftest feed-internal->wire
  (testing "On feed internal to wire"
    (is (= (adapters/feed-internal->wire internal-feed)
           {:images [{:image "foo.jpg"
                      :size  {:width  16
                              :height 16}}]}))))
