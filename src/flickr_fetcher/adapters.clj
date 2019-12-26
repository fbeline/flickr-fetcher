(ns flickr-fetcher.adapters
  (:require [cuerdas.core :as str])
  (:import [javax.imageio ImageIO]))

(defn- sanitize-title [title]
  (-> title str/slug (str/prune 50)))

(defn feed-wire->internal [feed-wire]
  (some->> feed-wire
           :items
           (map (fn [{:keys [title media published]}]
                  {:title title
                   :file-name (str (sanitize-title title) ".jpg")
                   :media (:m media)
                   :published published}))))

(defn feed-internal->wire [feed-internal]
  {:images
   (map (fn [{:keys [media-binary file-name]}]
          {:image file-name
           :size  {:width  (.getWidth media-binary)
                   :height (.getHeight media-binary)}})
        feed-internal)})
