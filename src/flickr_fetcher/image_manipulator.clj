(ns flickr-fetcher.image-manipulator
  (:require [clojure.java.io :as io]
            [cuerdas.core :as str]))

(defn- sanitize-title [title]
  (-> title str/slug (str/prune 50)))

(defn save-to-disk! [title binary]
  (if (not (nil? binary))
    (let [file-name (str "feed/photos/" (sanitize-title title) ".jpg")]
      (io/make-parents file-name)
      (with-open [w (io/output-stream file-name)]
        (.write w binary)))))
