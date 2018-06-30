(ns chat-server.routes
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]))

(defn respond-hello [request]
  (ring-resp/response {:greeting "Hello, world!"}))

(def common-interceptors [(body-params/body-params) http/json-body])

(def routes
  #{["/greet" :get (conj common-interceptors `respond-hello)]})
