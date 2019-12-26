(ns flickr-fetcher.controller
  (:require [flickr-fetcher.http-out :as http-out]
            [flickr-fetcher.image-manipulator :as image-manipulator]))

(defn with-media-binary! [item]
  (assoc item :media-binary (http-out/get-image (:media item))))

(defn resize-image [{:keys [width height]} item]
  (if (and width height)
    (update item :media-binary (partial image-manipulator/resize-image width height))
    item))

(defn fetch-feed! [{:keys [size n]} gallery-path]
  (->> (http-out/flickr-feed)
       (#(if n (take n %) %))
       (pmap with-media-binary!)
       (pmap (partial resize-image size))
       (pmap (fn [{:keys [file-name media-binary] :as item}]
               (image-manipulator/save-to-disk! gallery-path file-name media-binary)
               item))))
