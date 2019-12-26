(ns flickr-fetcher.test-helper
  (:require  [cheshire.core :as json]
             [clojure.test :as t]
             [flickr-fetcher.service :refer [gallery-path]]
             [flickr-fetcher.service :as service]
             [io.pedestal.http :as bootstrap]
             [io.pedestal.test :refer :all]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

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

(defn- parse-body [body]
  (when body
    (json/parse-string body true)))

(defn http-request [method url payload]
  (update
   (response-for service
                 method url
                 :headers {"Content-Type" "application/json"}
                 :body (json/generate-string payload))
   :body parse-body))

(defmacro flow [d & body]
  `(try
     (with-redefs [gallery-path (fn [] "flickr/test/photos/")]
       (t/testing ~d
         ~@body))
     (finally
       (when (.exists (clojure.java.io/as-file "flickr/test/"))
         (delete-recursively "flickr/test/")))))
