(ns flickr-fetcher.image-manipulator
  (:require [clojure.java.io :as io]
            [mikera.image.core :as mikera])
  (:import java.io.File
           [javax.imageio ImageIO]))

(defn resize-image [width height binary]
  (mikera/scale-image binary width height))

(defn save-to-disk! [path file-name binary]
  (when-not (nil? binary)
    (let [file-path (str path file-name)]
      (io/make-parents file-path)
      (ImageIO/write binary "jpg" (File. file-path)))))
