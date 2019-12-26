(ns flickr-fetcher.service
  (:require [clojure.spec.alpha :as s]
            [flickr-fetcher.adapters :as adapters]
            [flickr-fetcher.controller :as controller]
            [flickr-fetcher.interceptors :refer [common-interceptors externalize validate-payload]]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(s/def ::width int?)
(s/def ::height int?)
(s/def ::size (s/keys :req-un [::width ::height]))
(s/def ::limit int?)
(s/def ::fetch-feed-payload (s/keys :opt-un [::limit ::size]))

(defn gallery-path []
  (or (System/getenv "GALLERY_PATH") "flickr/photos"))

(defn fetch-feed [{:keys [json-params]}]
  (let [response (controller/fetch-feed! json-params (gallery-path))]
    {:status (if (-> response count pos?) 201 200)
     :body   response}))

(def routes #{["/api/flickr/feed"
               :post (conj common-interceptors
                           (validate-payload ::fetch-feed-payload)
                           (externalize adapters/feed-internal->wire)
                           `fetch-feed)]})

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})
