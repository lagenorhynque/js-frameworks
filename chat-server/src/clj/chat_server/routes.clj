(ns chat-server.routes
  (:require [chat-server.handler.core :as handler]
            [chat-server.handler.hello :as hello]
            [integrant.core :as ig]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route :as route]))

;;; validation rules

(def validation-schemas
  (merge hello/validations))

;;; routing

(defmethod ig/init-key ::routes
  [_ {:keys [db]}]
  (let [common-interceptors [(body-params/body-params)
                             http/json-body
                             (handler/validate validation-schemas)
                             (handler/attach-database db)]]
    #(route/expand-routes
      #{["/greet" :get (conj common-interceptors `hello/respond-hello)]})))
