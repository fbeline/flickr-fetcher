(ns flickr-fetcher.test-helper
  (:require  [flickr-fetcher.service :refer [gallery-path]]))

(defn delete-recursively [fname]
;; snippet from https://gist.github.com/edw/5128978
  (let [func (fn [func f]
               (when (.isDirectory f)
                 (doseq [f2 (.listFiles f)]
                   (func func f2)))
               (clojure.java.io/delete-file f))]
    (func func (clojure.java.io/file fname))))

(defn images-count []
  (-> "flickr/test/photos/"
      clojure.java.io/file
      file-seq
      count
      dec))

(defmacro flow [description & body]
  (println (str "> " description))
  `(try
     (with-redefs [gallery-path (fn [] "flickr/test/photos/")]
       ~@body)
     (finally
       (when (.exists (clojure.java.io/as-file "flickr/test/"))
         (delete-recursively "flickr/test/")))))
