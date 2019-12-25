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

(defmacro flow [& body]
  `(try
     (with-redefs [gallery-path (fn [] "flickr/test/photos/")]
       ~@body)
     (finally
       (delete-recursively "flickr/test/"))))
