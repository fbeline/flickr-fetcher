(ns flickr-fetcher.image-manipulator
  (:require [clojure.java.io :as io]
            [cuerdas.core :as str]
            [mikera.image.core :as mikera])
  (:import [javax.imageio ImageIO])
  (:import java.io.File)
  )

(defn- sanitize-title [title]
  (-> title str/slug (str/prune 50)))

(defn resize-image [width height binary]
  (mikera/scale-image binary width height))

(defn save-to-disk! [path title binary]
  (if (not (nil? binary))
    (let [file-name (str path (sanitize-title title) ".jpg")]
      (io/make-parents file-name)
      (ImageIO/write binary "jpg" (File. file-name)))))
