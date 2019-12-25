(ns flickr-fetcher.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [flickr-fetcher.controller :as controller]))

(defn gallery-path []
  (or (System/getenv "GALLERY_PATH") "flicker/feed"))

(defn fetch-feed
  [{:keys [json-params]}]
  {:status 200
   :body   (controller/fetch-feed! json-params (gallery-path))})

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
