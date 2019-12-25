(ns flickr-fetcher.controller
  (:require [flickr-fetcher.http-out :as http-out]
            [flickr-fetcher.image-manipulator :as image-manipulator]))

(defn with-media-binary! [item]
  (assoc item :media-binary (http-out/get-image (:media item))))

(defn resize-image [item]
  (update item :media-binary image-manipulator/resize-image))

(defn fetch-feed! [opts gallery-path]
  (->> (http-out/flickr-feed)
       (pmap with-media-binary!)
       (run! (fn [{:keys [title media-binary]}]
               (image-manipulator/save-to-disk! gallery-path title media-binary)))))
