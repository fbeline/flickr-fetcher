(ns flickr-fetcher.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
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

(defn fetch-feed
  [{:keys [json-params]}]
  (if (s/valid? ::fetch-feed-payload json-params)
    (do (controller/fetch-feed! json-params (gallery-path))
        {:status 200
         :body   {}})
    {:status 400
     :body   {}}))

(def common-interceptors [(body-params/body-params) http/json-body])

(def routes #{["/flickr/feed" :post (conj common-interceptors `fetch-feed)]})

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})
