(ns flickr-fetcher.test-helper
  (:require  [flickr-fetcher.service :refer [gallery-path]]))

;; snippet from https://gist.github.com/edw/5128978
(defn delete-recursively [fname]
  (let [func (fn [func f]
               (when (.isDirectory f)
                 (doseq [f2 (.listFiles f)]
                   (func func f2)))
               (clojure.java.io/delete-file f))]
    (func func (clojure.java.io/file fname))))

(defmacro flow [description & body]
  `(try
     (with-redefs [gallery-path (fn [] "flickr/test/photos/")]
       ~@body)
     (finally
       (when (.exists (clojure.java.io/as-file "flickr/test/"))
         (delete-recursively "flickr/test/")))))
