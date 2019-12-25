(ns flickr-fetcher.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.interceptor.error :as error-int]
            [ring.util.response :as ring-resp]
            [flickr-fetcher.controller :as controller]
            [clojure.spec.alpha :as s]))

(s/def ::width int?)
(s/def ::height int?)
(s/def ::size (s/keys :req-un [::width ::height]))
(s/def ::n int?)
(s/def ::fetch-feed-payload (s/keys :opt-un [::n ::size]))

(defn gallery-path []
  (or (System/getenv "GALLERY_PATH") "flickr/photos"))

(defn fetch-feed [{:keys [json-params]}]
  (controller/fetch-feed! json-params (gallery-path))
  {:status 200
   :body   {}})

(defn validate-payload [spec]
  {:name ::validate-payload
   :enter (fn [{request :request :as context}]
            (if (s/valid? spec (:json-params request))
              context
              (throw (AssertionError. "Invalid request payload."))))})

(def service-error-handler
  (error-int/error-dispatch [ctx ex]

    [{:exception-type :java.lang.AssertionError :interceptor ::validate-payload}]
    (assoc ctx :response {:status 400 :body "Another bad one"})
   
    :else
    (assoc ctx :io.pedestal.interceptor.chain/error ex)))

(def common-interceptors [(body-params/body-params) http/json-body service-error-handler])

(def routes #{["/flickr/feed"
               :post (conj common-interceptors
                           (validate-payload ::fetch-feed-payload)
                           `fetch-feed)]})

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})
