(ns flickr-fetcher.controller
  (:require [flickr-fetcher.http-out :as http-out]
            [flickr-fetcher.image-manipulator :as image-manipulator]))

(defn with-media-binary! [item]
  (assoc item :media-binary (http-out/get-image (:media item))))

(defn fetch-feed! [opts]
  (->> (http-out/flickr-feed)
       (pmap with-media-binary!)
       (run! (fn [{:keys [title media-binary]}]
               (image-manipulator/save-to-disk! title media-binary)))))
