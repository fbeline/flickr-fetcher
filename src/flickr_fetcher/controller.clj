(ns flickr-fetcher.controller
  (:require [flickr-fetcher.http-out :as http-out]
            [flickr-fetcher.image-manipulator :as image-manipulator]))

(defn with-media-binary! [item]
  (assoc item :media-binary (http-out/get-image (:media item))))

(defn resize-image [width height item]
  (if (and width height)
    (update item :media-binary (partial image-manipulator/resize-image width height))
    item))

(defn take-first-n-items [n items]
  (if n
    (take n items)
    items))

(defn fetch-feed! [{:keys [width height n]} gallery-path]
  (->> (http-out/flickr-feed)
       (take-first-n-items n)
       (pmap with-media-binary!)
       (pmap (partial resize-image width height))
       (run! (fn [{:keys [title media-binary]}]
               (image-manipulator/save-to-disk! gallery-path title media-binary)))))
