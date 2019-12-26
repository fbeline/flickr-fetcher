(ns flickr-fetcher.interceptors
  (:require [io.pedestal.http.body-params :as body-params]
            [io.pedestal.interceptor.error :as error-int]
            [io.pedestal.http :as http]
            [clojure.spec.alpha :as s]))

(defn validate-payload [spec]
  {:name ::validate-payload
   :enter (fn [{request :request :as context}]
            (if (s/valid? spec (:json-params request))
              context
              (throw (AssertionError. "Invalid request payload."))))})

(defn externalize [adapter-fn]
  {:name ::externalize
   :leave (fn [context]
            (update-in context [:response :body] adapter-fn))})

(def service-error-handler
  (error-int/error-dispatch [ctx ex]

    [{:exception-type :java.lang.AssertionError :interceptor ::validate-payload}]
    (assoc ctx :response {:status 400 :body {}})

    :else
    (assoc ctx :io.pedestal.interceptor.chain/error ex)))

(def common-interceptors [(body-params/body-params) http/json-body service-error-handler])
